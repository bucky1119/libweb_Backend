package com.cau.web.service;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TranslationService {

    private static final String APP_ID = "20240929002164129"; // 你的百度翻译API应用ID
    private static final String SECRET_KEY = "BIoXvnQiLRgY9KYlDpYa"; // 你的百度翻译API密钥
    private static final String TRANSLATE_API_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    public String translateTitle(String title, String targetLanguage) {
        return translate(title, targetLanguage);
    }

    public List<String> translateTitles(List<String> titles, String targetLanguage) {
        return titles.stream()
                .map(title -> translate(title, targetLanguage))
                .collect(Collectors.toList());
    }

    private String translate(String title, String targetLanguage) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 生成随机数 salt
            Random random = new Random();
            String salt = String.valueOf(random.nextInt(10000));

            // 生成签名 sign (appId + q + salt + 密钥)
            String sign = getMD5(APP_ID + title + salt + SECRET_KEY);

            // URL encode the query
            String encodedTitle = URLEncoder.encode(title, "UTF-8");

            // 创建请求参数
            StringBuilder params = new StringBuilder();
            params.append("q=").append(encodedTitle);
            params.append("&from=").append("auto");
            params.append("&to=").append(targetLanguage);
            params.append("&appid=").append(APP_ID);
            params.append("&salt=").append(salt);
            params.append("&sign=").append(sign);

            // 使用 POST 请求并设置 Content-Type 为 application/x-www-form-urlencoded
            HttpPost post = new HttpPost(TRANSLATE_API_URL);
            post.setEntity(new StringEntity(params.toString(), StandardCharsets.UTF_8));
            post.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // 发送请求并处理响应
            String response = EntityUtils.toString(httpClient.execute(post).getEntity(), "UTF-8");

            // 解析响应
            JSONObject responseJson = new JSONObject(response);
            if (responseJson.has("trans_result")) {
                return responseJson.getJSONArray("trans_result").getJSONObject(0).getString("dst");
            } else {
                // 如果返回了错误码，输出错误信息
                String errorCode = responseJson.optString("error_code");
                String errorMsg = responseJson.optString("error_msg");
                System.err.println("Error from API: " + errorMsg);
                return "翻译失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "翻译失败";
        }
    }

    // MD5签名生成
    private String getMD5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);

        // 如果长度不足32位，补0
        while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }
        return hashtext;
    }
}