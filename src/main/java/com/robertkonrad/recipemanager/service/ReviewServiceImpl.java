package com.robertkonrad.recipemanager.service;

import com.robertkonrad.recipemanager.dao.RecipeDAO;
import com.robertkonrad.recipemanager.dao.ReviewDAO;
import com.robertkonrad.recipemanager.dao.UserDAO;
import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.Review;
import com.robertkonrad.recipemanager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RecipeDAO recipeDAO;

    @Override
    @Transactional
    public void saveReview(int recipeId, Review review) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userDAO.getUser(auth.getName());
        Recipe recipe = recipeDAO.getRecipe(recipeId);
        review.setAuthor(user);
        review.setRecipe(recipe);
        Date createdDate = new Date();
        review.setCreatedDate(createdDate);
        reviewDAO.saveReview(review);
    }

    @Override
    @Transactional
    public void deleteReviews(List<Review> reviews) {
        reviewDAO.deleteReviews(reviews);
    }
}
