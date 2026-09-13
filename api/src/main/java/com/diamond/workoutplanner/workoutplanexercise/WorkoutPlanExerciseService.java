package com.diamond.workoutplanner.workoutplanexercise;

import java.util.List;

import org.springframework.stereotype.Service;
import com.diamond.workoutplanner.exception.ResourceNotFoundException;
import com.diamond.workoutplanner.exercise.ExerciseRepository;
import com.diamond.workoutplanner.workoutplan.WorkoutPlanRepository;

/*
WorkoutPlanExerciseRepository
- create/update/delete the WorkoutPlanExercise

WorkoutPlanRepository
- finds the workout it belongs to
*/

@Service
public class WorkoutPlanExerciseService {

    private final WorkoutPlanExerciseRepository workoutPlanExerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;

    public WorkoutPlanExerciseService(
            WorkoutPlanExerciseRepository workoutPlanExerciseRepository,
            WorkoutPlanRepository workoutPlanRepository,
            ExerciseRepository exerciseRepository) {
        this.workoutPlanExerciseRepository = workoutPlanExerciseRepository;
        this.workoutPlanRepository = workoutPlanRepository;
    }

    // get all WorkoutPlanExercises associated with a specific WorkoutPlan
    public List<WorkoutPlanExercise> getWorkoutPlanExercisesByWorkoutPlanId(
            int workoutPlanId) {

        workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found with id: " + workoutPlanId));

        return workoutPlanExerciseRepository.findByWorkoutPlanIdOrderByPositionAsc(workoutPlanId);
    }
}