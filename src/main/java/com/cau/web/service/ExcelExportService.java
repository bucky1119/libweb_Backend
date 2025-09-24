package com.cau.web.service;

import com.cau.web.entity.Article;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExcelExportService {

    @Autowired
    private TranslationService translationService; // 注入翻译服务

//    public void exportArticlesToExcel(List<Article> articles, HttpServletResponse response) throws IOException {
//        System.out.println("Exporting articles to Excel...");
//        if (articles == null || articles.isEmpty()) {
//            System.out.println("No articles to export.");
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No articles found to export.");
//            return;
//        }
//
//        // 提取所有文章标题并进行批量翻译
//        List<String> titles = articles.stream().map(Article::getTitle).collect(Collectors.toList());
//        List<String> translatedTitles = translationService.translateTitles(titles, "zh");
//
//        // 创建 Excel 文件
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Articles");
//
//        // 设置表头
//        String[] headers = {"Title", "Title Translation", "Author", "Info Type", "Post Agency", "Nation", "Date", "Link URL", "Domain", "Subject", "Text"};
//        Row headerRow = sheet.createRow(0);
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//        }
//
//        // 填充数据
//        int rowNum = 1;
//        for (int i = 0; i < articles.size(); i++) {
//            Article article = articles.get(i);
//            String translatedTitle = translatedTitles.get(i);
//            Row row = sheet.createRow(rowNum++);
//
//            // 打印要保存的每一行的内容
//            System.out.println("Saving article data to Excel:");
//            System.out.println("Title: " + article.getTitle());
//            System.out.println("Translated Title: " + translatedTitle);
//            System.out.println("Author: " + article.getAuthor());
//            System.out.println("Info Type: " + article.getInfoType());
//            System.out.println("Post Agency: " + article.getPostAgency());
//            System.out.println("Nation: " + article.getNation());
//            System.out.println("Date: " + article.getDate());
//            System.out.println("Link URL: " + article.getLinkUrl());
//            System.out.println("Domain: " + article.getDomain());
//            System.out.println("Subject: " + article.getSubject());
//            System.out.println("Text: " + article.getText());
//
//            // 保存到 Excel 行
//            row.createCell(0).setCellValue(article.getTitle());
//            row.createCell(1).setCellValue(translatedTitle);
//            row.createCell(2).setCellValue(article.getAuthor());
//            row.createCell(3).setCellValue(article.getInfoType());
//            row.createCell(4).setCellValue(article.getPostAgency());
//            row.createCell(5).setCellValue(article.getNation());
//            row.createCell(6).setCellValue(article.getDate().toString());
//            row.createCell(7).setCellValue(article.getLinkUrl());
//            row.createCell(8).setCellValue(article.getDomain());
//            row.createCell(9).setCellValue(article.getSubject());
//            row.createCell(10).setCellValue(article.getText());
//        }
//
//        // 设置响应头，告诉浏览器这是 Excel 文件
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=\"articles.xlsx\"");
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        // 写入数据到响应输出流
//        System.out.println("Writing workbook to response...");
//        try (ServletOutputStream outputStream = response.getOutputStream()) {
//            workbook.write(outputStream);
//            response.flushBuffer(); // 强制将缓冲区中的所有数据发送到客户端
//            System.out.println("Workbook written to response successfully.");
//        } catch (IOException e) {
//            System.err.println("Error writing workbook to response: " + e.getMessage());
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error exporting articles to Excel");
//        } finally {
//            workbook.close();
//        }
//    }

    public Workbook exportArticlesToExcel(List<Article> articles) throws IOException {
        System.out.println("Exporting articles to Excel...");
        if (articles == null || articles.isEmpty()) {
            throw new IllegalArgumentException("No articles found to export.");
        }

        // 提取所有文章标题并进行批量翻译
        List<String> titles = articles.stream().map(Article::getTitle).collect(Collectors.toList());
        List<String> translatedTitles = translationService.translateTitles(titles, "zh");

        // 创建 Excel 文件
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Articles");

        // 设置表头
        String[] headers = {"Title", "Title Translation", "Author", "Info Type", "Post Agency", "Nation", "Date", "Link URL", "Domain", "Subject", "Text"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 填充数据
        int rowNum = 1;
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            String translatedTitle = translatedTitles.get(i);
            Row row = sheet.createRow(rowNum++);

            // 保存到 Excel 行
            row.createCell(0).setCellValue(article.getTitle());
            row.createCell(1).setCellValue(translatedTitle);
            row.createCell(2).setCellValue(article.getAuthor());
            row.createCell(3).setCellValue(article.getInfoType());
            row.createCell(4).setCellValue(article.getPostAgency());
            row.createCell(5).setCellValue(article.getNation());
            row.createCell(6).setCellValue(article.getArticleDate() != null ? article.getArticleDate().toString() : "");
            row.createCell(7).setCellValue(article.getLinkUrl());
            row.createCell(8).setCellValue(article.getDomain());
            row.createCell(9).setCellValue(article.getSubject());
            row.createCell(10).setCellValue(article.getText());
        }

        return workbook;
    }


}