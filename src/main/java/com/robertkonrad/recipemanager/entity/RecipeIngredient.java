package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;

@Entity
@Table(name = "recipe_ingredient", schema = "recipemanager")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "amount")
    private double amount;

    @Column(name = "unit")
    private String unit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    public RecipeIngredient() {

    }

    public RecipeIngredient(String ingredientName, double amount, String unit, Recipe recipe) {
        this.ingredientName = ingredientName;
        this.amount = amount;
        this.unit = unit;
        this.recipe = recipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "RecipeIngredient{" +
                "id=" + id +
                ", ingredientName='" + ingredientName + '\'' +
                ", amount=" + amount +
                ", unit='" + unit + '\'' +
                ", recipe=" + recipe +
                '}';
    }
}
