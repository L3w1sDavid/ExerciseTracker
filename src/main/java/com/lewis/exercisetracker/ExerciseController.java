package com.lewis.exercisetracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class ExerciseController {

    @Autowired private UserRepository userRepo;
    @Autowired private ExerciseRepository exRepo;

    private final DateTimeFormatter fccFormat = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);

    @PostMapping
    public User createUser(@RequestParam String username) {
        String generatedId = UUID.randomUUID().toString();
        User newUser = new User(generatedId, username); // Passing ID here
        return userRepo.save(newUser);
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/{_id}/exercises")
    public Map<String, Object> addExercise(
            @PathVariable("_id") String id,
            @RequestParam String description,
            @RequestParam Integer duration,
            @RequestParam(required = false) String date) {

        User user = userRepo.findById(id).orElseThrow();
        LocalDate exerciseDate = (date == null || date.isEmpty()) ? LocalDate.now() : LocalDate.parse(date);

        Exercise ex = new Exercise();
        ex.setUserId(id);
        ex.setDescription(description);
        ex.setDuration(duration);
        ex.setDate(exerciseDate);
        exRepo.save(ex);

        return Map.of(
                "_id", user.get_id(),
                "username", user.getUsername(),
                "date", exerciseDate.format(fccFormat),
                "duration", duration,
                "description", description
        );
    }

    @GetMapping("/{_id}/logs")
    public Map<String, Object> getLogs(
            @PathVariable("_id") String id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) Integer limit) {

        User user = userRepo.findById(id).orElseThrow();
        List<Exercise> logs = exRepo.findByUserId(id);

        if (from != null) {
            LocalDate fromDate = LocalDate.parse(from);
            logs = logs.stream().filter(e -> !e.getDate().isBefore(fromDate)).collect(Collectors.toList());
        }
        if (to != null) {
            LocalDate toDate = LocalDate.parse(to);
            logs = logs.stream().filter(e -> !e.getDate().isAfter(toDate)).collect(Collectors.toList());
        }
        if (limit != null) {
            logs = logs.stream().limit(limit).collect(Collectors.toList());
        }

        List<Map<String, Object>> logList = logs.stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("description", e.getDescription());
                    map.put("duration", e.getDuration());
                    map.put("date", e.getDate().format(fccFormat));
                    return map;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("_id", user.get_id());
        response.put("username", user.getUsername());
        response.put("count", logs.size());
        response.put("log", logList);
        return response;
    }
}