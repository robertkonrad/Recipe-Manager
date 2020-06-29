package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Recipe;
import com.robertkonrad.recipemanager.entity.Review;
import com.robertkonrad.recipemanager.entity.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;

@Repository
public class ReviewDAOImpl implements ReviewDAO {

    @Autowired
    EntityManager entityManager;

    @Override
    public void saveReview(int recipeId, Review review) {
        Session session = entityManager.unwrap(Session.class);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = session.createQuery("FROM User u WHERE u.username='" + auth.getName() + "'", User.class).getSingleResult();
        Recipe recipe = session.createQuery("FROM Recipe r WHERE r.id='" + recipeId + "'", Recipe.class).getSingleResult();
        review.setAuthor(user);
        review.setRecipe(recipe);
        Date createdDate = new Date();
        review.setCreatedDate(createdDate);
        session.save(review);
    }
}
