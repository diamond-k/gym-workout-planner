package com.diamond.workoutplanner.workoutplan;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workout-plans")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping
    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanService.getAllWorkoutPlans();
    }

    @GetMapping("/{id}")
    public WorkoutPlan getWorkoutPlanById(@PathVariable int id) {
        return workoutPlanService.getWorkoutPlanById(id);
    }

    @PostMapping
    public WorkoutPlan createWorkoutPlan(@Valid @RequestBody WorkoutPlan workoutPlan) {
        return workoutPlanService.createWorkoutPlan(workoutPlan);
    }

    @PutMapping("/{id}")
    public WorkoutPlan updateWorkoutPlan(@PathVariable int id, @Valid @RequestBody WorkoutPlan workoutPlan) {
        return workoutPlanService.updateWorkoutPlan(id, workoutPlan);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkoutPlan(@PathVariable int id) {
        workoutPlanService.deleteWorkoutPlan(id);
    }
}