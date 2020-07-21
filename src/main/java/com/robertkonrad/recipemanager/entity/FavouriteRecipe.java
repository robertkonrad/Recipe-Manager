package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    public FavouriteRecipe() {

    }

    public FavouriteRecipe(User user, Recipe recipe) {
        this.user = user;
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

    @Override
    public String toString() {
        return "FavouriteRecipe{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", recipe=" + recipe.getTitle() +
                '}';
    }
}
