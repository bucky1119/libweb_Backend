package com.cau.web.service;

import com.cau.web.dto.AverageScoreResponse;
import com.cau.web.dto.ApiResponseNormal;
import com.cau.web.entity.ArticleRating;

import java.util.List;

public interface ArticleRatingService {
    ApiResponseNormal<Void> rateArticle(Integer articleId, Integer expertId, int innovationScore, int disruptionScore, int frontierScore, int industryImpactScore, int additionalScore);
    AverageScoreResponse getAverageValueScore(Integer articleId);
    List<ArticleRating> getArticleRatingHistory(Integer articleId);
}

