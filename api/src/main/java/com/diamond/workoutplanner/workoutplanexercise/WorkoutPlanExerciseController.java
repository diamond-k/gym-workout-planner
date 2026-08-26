package com.diamond.workoutplanner.workoutplanexercise;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workout-plans/{workoutPlanId}/workout-plan-exercises")
public class WorkoutPlanExerciseController {

    private final WorkoutPlanExerciseService workoutPlanExerciseService;

    public WorkoutPlanExerciseController(WorkoutPlanExerciseService workoutPlanExerciseService) {
        this.workoutPlanExerciseService = workoutPlanExerciseService;
    }

    @GetMapping
    public List<WorkoutPlanExercise> getWorkoutPlanExercisesByWorkoutPlanId(@PathVariable int workoutPlanId) {
        return workoutPlanExerciseService.getWorkoutPlanExercisesByWorkoutPlanId(workoutPlanId);
    }

    public record CreateWorkoutPlanExerciseRequest(
        @Positive(message = "Exercise id must be greater than 0")
        int exerciseId,

        @Positive(message = "Target sets must be greater than 0")
        int targetSets,

        @Positive(message = "Target reps must be greater than 0")
        int targetReps) {
    }

    @PostMapping
    public WorkoutPlanExercise createWorkoutPlanExercise(
            @PathVariable int workoutPlanId,
            @Valid @RequestBody CreateWorkoutPlanExerciseRequest request) {

        return workoutPlanExerciseService.createWorkoutPlanExercise(
                workoutPlanId,
                request.exerciseId(),
                request.targetSets(),
                request.targetReps());
    }

    @PutMapping("/{workoutPlanExerciseId}")
    public WorkoutPlanExercise updateWorkoutPlanExercise(
            @PathVariable int workoutPlanId,
            @PathVariable int workoutPlanExerciseId,
            @Valid @RequestBody WorkoutPlanExercise updatedWorkoutPlanExercise) {

        return workoutPlanExerciseService.updateWorkoutPlanExercise(
                workoutPlanId,
                workoutPlanExerciseId,
                updatedWorkoutPlanExercise);
    }

    @DeleteMapping("/{workoutPlanExerciseId}")
    public void deleteWorkoutPlanExercise(
            @PathVariable int workoutPlanId,
            @PathVariable int workoutPlanExerciseId) {

        workoutPlanExerciseService.deleteWorkoutPlanExercise(
                workoutPlanId,
                workoutPlanExerciseId);
    }
}