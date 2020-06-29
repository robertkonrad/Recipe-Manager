package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Review;

public interface ReviewDAO {

    void saveReview(int recipeId, Review review);
}
