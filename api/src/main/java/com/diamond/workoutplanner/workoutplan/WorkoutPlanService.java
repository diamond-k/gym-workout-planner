package com.diamond.workoutplanner.workoutplan;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diamond.workoutplanner.workoutplanexercise.WorkoutPlanExerciseRepository;

/*
WorkoutPlanRepository
manages the WorkoutPlan entity

WorkoutPlanExerciseRepository
is needed because deleting a WorkoutPlan must first 
delete the WorkoutPlanExercise records that belong to it
*/

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutPlanExerciseRepository workoutPlanExerciseRepository;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository,
            WorkoutPlanExerciseRepository workoutPlanExerciseRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanExerciseRepository = workoutPlanExerciseRepository;
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
        workoutPlanExerciseRepository.deleteAll(workoutPlanExerciseRepository.findByWorkoutPlanId(id));
        workoutPlanRepository.delete(existingWorkoutPlan);
    }
}