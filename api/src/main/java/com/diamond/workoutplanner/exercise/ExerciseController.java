package com.diamond.workoutplanner.exercise;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diamond.workoutplanner.exception.InvalidMuscleGroupException;
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
    public List<Exercise> getExercisesByMuscleGroup(@RequestParam String muscleGroup) {
        try {      
            MuscleGroup parsedMuscleGroup = MuscleGroup.valueOf(muscleGroup.toUpperCase());
            return exerciseService.getByMuscleGroup(parsedMuscleGroup);
        } catch (IllegalArgumentException exception) {
            throw new InvalidMuscleGroupException(
                    "Invalid muscle group: " + muscleGroup);
        }
    }
}
