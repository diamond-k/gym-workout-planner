package com.diamond.workoutplanner.workoutplanexercise;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diamond.workoutplanner.workoutplanexercise.dto.WorkoutPlanExerciseResponse;

@RestController
@RequestMapping("/api/workout-plans/{workoutPlanId}/workout-plan-exercises")
public class WorkoutPlanExerciseController {

    private final WorkoutPlanExerciseService workoutPlanExerciseService;

    public WorkoutPlanExerciseController(WorkoutPlanExerciseService workoutPlanExerciseService) {
        this.workoutPlanExerciseService = workoutPlanExerciseService;
    }

    @GetMapping
    public List<WorkoutPlanExerciseResponse> getWorkoutPlanExercisesByWorkoutPlanId(@PathVariable int workoutPlanId) {

        return workoutPlanExerciseService
                .getWorkoutPlanExercisesByWorkoutPlanId(workoutPlanId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private WorkoutPlanExerciseResponse mapToResponse(WorkoutPlanExercise workoutPlanExercise) {
        return new WorkoutPlanExerciseResponse(
                workoutPlanExercise.getId(),
                workoutPlanExercise.getExercise().getId(),
                workoutPlanExercise.getExercise().getName(),
                workoutPlanExercise.getExercise().getMuscleGroup(),
                workoutPlanExercise.getExercise().getInstructions(),
                workoutPlanExercise.getTargetSets(),
                workoutPlanExercise.getTargetReps()
        );
    }
}