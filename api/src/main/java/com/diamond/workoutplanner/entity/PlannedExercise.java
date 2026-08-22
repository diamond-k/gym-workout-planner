package com.diamond.workoutplanner.entity;

import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "planned_exercises")
public class PlannedExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Positive
    @Column(nullable = false)
    private int targetSets;

    @Positive
    @Column(nullable = false)
    private int targetReps;

    public PlannedExercise() {
    }

    public PlannedExercise(WorkoutPlan workoutPlan, Exercise exercise, int targetSets, int targetReps) {
        this.workoutPlan = workoutPlan;
        this.exercise = exercise;
        this.targetSets = targetSets;
        this.targetReps = targetReps;
    }   

    public int getId() {
        return id;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getTargetSets() {
        return targetSets;
    }

    public int getTargetReps() {
        return targetReps;
    }

    public void setTargetSets(int targetSets) {
        this.targetSets = targetSets;
    }

    public void setTargetReps(int targetReps) {
        this.targetReps = targetReps;
    }
}
