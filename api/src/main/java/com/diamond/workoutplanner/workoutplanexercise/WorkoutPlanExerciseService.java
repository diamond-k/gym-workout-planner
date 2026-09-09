package com.diamond.workoutplanner.workoutplanexercise;

import java.util.List;

import org.springframework.stereotype.Service;

import com.diamond.workoutplanner.exception.DuplicateWorkoutPlanExerciseException;
import com.diamond.workoutplanner.exception.ResourceNotFoundException;
import com.diamond.workoutplanner.exercise.Exercise;
import com.diamond.workoutplanner.exercise.ExerciseRepository;
import com.diamond.workoutplanner.workoutplan.WorkoutPlan;
import com.diamond.workoutplanner.workoutplan.WorkoutPlanRepository;

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

    public WorkoutPlanExerciseService(
            WorkoutPlanExerciseRepository workoutPlanExerciseRepository,
            WorkoutPlanRepository workoutPlanRepository,
            ExerciseRepository exerciseRepository) {
        this.workoutPlanExerciseRepository = workoutPlanExerciseRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
    }

    // get a WorkoutPlanExercise by its ID and associated WorkoutPlan ID
    private WorkoutPlanExercise getWorkoutPlanExerciseByIdAndWorkoutPlanId(
            int workoutPlanExerciseId,
            int workoutPlanId) {

        return workoutPlanExerciseRepository
                .findByIdAndWorkoutPlanId(workoutPlanExerciseId, workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan exercise not found with id: "
                                + workoutPlanExerciseId
                                + " and Workout plan id: "
                                + workoutPlanId));
    }

    // get all WorkoutPlanExercises associated with a specific WorkoutPlan
    public List<WorkoutPlanExercise> getWorkoutPlanExercisesByWorkoutPlanId(
            int workoutPlanId) {

        workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan not found with id: " + workoutPlanId));

        return workoutPlanExerciseRepository.findByWorkoutPlanId(workoutPlanId);
    }

    // create a new WorkoutPlanExercise
    public WorkoutPlanExercise createWorkoutPlanExercise(
            int workoutPlanId,
            int exerciseId,
            int targetSets,
            int targetReps) {

        if (workoutPlanExerciseRepository
                .existsByWorkoutPlan_IdAndExercise_Id(workoutPlanId, exerciseId)) {
            throw new DuplicateWorkoutPlanExerciseException(
                    "Exercise is already in this workout plan");
        }

        WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout plan not found with id: " + workoutPlanId));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise not found with id: " + exerciseId));

        WorkoutPlanExercise workoutPlanExercise =
                new WorkoutPlanExercise(workoutPlan, exercise, targetSets, targetReps);

        return workoutPlanExerciseRepository.save(workoutPlanExercise);
    }

    // update an existing WorkoutPlanExercise
    public WorkoutPlanExercise updateWorkoutPlanExercise(
            int workoutPlanId,
            int workoutPlanExerciseId,
            int targetSets,
            int targetReps) {

        WorkoutPlanExercise existingWorkoutPlanExercise =
                getWorkoutPlanExerciseByIdAndWorkoutPlanId(
                        workoutPlanExerciseId,
                        workoutPlanId);

        existingWorkoutPlanExercise.setTargetSets(targetSets);
        existingWorkoutPlanExercise.setTargetReps(targetReps);

        return workoutPlanExerciseRepository.save(existingWorkoutPlanExercise);
    }

    // delete a WorkoutPlanExercise
    public void deleteWorkoutPlanExercise(
            int workoutPlanId,
            int workoutPlanExerciseId) {

        WorkoutPlanExercise existingWorkoutPlanExercise =
                getWorkoutPlanExerciseByIdAndWorkoutPlanId(
                        workoutPlanExerciseId,
                        workoutPlanId);

        workoutPlanExerciseRepository.delete(existingWorkoutPlanExercise);
    }
}