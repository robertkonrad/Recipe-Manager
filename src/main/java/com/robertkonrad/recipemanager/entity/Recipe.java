package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "recipe", schema = "recipemanager")
public class Recipe {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", length = 100)
    @NotEmpty(message = "Title cannot be empty!")
    @NotNull
    @Size(max = 100)
    private String title;

    @Column(name = "directions", length = 8000)
    @NotEmpty(message = "Directions cannnot be empty!")
    @NotNull
    @Size(max = 8000)
    private String directions;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    private User lastModificatedBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "last_modificated")
    private Date lastModificated;

    @OneToMany(mappedBy = "ingredient")
    private Set<RecipeIngredient> ingredient;

    @Column(name = "image")
    private String image;

    public Recipe() {

    }

    public Recipe(@NotEmpty(message = "Title cannot be empty!") @NotNull @Size(max = 100) String title, @NotEmpty(message = "Directions cannnot be empty!") @NotNull @Size(max = 8000) String directions, User author, User lastModificatedBy, Date createdDate, Date lastModificated, Set<RecipeIngredient> ingredient, String image) {
        this.title = title;
        this.directions = directions;
        this.author = author;
        this.lastModificatedBy = lastModificatedBy;
        this.createdDate = createdDate;
        this.lastModificated = lastModificated;
        this.ingredient = ingredient;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getLastModificatedBy() {
        return lastModificatedBy;
    }

    public void setLastModificatedBy(User lastModificatedBy) {
        this.lastModificatedBy = lastModificatedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModificated() {
        return lastModificated;
    }

    public void setLastModificated(Date lastModificated) {
        this.lastModificated = lastModificated;
    }

    public Set<RecipeIngredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(Set<RecipeIngredient> ingredient) {
        this.ingredient = ingredient;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", directions='" + directions + '\'' +
                ", author=" + author +
                ", lastModificatedBy=" + lastModificatedBy +
                ", createdDate=" + createdDate +
                ", lastModificated=" + lastModificated +
                ", ingredient=" + ingredient +
                ", image='" + image + '\'' +
                '}';
    }
}
