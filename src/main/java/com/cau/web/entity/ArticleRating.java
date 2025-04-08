package com.cau.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("article_rating")
public class ArticleRating {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("article_id")
    private Integer articleId;  // 修改类型为 Integer，保持一致性
    @TableField("expert_id")
    private Integer expertId;
    @TableField("innovation_score")
    private int innovationScore;
    @TableField("disruption_score")
    private int disruptionScore;
    @TableField("frontier_score")
    private int frontierScore;
    @TableField("industry_impact_score")
    private int industryImpactScore;
    @TableField("additional_score")
    private int additionalScore;  // 新增字段

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer  getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getExpertId() {
        return expertId;
    }

    public void setExpertId(Integer expertId) {
        this.expertId = expertId;
    }

    public int getInnovationScore() {
        return innovationScore;
    }

    public void setInnovationScore(int innovationScore) {
        this.innovationScore = innovationScore;
    }

    public int getDisruptionScore() {
        return disruptionScore;
    }

    public void setDisruptionScore(int disruptionScore) {
        this.disruptionScore = disruptionScore;
    }

    public int getFrontierScore() {
        return frontierScore;
    }

    public void setFrontierScore(int frontierScore) {
        this.frontierScore = frontierScore;
    }

    public int getIndustryImpactScore() {
        return industryImpactScore;
    }

    public void setIndustryImpactScore(int industryImpactScore) {
        this.industryImpactScore = industryImpactScore;
    }

    public int getAdditionalScore() {
        return additionalScore;
    }

    public void setAdditionalScore(int additionalScore) {
        this.additionalScore = additionalScore;
    }
}
