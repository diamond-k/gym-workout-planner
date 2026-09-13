package com.diamond.workoutplanner.workoutplan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import com.diamond.workoutplanner.workoutplanexercise.dto.CreateWorkoutPlanExerciseRequest;

public record UpdateWorkoutPlanRequest(

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be 255 characters or less")
    @Pattern(
        regexp = "^\\s*$|.*[\\p{L}\\p{N}].*",
        message = "Name must contain at least one letter or number"
    )
    String name,

    @Size(max = 1000, message = "Description must be 1000 characters or less")
    String description,

    @Valid
    @NotEmpty(message = "At least one exercise is required")
    List<CreateWorkoutPlanExerciseRequest> exercises

) {
}