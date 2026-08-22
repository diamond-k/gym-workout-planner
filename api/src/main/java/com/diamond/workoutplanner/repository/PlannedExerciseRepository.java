package com.diamond.workoutplanner.repository;

import java.util.List;
import java.util.Optional;
import com.diamond.workoutplanner.entity.PlannedExercise;
import org.springframework.data.repository.ListCrudRepository;

public interface PlannedExerciseRepository extends ListCrudRepository<PlannedExercise, Integer> {
    // derived query: finds all planned exercises belonging to a workout plan
    List<PlannedExercise> findByWorkoutPlanId(int workoutPlanId);

    Optional<PlannedExercise> findByIdAndWorkoutPlanId(int plannedExerciseId, int workoutPlanId);
}
