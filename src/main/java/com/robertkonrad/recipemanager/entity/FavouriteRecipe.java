package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "favourite_recipe", schema = "recipemanager")
public class FavouriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @Column(name = "created_date")
    private Date addToFavouriteDate;

    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    public FavouriteRecipe() {

    }

    public FavouriteRecipe(User user, Date addToFavouriteDate, Recipe recipe) {
        this.user = user;
        this.addToFavouriteDate = addToFavouriteDate;
        this.recipe = recipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Date getAddToFavouriteDate() {
        return addToFavouriteDate;
    }

    public void setAddToFavouriteDate(Date addToFavouriteDate) {
        this.addToFavouriteDate = addToFavouriteDate;
    }

    @Override
    public String toString() {
        return "FavouriteRecipe{" +
                "id=" + id +
                ", user=" + user +
                ", addToFavouriteDate=" + addToFavouriteDate +
                ", recipe=" + recipe +
                '}';
    }
}
