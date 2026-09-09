package com.diamond.workoutplanner.workoutplanexercise;

import java.util.List;
import org.springframework.data.repository.ListCrudRepository;

public interface WorkoutPlanExerciseRepository extends ListCrudRepository<WorkoutPlanExercise, Integer> {
    // derived query: finds all workout plan exercises belonging to a workout plan
    // ordered by their position in the workout plan
    // method name tells Spring what query to build.
    List<WorkoutPlanExercise> findByWorkoutPlanIdOrderByPositionAsc(int workoutPlanId);
}
