package com.cau.web.Controller;

import com.cau.web.service.TranslationService;
import com.cau.web.dto.ApiResponseNormal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/translate")
public class TranslationController {

    @Autowired
    private TranslationService translationService;

    @PostMapping("/title")
    public ApiResponseNormal<String> translateTitle(@RequestParam String title, @RequestParam String targetLanguage) {
        // 调用翻译服务
        String translatedTitle = translationService.translateTitle(title, targetLanguage);

        if (translatedTitle.startsWith("翻译失败")) {
            return new ApiResponseNormal<>(500, translatedTitle, "翻译失败");
        }
        return new ApiResponseNormal<>(200, translatedTitle, "翻译成功");
    }

}
