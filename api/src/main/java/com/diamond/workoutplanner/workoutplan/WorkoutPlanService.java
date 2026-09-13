package com.diamond.workoutplanner.workoutplan;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.diamond.workoutplanner.exercise.Exercise;
import com.diamond.workoutplanner.workoutplanexercise.WorkoutPlanExercise;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diamond.workoutplanner.exception.DuplicateWorkoutPlanExerciseException;
import com.diamond.workoutplanner.exception.ResourceNotFoundException;
import com.diamond.workoutplanner.workoutplanexercise.dto.CreateWorkoutPlanExerciseRequest;
import com.diamond.workoutplanner.exercise.ExerciseRepository;

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final ExerciseRepository exerciseRepository;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository, ExerciseRepository exerciseRepository) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAllByOrderByUpdatedAtDesc();
    }

    public WorkoutPlan getWorkoutPlanById(int id) {
        return workoutPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found with id: " + id));
    }

    @Transactional
    public WorkoutPlan createWorkoutPlan(
            String name,
            String description,
            List<CreateWorkoutPlanExerciseRequest> exercises) {

        WorkoutPlan workoutPlan = new WorkoutPlan(name, description);
        Set<Integer> exerciseIds = new HashSet<>();

        for (int position = 0; position < exercises.size(); position++) {
            
            CreateWorkoutPlanExerciseRequest requestExercise = exercises.get(position);
            
            if (!exerciseIds.add(requestExercise.exerciseId())) {
                throw new DuplicateWorkoutPlanExerciseException(
                        "Exercise is already in this workout"
                );
            }

            Exercise exercise = exerciseRepository
            .findById(requestExercise.exerciseId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Exercise not found with id: " + requestExercise.exerciseId()));

            WorkoutPlanExercise workoutPlanExercise = new WorkoutPlanExercise(
                    workoutPlan,
                    exercise,
                    requestExercise.targetSets(),
                    requestExercise.targetReps(), 
                    position);

            workoutPlan.addExercise(workoutPlanExercise);
        }

        return workoutPlanRepository.save(workoutPlan);
    }

    @Transactional
    public WorkoutPlan updateWorkoutPlan(
            int id,
            String name,
            String description,
            List<CreateWorkoutPlanExerciseRequest> exercises) {

        WorkoutPlan workoutPlan = getWorkoutPlanById(id);

        workoutPlan.setName(name);
        workoutPlan.setDescription(description);

        // store the exercises already in the workout, using exerciseId as the key
        Map<Integer, WorkoutPlanExercise> existingExercises = new HashMap<>();

        for (WorkoutPlanExercise workoutPlanExercise : workoutPlan.getExercises()) {
            existingExercises.put(
                    workoutPlanExercise.getExercise().getId(),
                    workoutPlanExercise
            );
        }

        // keep track of every exercise included in the edited plan
        Set<Integer> incomingExerciseIds = new HashSet<>();

        for (int position = 0; position < exercises.size(); position++) {

            // get the exercise at the position in list
            CreateWorkoutPlanExerciseRequest requestExercise = exercises.get(position);
            int exerciseId = requestExercise.exerciseId();

            if (!incomingExerciseIds.add(exerciseId)) {
                throw new DuplicateWorkoutPlanExerciseException(
                        "Exercise is already in this workout"
                );
            }

            WorkoutPlanExercise existingExercise = existingExercises.get(exerciseId);

            if (existingExercise != null) {
                // already in the plan - update its targets and position
                existingExercise.setTargetSets(requestExercise.targetSets());
                existingExercise.setTargetReps(requestExercise.targetReps());
                existingExercise.setPosition(position);

            } else {
                // new exercise - add it to the plan
                Exercise exercise = exerciseRepository.findById(exerciseId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Exercise not found with id: " + exerciseId));

                WorkoutPlanExercise newWorkoutPlanExercise =
                        new WorkoutPlanExercise(
                                workoutPlan,
                                exercise,
                                requestExercise.targetSets(),
                                requestExercise.targetReps(),
                                position
                        );

                workoutPlan.addExercise(newWorkoutPlanExercise);
            }
        }

        // remove exercises that are no longer in the edited plan
        workoutPlan.getExercises().removeIf(
                workoutPlanExercise ->
                        !incomingExerciseIds.contains(
                                workoutPlanExercise.getExercise().getId()
                        )
        );

        workoutPlan.setUpdatedAt();

        return workoutPlanRepository.save(workoutPlan);
    }

    @Transactional
    public void deleteWorkoutPlan(int id) {
        WorkoutPlan existingWorkoutPlan = getWorkoutPlanById(id);
        workoutPlanRepository.delete(existingWorkoutPlan);
    }
}