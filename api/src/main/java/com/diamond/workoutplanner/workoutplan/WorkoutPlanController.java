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
import com.diamond.workoutplanner.workoutplan.dto.CreateWorkoutPlanRequest;
import com.diamond.workoutplanner.workoutplan.dto.UpdateWorkoutPlanRequest;
import com.diamond.workoutplanner.workoutplan.dto.WorkoutPlanResponse;

@RestController
@RequestMapping("/api/workout-plans")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    public WorkoutPlanController(WorkoutPlanService workoutPlanService) {
        this.workoutPlanService = workoutPlanService;
    }

    @GetMapping
    public List<WorkoutPlanResponse> getAllWorkoutPlans() {
        return workoutPlanService.getAllWorkoutPlans()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public WorkoutPlanResponse getWorkoutPlanById(@PathVariable int id) {
        WorkoutPlan workoutPlan = workoutPlanService.getWorkoutPlanById(id);
        return mapToResponse(workoutPlan);
    }

    @PostMapping
    public WorkoutPlanResponse createWorkoutPlan(@Valid @RequestBody CreateWorkoutPlanRequest request) {
        
        WorkoutPlan createdWorkoutPlan = workoutPlanService.createWorkoutPlan(
                request.name(),
                request.description(),
                request.exercises()
        );
        return mapToResponse(createdWorkoutPlan);
    }

    @PutMapping("/{id}")
    public WorkoutPlanResponse updateWorkoutPlan(@PathVariable int id, @Valid @RequestBody UpdateWorkoutPlanRequest request) {
       WorkoutPlan updatedWorkoutPlan = workoutPlanService.updateWorkoutPlan(
        id,
        request.name(),
        request.description(),
        request.exercises());

        return mapToResponse(updatedWorkoutPlan);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkoutPlan(@PathVariable int id) {
        workoutPlanService.deleteWorkoutPlan(id);
    }

    private WorkoutPlanResponse mapToResponse(WorkoutPlan workoutPlan) {
        return new WorkoutPlanResponse(
                workoutPlan.getId(),
                workoutPlan.getName(),
                workoutPlan.getDescription(),
                workoutPlan.getCreatedAt(),
                workoutPlan.getUpdatedAt()
        );
    }
}