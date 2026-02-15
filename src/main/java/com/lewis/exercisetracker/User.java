package com.lewis.exercisetracker;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty; // Make sure this import is here!

@Entity
@Table(name = "app_users")
public class User {
    @Id
    @JsonProperty("_id")
    private String _id;

    private String username;

    public User() {}

    public User(String id, String username) {
        this._id = id;
        this.username = username;
    }


    public String get_id() { return _id; }
    public String getUsername() { return username; }
}