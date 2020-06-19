package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;

@Entity
@Table(name = "recipe_ingredient", schema = "recipemanager")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient")
    private Ingredient ingredient;

    public RecipeIngredient() {

    }

    public RecipeIngredient(Recipe recipe, Ingredient ingredient) {
        this.recipe = recipe;
        this.ingredient = ingredient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "id=" + id +
                ", recipe=" + recipe +
                ", ingredient=" + ingredient +
                '}';
    }
}
