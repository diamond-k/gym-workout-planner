package com.diamond.workoutplanner.workoutplan;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "workout_plans") // maps this class to the 'workout_plans' table
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Workout plan name must be 255 characters or less")
    @Column(nullable = false)
    private String name;

    @Size(max = 1000, message = "Workout plan description must be 1000 characters or less")
    @Column(length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public WorkoutPlan() {
    }

    public WorkoutPlan(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}
