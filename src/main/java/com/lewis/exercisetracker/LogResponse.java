package com.lewis.exercisetracker;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class LogResponse {
    private String username;
    private Long count;
    private String id;
    private List<ExerciseResponse> log;
}