package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.entity.Review;

import java.util.List;

public interface ReviewService {

    void saveReview(int recipeId, Review review);

    void deleteReviews(List<Review> reviews);

    Review getReview(int id);
}
