package com.diamond.workoutplanner.workoutplan;

import org.springframework.data.repository.ListCrudRepository;
import java.util.List;

public interface WorkoutPlanRepository extends ListCrudRepository<WorkoutPlan, Integer> {
    List<WorkoutPlan> findAllByOrderByUpdatedAtDesc();
}