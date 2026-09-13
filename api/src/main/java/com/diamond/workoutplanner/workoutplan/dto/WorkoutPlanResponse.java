package com.diamond.workoutplanner.workoutplan.dto;

import java.time.Instant;

public record WorkoutPlanResponse(
        int id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}