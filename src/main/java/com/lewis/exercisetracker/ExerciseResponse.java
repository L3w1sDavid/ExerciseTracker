package com.lewis.exercisetracker;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExerciseResponse {
    private String username;
    private String description;
    private Integer duration;
    private String date;
    private String id;
}