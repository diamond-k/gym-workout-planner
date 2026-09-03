package com.diamond.workoutplanner.workoutplan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateWorkoutPlanRequest(

    @NotBlank(message = "Workout plan name is required")
    @Size(max = 255, message = "Workout plan name must be 255 characters or fewer")
    String name,

    @Size(max = 1000, message = "Description must be 1000 characters or fewer")
    String description

) {
}