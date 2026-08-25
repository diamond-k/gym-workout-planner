package com.diamond.workoutplanner.exercise;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

// @RestController tells Spring: this class receives 
// HTTP requests and returns response data.
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }

    @GetMapping(params = "muscleGroup")
    public List<Exercise> getExercisesByMuscleGroup(@RequestParam MuscleGroup muscleGroup) {
        return exerciseService.getByMuscleGroup(muscleGroup);
    }
}
