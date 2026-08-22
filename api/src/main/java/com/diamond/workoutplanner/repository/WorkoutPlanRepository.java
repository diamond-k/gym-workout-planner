package com.diamond.workoutplanner.repository;

import com.diamond.workoutplanner.entity.WorkoutPlan;
import org.springframework.data.repository.ListCrudRepository;

public interface WorkoutPlanRepository extends ListCrudRepository<WorkoutPlan, Integer> {

}