package com.diamond.workoutplanner.workoutplanexercise.dto;

import com.diamond.workoutplanner.exercise.MuscleGroup;

public record WorkoutPlanExerciseResponse(
        int id,
        int exerciseId,
        String exerciseName,
        MuscleGroup muscleGroup,
        String instructions,
        int targetSets,
        int targetReps
) {
}