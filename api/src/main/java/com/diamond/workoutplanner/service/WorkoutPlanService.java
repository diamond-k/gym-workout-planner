package com.diamond.workoutplanner.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.diamond.workoutplanner.entity.WorkoutPlan;
import com.diamond.workoutplanner.repository.PlannedExerciseRepository;
import com.diamond.workoutplanner.repository.WorkoutPlanRepository;

/*
WorkoutPlanRepository
manages the WorkoutPlan entity

PlannedExerciseRepository
is needed because deleting a WorkoutPlan must first 
delete the PlannedExercise records that belong to it
*/

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final PlannedExerciseRepository plannedExerciseRepository;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository, PlannedExerciseRepository plannedExerciseRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.plannedExerciseRepository = plannedExerciseRepository;
    }

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    public WorkoutPlan getWorkoutPlanById(int id) {
        return workoutPlanRepository.findById(id).orElseThrow();
    }

    public WorkoutPlan createWorkoutPlan(WorkoutPlan workoutPlan) {
        return workoutPlanRepository.save(workoutPlan);
    }

    public WorkoutPlan updateWorkoutPlan(int id, WorkoutPlan updatedWorkoutPlan) {
        WorkoutPlan existingWorkoutPlan = getWorkoutPlanById(id);
        existingWorkoutPlan.setName(updatedWorkoutPlan.getName());
        existingWorkoutPlan.setDescription(updatedWorkoutPlan.getDescription());
        return workoutPlanRepository.save(existingWorkoutPlan);
    }

    @Transactional
    public void deleteWorkoutPlan(int id) {
        WorkoutPlan existingWorkoutPlan = getWorkoutPlanById(id);
        plannedExerciseRepository.deleteAll(
            plannedExerciseRepository.findByWorkoutPlanId(id)
        );
        workoutPlanRepository.delete(existingWorkoutPlan);
    }
}