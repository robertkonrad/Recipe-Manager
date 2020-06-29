package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.dao.ReviewDAO;
import com.robertkonrad.recipemanager.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    @Override
    @Transactional
    public void saveReview(int recipeId, Review review) {
        reviewDAO.saveReview(recipeId, review);
    }
}
