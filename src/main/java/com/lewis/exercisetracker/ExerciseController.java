package com.lewis.exercisetracker;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ExerciseController {

    private final UserRepository userRepo;
    private final ExerciseRepository exRepo;
    private final DateTimeFormatter fccFormat = DateTimeFormatter.ofPattern("EEE MMM dd yyyy", Locale.ENGLISH);

    @PostMapping
    public User createUser(@RequestParam String username) {
        String generatedId = UUID.randomUUID().toString();
        User newUser = new User(generatedId, username);
        return userRepo.save(newUser);
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/{id}/exercises")
    public ExerciseResponse addExercise(
            @PathVariable String id,
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

        return new ExerciseResponse(
                user.getUsername(),
                description,
                duration,
                exerciseDate.format(fccFormat),
                user.getid()
        );
    }

    @GetMapping("/{id}/logs")
    public LogResponse getLogs(@PathVariable String id) {
        User user = userRepo.findById(id).orElseThrow();
        List<Exercise> exercises = exRepo.findByUserId(id);

        List<ExerciseResponse> log = exercises.stream()
                .map(ex -> new ExerciseResponse(
                        user.getUsername(),
                        ex.getDescription(),
                        ex.getDuration(),
                        ex.getDate().format(fccFormat),
                        user.getid()))
                .collect(Collectors.toList());

        return new LogResponse(
                user.getUsername(),
                (long) log.size(),
                user.getid(),
                log
        );
    }
}