package com.diamond.workoutplanner.workoutplanexercise;

import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Positive;
import com.diamond.workoutplanner.exercise.Exercise;
import com.diamond.workoutplanner.workoutplan.WorkoutPlan;

@Entity
@Table(name = "workout_plan_exercises")
public class WorkoutPlanExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_plan_id", nullable = false)
    private WorkoutPlan workoutPlan;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Positive(message = "Target sets must be greater than 0")
    @Column(nullable = false)
    private int targetSets;

    @Positive(message = "Target reps must be greater than 0")
    @Column(nullable = false)
    private int targetReps;

    public WorkoutPlanExercise() {
    }

    public WorkoutPlanExercise(WorkoutPlan workoutPlan, Exercise exercise, int targetSets, int targetReps) {
        this.workoutPlan = workoutPlan;
        this.exercise = exercise;
        this.targetSets = targetSets;
        this.targetReps = targetReps;
    }

    public Integer getId() {
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
