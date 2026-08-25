package com.diamond.workoutplanner.repository;

import java.util.List;
import java.util.Optional;
import com.diamond.workoutplanner.entity.WorkoutPlanExercise;

import org.springframework.data.repository.ListCrudRepository;

public interface WorkoutPlanExerciseRepository extends ListCrudRepository<WorkoutPlanExercise, Integer> {
    // derived query: finds all workout plan exercises belonging to a workout plan
    List<WorkoutPlanExercise> findByWorkoutPlanId(int workoutPlanId);

    Optional<WorkoutPlanExercise> findByIdAndWorkoutPlanId(int workoutPlanExerciseId, int workoutPlanId);
}
