package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "ingredient", schema = "recipemanager")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true, length = 30)
    @NotNull
    @NotEmpty(message = "Name of ingredient cannot be empty!")
    @Size(max = 30)
    private String name;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeIngredient> recipe;

    public Ingredient() {

    }

    public Ingredient(@NotNull @NotEmpty @Size(max = 30) String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RecipeIngredient> getRecipe() {
        return recipe;
    }

    public void setRecipe(Set<RecipeIngredient> recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
