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
        if (article.getDate() == null)
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
        queryWrapper.orderByDesc("date"); // 按照创建时间降序排序

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
        queryWrapper.orderByDesc("date");
        queryWrapper.last("LIMIT 10"); // 获取最新的10篇文章

        return this.list(queryWrapper);
    }

    // 使用 Java 8 兼容的 Set 实现
    private static final Set<String> ALLOWED_SORT_FIELDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            "date", "title", "author", "info_type", "post_agency", "text", "nation", "domain", "subject"
    )));

    @Override
    public Page<Article> searchArticles(String title, String author, String info_type, String postAgency, String text,
                                        LocalDate startDate, LocalDate endDate, String nation, String domain, String subject,
                                        String sortBy, String sortOrder, int pageNumber, int pageSize) {

        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();

        // 添加排序字段校验
        if (sortBy != null && !ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sortBy field: " + sortBy);
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
            queryWrapper.between("date", startDate, endDate);
        } else if (startDate != null) {
            queryWrapper.ge("date", startDate);
        } else if (endDate != null) {
            queryWrapper.le("date", endDate);
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
        if (sortBy != null && !sortBy.isEmpty()) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                queryWrapper.orderByAsc(sortBy);
            } else {
                queryWrapper.orderByDesc(sortBy);
            }
        } else {
            queryWrapper.orderByDesc("date"); // 默认按日期降序排序
        }

        Page<Article> page = new Page<>(pageNumber, pageSize);
        return this.page(page, queryWrapper);
    }

    @Override
    public Integer getMaxId() {
        return articleMapper.getMaxId();
    }
}
