package com.robertkonrad.recipemanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "authorities", schema = "blog")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "authority", columnDefinition = "varchar(255) default 'USER'")
    private String authority = "USER";

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private User username;

    public Role() {

    }

    public Role(String authority, User user) {
        super();
        this.authority = authority;
        this.username = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public User getUser() {
        return username;
    }

    public void setUser(User user) {
        this.username = user;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", authority=" + authority + "]";
    }


}
