package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.entity.Review;

public interface ReviewService {

    void saveReview(int recipeId, Review review);
}
