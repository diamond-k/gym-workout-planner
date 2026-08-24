package com.diamond.workoutplanner.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.diamond.workoutplanner.entity.Exercise;
import com.diamond.workoutplanner.entity.PlannedExercise;
import com.diamond.workoutplanner.entity.WorkoutPlan;
import com.diamond.workoutplanner.repository.ExerciseRepository;
import com.diamond.workoutplanner.repository.PlannedExerciseRepository;
import com.diamond.workoutplanner.repository.WorkoutPlanRepository;

/*
PlannedExerciseRepository
- create/update/delete the PlannedExercise

WorkoutPlanRepository
- finds the workout plan it belongs to

ExerciseRepository 
- finds the catalogue exercise being added
*/

@Service
public class PlannedExerciseService {

    private final PlannedExerciseRepository plannedExerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;

    public PlannedExerciseService(PlannedExerciseRepository plannedExerciseRepository,
            WorkoutPlanRepository workoutPlanRepository, ExerciseRepository exerciseRepository) {
        this.plannedExerciseRepository = plannedExerciseRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
    }

    private PlannedExercise getPlannedExerciseByIdAndWorkoutPlanId(int plannedExerciseId, int workoutPlanId) {
        return plannedExerciseRepository
                .findByIdAndWorkoutPlanId(plannedExerciseId, workoutPlanId)
                .orElseThrow();
    }

    public List<PlannedExercise> getPlannedExercisesByWorkoutPlanId(int workoutPlanId) {
        return plannedExerciseRepository.findByWorkoutPlanId(workoutPlanId);
    }

    public PlannedExercise createPlannedExercise(int workoutPlanId, int exerciseId, int targetSets, int targetReps) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId).orElseThrow();
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow();
        PlannedExercise plannedExercise = new PlannedExercise(workoutPlan, exercise, targetSets, targetReps);
        return plannedExerciseRepository.save(plannedExercise);
    }

    public PlannedExercise updatePlannedExercise(int workoutPlanId, int plannedExerciseId,
            PlannedExercise updatedPlannedExercise) {
        PlannedExercise existingPlannedExercise = getPlannedExerciseByIdAndWorkoutPlanId(plannedExerciseId,
                workoutPlanId);
        existingPlannedExercise.setTargetReps(updatedPlannedExercise.getTargetReps());
        existingPlannedExercise.setTargetSets(updatedPlannedExercise.getTargetSets());
        return plannedExerciseRepository.save(existingPlannedExercise);
    }

    public void deletePlannedExercise(int workoutPlanId, int plannedExerciseId) {
        PlannedExercise existingPlannedExercise = getPlannedExerciseByIdAndWorkoutPlanId(plannedExerciseId,
                workoutPlanId);
        plannedExerciseRepository.delete(existingPlannedExercise);
    }
}