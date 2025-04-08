package com.cau.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
@TableName("web_data_table")
public class Article {

    @TableId(type = IdType.AUTO)  // 指定自动生成策略
    private Integer id;
    @TableField("formatted_id")
    private String formattedId; // 新增的字段
    @TableField("title")
    private String title;
    @TableField("author")
    private String author;
    @TableField("info_type")
    private String infoType;
    @TableField("post_agency")
    private String postAgency;
    @TableField("nation")
    private String nation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("article_date")
    private Date date;

    @TableField("link_url")
    private String linkUrl;
    @TableField("domain")
    private String domain;
    @TableField("subject")
    private String subject;
    @TableField("text")
    private String text;
    @TableField("average_rating")
    private Double averageRating;
    @TableField("rating_count")
    private Integer ratingCount;


    // 构造方法
    public Article() {
    }

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public String getFormattedId() {
        return formattedId;
    }

    public void setFormattedId(String formattedId) {
        this.formattedId = formattedId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getPostAgency() {
        return postAgency;
    }

    public void setPostAgency(String postAgency) {
        this.postAgency = postAgency;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // toString 方法，用于打印对象信息
    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", infoType='" + infoType + '\'' +
                ", postAgency='" + postAgency + '\'' +
                ", nation='" + nation + '\'' +
                ", date=" + date +
                ", linkUrl='" + linkUrl + '\'' +
                ", domain='" + domain + '\'' +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}

