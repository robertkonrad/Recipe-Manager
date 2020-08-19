package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Review;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReviewDAOImpl implements ReviewDAO {

    @Autowired
    EntityManager entityManager;

    @Override
    public void saveReview(Review review) {
        Session session = entityManager.unwrap(Session.class);
        session.save(review);
    }

    @Override
    public void deleteReviews(List<Review> reviews) {
        Session session = entityManager.unwrap(Session.class);
        for (Review review : reviews) {
            session.delete(review);
        }
    }

    @Override
    public Review getReview(int id) {
        Session session = entityManager.unwrap(Session.class);
        try {
            return session.get(Review.class, id);
        } catch (NullPointerException e) {
            return null;
        }
    }
}
