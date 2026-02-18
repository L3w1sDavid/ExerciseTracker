package com.lewis.exercisetracker;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty; // Make sure this import is here!
import lombok.Getter;

@Entity
@Table(name = "app_users")
public class User {
    @Id
    @JsonProperty("id")
    private String id;

    @Getter
    private String username;

    public User() {}

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }


    public String getid() { return id; }


}