package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "review", schema = "recipemanager")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Description cannot be empty.")
    @NotNull
    @Size(max = 2000, min = 1, message = "Max length is 2000 chars.")
    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    @Column(name = "stars")
    private int stars;

    public Review() {

    }

    public Review(@NotEmpty(message = "Description cannot be empty.") @NotNull @Size(max = 2000, min = 1, message = "Max length is 2000 chars.") String description, Date createdDate, User author, Recipe recipe) {
        this.description = description;
        this.createdDate = createdDate;
        this.author = author;
        this.recipe = recipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    @Override
    public String toString() {
        return "Review{" +
                "description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", author=" + author +
                ", recipe=" + recipe +
                ", stars=" + stars +
                '}';
    }
}
