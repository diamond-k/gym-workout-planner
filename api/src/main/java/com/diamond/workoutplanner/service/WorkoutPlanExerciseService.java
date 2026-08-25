package com.diamond.workoutplanner.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.diamond.workoutplanner.entity.Exercise;
import com.diamond.workoutplanner.entity.WorkoutPlanExercise;
import com.diamond.workoutplanner.entity.WorkoutPlan;
import com.diamond.workoutplanner.repository.ExerciseRepository;
import com.diamond.workoutplanner.repository.WorkoutPlanExerciseRepository;
import com.diamond.workoutplanner.repository.WorkoutPlanRepository;

/*
WorkoutPlanExerciseRepository
- create/update/delete the WorkoutPlanExercise

WorkoutPlanRepository
- finds the workout plan it belongs to

ExerciseRepository 
- finds the catalogue exercise being added
*/

@Service
public class WorkoutPlanExerciseService {

    private final WorkoutPlanExerciseRepository workoutPlanExerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutPlanExerciseService(WorkoutPlanExerciseRepository workoutPlanExerciseRepository,
            WorkoutPlanRepository workoutPlanRepository, ExerciseRepository exerciseRepository) {
        this.workoutPlanExerciseRepository = workoutPlanExerciseRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
    }

    private WorkoutPlanExercise getWorkoutPlanExerciseByIdAndWorkoutPlanId(int workoutPlanExerciseId, int workoutPlanId) {
        return workoutPlanExerciseRepository
                .findByIdAndWorkoutPlanId(workoutPlanExerciseId, workoutPlanId)
                .orElseThrow();
    }

    public List<WorkoutPlanExercise> getWorkoutPlanExercisesByWorkoutPlanId(int workoutPlanId) {
        return workoutPlanExerciseRepository.findByWorkoutPlanId(workoutPlanId);
    }

    public WorkoutPlanExercise createWorkoutPlanExercise(int workoutPlanId, int exerciseId, int targetSets, int targetReps) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId).orElseThrow();
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow();
        WorkoutPlanExercise workoutPlanExercise = new WorkoutPlanExercise(workoutPlan, exercise, targetSets, targetReps);
        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    public WorkoutPlanExercise updateWorkoutPlanExercise(int workoutPlanId, int workoutPlanExerciseId, WorkoutPlanExercise updatedWorkoutPlanExercise) {
        WorkoutPlanExercise existingWorkoutPlanExercise = getWorkoutPlanExerciseByIdAndWorkoutPlanId(workoutPlanExerciseId, workoutPlanId);
        existingWorkoutPlanExercise.setTargetReps(updatedWorkoutPlanExercise.getTargetReps());
        existingWorkoutPlanExercise.setTargetSets(updatedWorkoutPlanExercise.getTargetSets());
        return workoutPlanExerciseRepository.save(existingWorkoutPlanExercise);
    }

    public void deleteWorkoutPlanExercise(int workoutPlanId, int workoutPlanExerciseId) {
        WorkoutPlanExercise existingWorkoutPlanExercise = 
        getWorkoutPlanExerciseByIdAndWorkoutPlanId(workoutPlanExerciseId, workoutPlanId);
        workoutPlanExerciseRepository.delete(existingWorkoutPlanExercise);
    }
}