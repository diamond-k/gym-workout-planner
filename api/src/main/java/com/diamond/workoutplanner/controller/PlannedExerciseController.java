package com.diamond.workoutplanner.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.diamond.workoutplanner.entity.PlannedExercise;
import com.diamond.workoutplanner.service.PlannedExerciseService;

@RestController
@RequestMapping("/api/workout-plans/{workoutPlanId}/planned-exercises")
public class PlannedExerciseController {

    private final PlannedExerciseService plannedExerciseService;

    public PlannedExerciseController(PlannedExerciseService plannedExerciseService) {
        this.plannedExerciseService = plannedExerciseService;
    }

    @GetMapping
    public List<PlannedExercise> getPlannedExercisesByWorkoutPlanId(@PathVariable int workoutPlanId) {
        return plannedExerciseService.getPlannedExercisesByWorkoutPlanId(workoutPlanId);
    }

    public record CreatePlannedExerciseRequest(int exerciseId, int targetSets, int targetReps) {
    }

    @PostMapping
    public PlannedExercise createPlannedExercise(
            @PathVariable int workoutPlanId,
            @RequestBody CreatePlannedExerciseRequest request) {

        return plannedExerciseService.createPlannedExercise(
                workoutPlanId,
                request.exerciseId(),
                request.targetSets(),
                request.targetReps());
    }

    @PutMapping("/{plannedExerciseId}")
    public PlannedExercise updatePlannedExercise(
            @PathVariable int workoutPlanId,
            @PathVariable int plannedExerciseId,
            @RequestBody PlannedExercise updatedPlannedExercise) {

        return plannedExerciseService.updatePlannedExercise(
                workoutPlanId,
                plannedExerciseId,
                updatedPlannedExercise);
    }

    @DeleteMapping("/{plannedExerciseId}")
    public void deletePlannedExercise(
            @PathVariable int workoutPlanId,
            @PathVariable int plannedExerciseId) {

        plannedExerciseService.deletePlannedExercise(
                workoutPlanId,
                plannedExerciseId);
    }
}