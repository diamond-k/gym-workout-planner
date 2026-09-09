package com.diamond.workoutplanner.workoutplan;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.diamond.workoutplanner.exception.ResourceNotFoundException;
import com.diamond.workoutplanner.workoutplanexercise.WorkoutPlanExerciseRepository;
import com.diamond.workoutplanner.workoutplanexercise.WorkoutPlanExerciseService;
import com.diamond.workoutplanner.workoutplanexercise.dto.CreateWorkoutPlanExerciseRequest;

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
    private final WorkoutPlanExerciseService workoutPlanExerciseService;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepository,
            WorkoutPlanExerciseRepository workoutPlanExerciseRepository,
            WorkoutPlanExerciseService workoutPlanExerciseService) {
        this.workoutPlanRepository = workoutPlanRepository;
        this.workoutPlanExerciseRepository = workoutPlanExerciseRepository;
        this.workoutPlanExerciseService = workoutPlanExerciseService;
    }

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepository.findAll();
    }

    public WorkoutPlan getWorkoutPlanById(int id) {
        return workoutPlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Workout plan not found with id: " + id
            ));
    }

    @Transactional
    public WorkoutPlan createWorkoutPlan(
            String name,
            String description,
            List<CreateWorkoutPlanExerciseRequest> exercises) {

        // create and save the WorkoutPlan so it gets its database id
        WorkoutPlan workoutPlan = new WorkoutPlan(name, description);
        WorkoutPlan savedWorkoutPlan = workoutPlanRepository.save(workoutPlan);

        // then create each WorkoutPlanExercise using the new plan id
        for (CreateWorkoutPlanExerciseRequest exercise : exercises) {
            workoutPlanExerciseService.createWorkoutPlanExercise(
                    savedWorkoutPlan.getId(),
                    exercise.exerciseId(),
                    exercise.targetSets(),
                    exercise.targetReps()
            );
        }

        return savedWorkoutPlan;
    }

    public WorkoutPlan updateWorkoutPlan(int id, String name, String description) {
        WorkoutPlan existingWorkoutPlan = getWorkoutPlanById(id);
        existingWorkoutPlan.setName(name);
        existingWorkoutPlan.setDescription(description);
        return workoutPlanRepository.save(existingWorkoutPlan);
    }

    @Transactional
    public void deleteWorkoutPlan(int id) {
        WorkoutPlan existingWorkoutPlan = getWorkoutPlanById(id);
        workoutPlanExerciseRepository.deleteAll(workoutPlanExerciseRepository.findByWorkoutPlanId(id));
        workoutPlanRepository.delete(existingWorkoutPlan);
    }
}