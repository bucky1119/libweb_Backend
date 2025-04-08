package com.cau.web.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cau.web.dto.ApiResponseNormal;
import com.cau.web.entity.Article;
import com.cau.web.service.ArticleService;
import com.cau.web.service.ExcelExportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ExcelExportService excelExportService; // 注入 Excel 导出服务

    @Secured({"ROLE_LIBRARIAN", "ROLE_ADMIN"})
    @PostMapping("/add")
    public ApiResponseNormal<Article> addArticle(@RequestBody Article article) {
        System.out.println("Received Article: " + article); // 添加日志输出
        boolean isSuccess = articleService.saveArticle(article);
        if (isSuccess) {
            return new ApiResponseNormal<>(200, article, "文章已添加");
        } else {
            return new ApiResponseNormal<>(500, null, "文章添加失败");
        }
    }

    @Secured({"ROLE_LIBRARIAN", "ROLE_ADMIN"})
    @PutMapping("/{id}")
    public ApiResponseNormal<Article> updateArticle(@PathVariable Integer id, @RequestBody Article article) {
        article.setId(id);
        boolean isSuccess = articleService.updateArticle(article);
        if (isSuccess) {
            return new ApiResponseNormal<>(200, article, "文章已更新");
        } else {
            return new ApiResponseNormal<>(500, null, "文章更新失败");
        }
    }

    @Secured({"ROLE_LIBRARIAN", "ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ApiResponseNormal<Void> deleteArticle(@PathVariable Long id) {
        boolean isSuccess = articleService.deleteArticleById(id);
        if (isSuccess) {
            return new ApiResponseNormal<>(200, null, "文章已删除");
        } else {
            return new ApiResponseNormal<>(500, null, "文章删除失败");
        }
    }

    @GetMapping("/{id}")
    public ApiResponseNormal<Article> getArticleById(@PathVariable Integer id) {
        Article article = articleService.getArticleById(id);
        if (article != null) {
            return new ApiResponseNormal<>(200, article, "获取文章成功");
        } else {
            return new ApiResponseNormal<>(404, null, "文章未找到");
        }
    }

    // 添加文章筛选与排序接口
    @GetMapping("/search")
    public ApiResponseNormal<Page<Article>> searchArticles(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String infoType,
            @RequestParam(required = false) String postAgency,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String nation,
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String sortBy,  // 排序字段（如"date"、"averageRating"）
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,  // 默认降序
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<Article> articles = articleService.searchArticles(
                title, author, infoType, postAgency, text, startDate, endDate, nation, domain, subject,
                sortBy, sortOrder, pageNumber, pageSize);
        return new ApiResponseNormal<>(200, articles, "筛选与排序成功");
    }

    // 导出文章列表为 Excel
    @GetMapping("/export")
    public void exportArticles(@RequestParam String articleIds, HttpServletResponse response) throws IOException {


        // 将逗号分隔的字符串转换为列表
        List<Long> ids = Arrays.stream(articleIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // 根据ID列表获取文章
        List<Article> articles = articleService.getArticlesByIds(ids);
        // System.out.println("获取的文章列表: " + articles);

        // 检查是否有文章数据
        if (articles == null || articles.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No articles found to export.");
            return;
        }

        // 调用导出服务生成 Excel
        Workbook workbook = excelExportService.exportArticlesToExcel(articles);

        // 设置响应头，告诉浏览器这是一个 Excel 文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"articles.xlsx\"");
        response.setStatus(HttpServletResponse.SC_OK);

//        // 调用导出服务生成 Excel
//        try (ServletOutputStream outputStream = response.getOutputStream()) {
//            excelExportService.exportArticlesToExcel(articles, response);
//            outputStream.flush();
//        } catch (IOException e) {
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error exporting articles to Excel");
//        }

        // 打印 Excel 中要写入的数据
        System.out.println("Printing data to be exported:");
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (Row row : sheet) {
                StringBuilder rowData = new StringBuilder();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.append(cell.getStringCellValue()).append(" | ");
                            break;
                        case NUMERIC:
                            rowData.append(cell.getNumericCellValue()).append(" | ");
                            break;
                        case BOOLEAN:
                            rowData.append(cell.getBooleanCellValue()).append(" | ");
                            break;
                        case FORMULA:
                            rowData.append(cell.getCellFormula()).append(" | ");
                            break;
                        default:
                            rowData.append("NULL").append(" | ");
                            break;
                    }
                }
                System.out.println(rowData.toString());
            }
        }

        System.out.println("Data printed successfully. Proceeding to write to response...");


        // 写入数据到响应输出流
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            workbook.write(outputStream);
            response.flushBuffer(); // 强制将缓冲区中的所有数据发送到客户端
            System.out.println("Workbook written to response successfully.");
        } catch (IOException e) {
            System.err.println("Error writing workbook to response: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error exporting articles to Excel");
        } finally {
            workbook.close();
        }
    }

}
