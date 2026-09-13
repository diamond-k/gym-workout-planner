package com.diamond.workoutplanner.workoutplan;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.Instant;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.Pattern;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import com.diamond.workoutplanner.workoutplanexercise.WorkoutPlanExercise;

@Entity
@Table(name = "workout_plans") // maps this class to the 'workout_plans' table
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be 255 characters or less")
    @Pattern(
        regexp = "^\\s*$|.*[\\p{L}\\p{N}].*",
        message = "Name must contain at least one letter or number"
    )
    @Column(nullable = false)
    private String name;

    @Size(max = 1000, message = "Description must be 1000 characters or less")
    @Column(length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @OneToMany(
        mappedBy = "workoutPlan",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<WorkoutPlanExercise> exercises = new ArrayList<>();

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @PrePersist
    public void setTimestamps() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    
    public List<WorkoutPlanExercise> getExercises() {
        return exercises;
    }

    public void addExercise(WorkoutPlanExercise workoutPlanExercise) {
        exercises.add(workoutPlanExercise);
    }

    public void removeExercise(WorkoutPlanExercise workoutPlanExercise) {
        exercises.remove(workoutPlanExercise);
    }
}
