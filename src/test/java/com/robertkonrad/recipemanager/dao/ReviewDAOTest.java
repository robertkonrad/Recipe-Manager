package com.robertkonrad.recipemanager.dao;

import com.robertkonrad.recipemanager.entity.Review;
import com.robertkonrad.recipemanager.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ReviewDAOTest {

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private UserDAO userDAO;

    @Transactional
    @Test
    @Rollback
    public void saveReviewTest() {
        Review review = new Review();
        review.setCreatedDate(new Date());
        User user = new User("__ss767test6667ss__", "password", 1, "__ss767test6667ss__@a.a");
        String username = userDAO.saveUser(user);
        review.setAuthor(user);
        Assert.assertEquals(0, review.getId());
        reviewDAO.saveReview(review);
        Assert.assertEquals("__ss767test6667ss__", review.getAuthor().getUsername());
        Assert.assertNotEquals(0, review.getId());
    }

    @Transactional
    @Test
    @Rollback
    public void deleteReviewsTest() {
        Review review = new Review();
        Review review2 = new Review();
        Review review3 = new Review();
        reviewDAO.saveReview(review);
        reviewDAO.saveReview(review2);
        reviewDAO.saveReview(review3);
        List<Review> reviews = new ArrayList<>();
        reviews.add(review);
        reviews.add(review2);
        reviews.add(review3);
        Assert.assertEquals(review.getId(), reviewDAO.getReview(review.getId()).getId());
        Assert.assertEquals(review2.getId(), reviewDAO.getReview(review2.getId()).getId());
        Assert.assertEquals(review3.getId(), reviewDAO.getReview(review3.getId()).getId());
        reviewDAO.deleteReviews(reviews);
        Assert.assertNull(reviewDAO.getReview(review.getId()));
        Assert.assertNull(reviewDAO.getReview(review2.getId()));
        Assert.assertNull(reviewDAO.getReview(review3.getId()));
    }

    @Transactional
    @Test
    @Rollback
    public void getReviewTest() {
        Review review = new Review();
        review.setDescription("test");
        reviewDAO.saveReview(review);
        Assert.assertNotNull(reviewDAO.getReview(review.getId()));
        Assert.assertEquals("test", reviewDAO.getReview(review.getId()).getDescription());
    }
}
