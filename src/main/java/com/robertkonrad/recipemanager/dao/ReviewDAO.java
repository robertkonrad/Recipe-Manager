package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Review;

import java.util.List;

public interface ReviewDAO {

    void saveReview(Review review);

    void deleteReviews(List<Review> reviews);

    Review getReview(int id);
}
