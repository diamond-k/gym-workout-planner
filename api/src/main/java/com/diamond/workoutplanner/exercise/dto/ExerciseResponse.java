package com.diamond.workoutplanner.exercise.dto;

import com.diamond.workoutplanner.exercise.MuscleGroup;

public record ExerciseResponse(
        int id,
        String name,
        MuscleGroup muscleGroup,
        String instructions
) {
}