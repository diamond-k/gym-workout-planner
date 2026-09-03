package com.diamond.workoutplanner.workoutplanexercise;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diamond.workoutplanner.workoutplanexercise.dto.WorkoutPlanExerciseResponse;
import com.diamond.workoutplanner.workoutplanexercise.dto.CreateWorkoutPlanExerciseRequest;
import com.diamond.workoutplanner.workoutplanexercise.dto.UpdateWorkoutPlanExerciseRequest;

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

    @PostMapping
    public WorkoutPlanExerciseResponse createWorkoutPlanExercise(@PathVariable int workoutPlanId, @Valid @RequestBody CreateWorkoutPlanExerciseRequest request) {
        WorkoutPlanExercise createdWorkoutPlanExercise =
            workoutPlanExerciseService.createWorkoutPlanExercise(
                workoutPlanId,
                request.exerciseId(),
                request.targetSets(),
                request.targetReps());

        return mapToResponse(createdWorkoutPlanExercise);
    }

    @PutMapping("/{workoutPlanExerciseId}")
    public WorkoutPlanExerciseResponse updateWorkoutPlanExercise(
        @PathVariable int workoutPlanId,
        @PathVariable int workoutPlanExerciseId,
        @Valid @RequestBody UpdateWorkoutPlanExerciseRequest request) {

        WorkoutPlanExercise updatedWorkoutPlanExercise =
                workoutPlanExerciseService.updateWorkoutPlanExercise(
                        workoutPlanId,
                        workoutPlanExerciseId,
                        request.targetSets(),
                        request.targetReps());

        return mapToResponse(updatedWorkoutPlanExercise);
    }

    @DeleteMapping("/{workoutPlanExerciseId}")
    public void deleteWorkoutPlanExercise(
            @PathVariable int workoutPlanId,
            @PathVariable int workoutPlanExerciseId) {

        workoutPlanExerciseService.deleteWorkoutPlanExercise(
                workoutPlanId,
                workoutPlanExerciseId);
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