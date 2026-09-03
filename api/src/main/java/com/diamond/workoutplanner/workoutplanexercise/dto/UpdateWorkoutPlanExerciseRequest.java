package com.diamond.workoutplanner.workoutplanexercise.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateWorkoutPlanExerciseRequest(
    @NotNull(message = "Target sets are required")
    @Positive(message = "Target sets must be greater than 0")
    Integer targetSets,

    @NotNull(message = "Target reps are required")
    @Positive(message = "Target reps must be greater than 0")
    Integer targetReps
) {
}