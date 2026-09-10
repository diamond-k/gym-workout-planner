package com.diamond.workoutplanner.workoutplan.dto;

import java.time.LocalDateTime;

public record WorkoutPlanResponse(
        int id,
        String name,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}