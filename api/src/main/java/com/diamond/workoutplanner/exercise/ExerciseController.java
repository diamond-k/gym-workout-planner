package com.diamond.workoutplanner.exercise;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diamond.workoutplanner.exception.InvalidMuscleGroupException;
import com.diamond.workoutplanner.exercise.dto.ExerciseResponse;
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
    public List<ExerciseResponse> getAllExercises() {
        return exerciseService.getAllExercises()
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @GetMapping(params = "muscleGroup")
    public List<ExerciseResponse> getExercisesByMuscleGroup(@RequestParam String muscleGroup) {
        try {
            MuscleGroup parsedMuscleGroup =
                    MuscleGroup.valueOf(muscleGroup.toUpperCase());

            return exerciseService.getByMuscleGroup(parsedMuscleGroup)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

        } catch (IllegalArgumentException exception) {
            throw new InvalidMuscleGroupException(
                    "Invalid muscle group: " + muscleGroup);
        }
    }

    private ExerciseResponse mapToResponse(Exercise exercise) {
        return new ExerciseResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getMuscleGroup(),
                exercise.getInstructions()
        );
    }
}
