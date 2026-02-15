package com.lewis.exercisetracker;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private String description;
    private Integer duration;
    private LocalDate date;

    public Exercise() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}