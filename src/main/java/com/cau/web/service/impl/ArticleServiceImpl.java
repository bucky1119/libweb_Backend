package com.cau.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cau.web.entity.Article;
import com.cau.web.mapper.ArticleMapper;
import com.cau.web.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public boolean saveArticle(Article article) {
        if (article.getArticleDate() == null)
            return false;
        return this.save(article);
    }

    @Override
    public Article getArticleById(Integer id) {
        return this.getById(id);
    }

    @Override
    public List<Article> getArticlesByIds(List<Long> ids) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public Page<Article> getAllArticles(int pageNumber, int pageSize) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("article_date"); // 按照创建时间降序排序

        Page<Article> page = new Page<>(pageNumber, pageSize);
        return this.page(page, queryWrapper);
    }

    @Override
    public boolean updateArticle(Article article) {
        return this.updateById(article);
    }

    @Override
    public boolean deleteArticleById(Long id) {
        Article article = this.getById(id);
        if (article == null) {
            System.out.println("删除失败，文章不存在，ID：" + id);
            return false;
        }
        System.out.println("成功删除文章，ID：" + id);
        return this.removeById(id);
    }

    @Override
    public List<Article> getLatestArticles() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("article_date");
        queryWrapper.last("LIMIT 10"); // 获取最新的10篇文章

        return this.list(queryWrapper);
    }

    // 允许排序（数据库列名）
    private static final Set<String> ALLOWED_COLUMNS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "id", "formatted_id", "title", "author", "info_type", "post_agency", "nation", "article_date",
            "link_url", "domain", "subject", "text", "average_rating", "rating_count"
    )));

    // 将 camelCase/PascalCase 转换为 snake_case（简单实现，足够应对本项目字段）
    private static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) return input;
        StringBuilder sb = new StringBuilder();
        char[] arr = input.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (Character.isUpperCase(c)) {
                if (i > 0 && (Character.isLowerCase(arr[i - 1]) || Character.isDigit(arr[i - 1]))) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(c));
            } else if (c == '-' || c == ' ') {
                sb.append('_');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // 解析客户端传来的排序字段，兼容 camelCase / snake_case，并做白名单校验
    private static String resolveSortColumn(String sortBy) {
        if (sortBy == null || sortBy.trim().isEmpty()) return null;
        String candidate = sortBy.trim();
        String snake = toSnakeCase(candidate).toLowerCase(Locale.ROOT);
        if (ALLOWED_COLUMNS.contains(snake)) return snake;
        // 容错：若用户已传 snake_case 但大小写不同
        String lower = candidate.toLowerCase(Locale.ROOT);
        if (ALLOWED_COLUMNS.contains(lower)) return lower;
        return null; // 无效字段返回 null，调用处回退到默认排序
    }

    @Override
    public Page<Article> searchArticles(String keyword, String title, String author, String info_type, String postAgency, String text,
                                        LocalDate startDate, LocalDate endDate, String nation, String domain, String subject,
                                        String sortBy, String sortOrder, int pageNumber, int pageSize) {

        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();

        // 解析与校验排序字段（无效时不抛异常，回退到默认）
        String sortColumn = resolveSortColumn(sortBy);

        // 全局关键字 OR 模糊匹配（标题、作者、post_agency、nation、text）
        if (keyword != null && !keyword.trim().isEmpty()) {
            String kw = keyword.trim();
            queryWrapper.and(qw -> qw.like("title", kw)
                    .or().like("author", kw)
                    .or().like("post_agency", kw)
                    .or().like("nation", kw)
                    .or().like("text", kw));
        }

        // 添加条件查询
        if (title != null && !title.isEmpty()) {
            queryWrapper.like("title", title);
        }
        if (author != null && !author.isEmpty()) {
            queryWrapper.like("author", author);
        }
        if (info_type != null && !info_type.isEmpty()) {
            queryWrapper.like("info_type", info_type);
        }
        if (postAgency != null && !postAgency.isEmpty()) {
            queryWrapper.like("post_agency", postAgency);
        }
        if (text != null && !text.isEmpty()) {
            queryWrapper.like("text", text);
        }
        if (startDate != null && endDate != null) {
            queryWrapper.between("article_date", startDate, endDate);
        } else if (startDate != null) {
            queryWrapper.ge("article_date", startDate);
        } else if (endDate != null) {
            queryWrapper.le("article_date", endDate);
        }
        if (nation != null && !nation.isEmpty()) {
            queryWrapper.eq("nation", nation);
        }
        if (domain != null && !domain.isEmpty()) {
            queryWrapper.eq("domain", domain);
        }
        if (subject != null && !subject.isEmpty()) {
            queryWrapper.eq("subject", subject);
        }

        // 添加排序条件
        if (sortColumn != null && !sortColumn.isEmpty()) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                queryWrapper.orderByAsc(sortColumn);
            } else {
                queryWrapper.orderByDesc(sortColumn);
            }
        } else {
            queryWrapper.orderByDesc("article_date"); // 默认按日期降序排序
        }

        Page<Article> page = new Page<>(pageNumber, pageSize);
        return this.page(page, queryWrapper);
    }

    @Override
    public Integer getMaxId() {
        return articleMapper.getMaxId();
    }
}
