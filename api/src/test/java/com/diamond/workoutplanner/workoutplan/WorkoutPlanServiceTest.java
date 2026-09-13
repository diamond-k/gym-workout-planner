package com.diamond.workoutplanner.workoutplan;

import com.diamond.workoutplanner.exercise.Exercise;
import  com.diamond.workoutplanner.exercise.ExerciseRepository;
import com.diamond.workoutplanner.workoutplanexercise.WorkoutPlanExercise;
import com.diamond.workoutplanner.workoutplanexercise.dto.CreateWorkoutPlanExerciseRequest;
import com.diamond.workoutplanner.exception.DuplicateWorkoutPlanExerciseException;
import com.diamond.workoutplanner.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanServiceTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    @Test
    void createWorkoutPlan_createsWorkoutWithExercisesInCorrectOrder() {
        Exercise benchPress = mock(Exercise.class);
        Exercise shoulderPress = mock(Exercise.class);

        when(exerciseRepository.findById(1))
                .thenReturn(Optional.of(benchPress));

        when(exerciseRepository.findById(3))
                .thenReturn(Optional.of(shoulderPress));

        when(workoutPlanRepository.save(any(WorkoutPlan.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<CreateWorkoutPlanExerciseRequest> exercises = List.of(
                new CreateWorkoutPlanExerciseRequest(1, 3, 10),
                new CreateWorkoutPlanExerciseRequest(3, 4, 8)
        );

        WorkoutPlan result = workoutPlanService.createWorkoutPlan(
                "Upper Body Workout",
                "Chest and shoulders",
                exercises
        );

        assertEquals("Upper Body Workout", result.getName());
        assertEquals("Chest and shoulders", result.getDescription());
        assertEquals(2, result.getExercises().size());

        WorkoutPlanExercise firstExercise = result.getExercises().get(0);
        assertEquals(benchPress, firstExercise.getExercise());
        assertEquals(3, firstExercise.getTargetSets());
        assertEquals(10, firstExercise.getTargetReps());
        assertEquals(0, firstExercise.getPosition());

        WorkoutPlanExercise secondExercise = result.getExercises().get(1);
        assertEquals(shoulderPress, secondExercise.getExercise());
        assertEquals(4, secondExercise.getTargetSets());
        assertEquals(8, secondExercise.getTargetReps());
        assertEquals(1, secondExercise.getPosition());

        verify(workoutPlanRepository).save(result);
    }

    @Test
    void createWorkoutPlan_throwsWhenDuplicateExerciseProvided() {
        Exercise benchPress = mock(Exercise.class);

        when(exerciseRepository.findById(1))
                .thenReturn(Optional.of(benchPress));

        List<CreateWorkoutPlanExerciseRequest> exercises = List.of(
                new CreateWorkoutPlanExerciseRequest(1, 3, 10),
                new CreateWorkoutPlanExerciseRequest(1, 4, 8)
        );

        assertThrows(
                DuplicateWorkoutPlanExerciseException.class,
                () -> workoutPlanService.createWorkoutPlan(
                        "Upper Body Workout",
                        "Chest workout",
                        exercises
                )
        );

        verify(exerciseRepository).findById(1);
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void createWorkoutPlan_throwsWhenExerciseDoesNotExist() {
        when(exerciseRepository.findById(99))
                .thenReturn(Optional.empty());

        List<CreateWorkoutPlanExerciseRequest> exercises = List.of(
                new CreateWorkoutPlanExerciseRequest(99, 3, 10)
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> workoutPlanService.createWorkoutPlan(
                        "Test Workout",
                        "Invalid exercise test",
                        exercises
                )
        );

        verify(exerciseRepository).findById(99);
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void updateWorkoutPlan_removesExerciseNotInRequest() {
        WorkoutPlan workoutPlan = new WorkoutPlan(
                "Lower Body Workout",
                "Leg workout"
        );

        Exercise squat = mock(Exercise.class);
        Exercise lunge = mock(Exercise.class);
        Exercise hipThrust = mock(Exercise.class);

        when(squat.getId()).thenReturn(1);
        when(lunge.getId()).thenReturn(2);
        when(hipThrust.getId()).thenReturn(3);

        workoutPlan.addExercise(
                new WorkoutPlanExercise(workoutPlan, squat, 3, 10, 0)
        );
        workoutPlan.addExercise(
                new WorkoutPlanExercise(workoutPlan, lunge, 3, 12, 1)
        );
        workoutPlan.addExercise(
                new WorkoutPlanExercise(workoutPlan, hipThrust, 4, 10, 2)
        );

        when(workoutPlanRepository.findById(1))
                .thenReturn(Optional.of(workoutPlan));

        when(workoutPlanRepository.save(any(WorkoutPlan.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<CreateWorkoutPlanExerciseRequest> exercises = List.of(
                new CreateWorkoutPlanExerciseRequest(1, 3, 10),
                new CreateWorkoutPlanExerciseRequest(3, 4, 10)
        );

        WorkoutPlan result = workoutPlanService.updateWorkoutPlan(
                1,
                "Updated Lower Body Workout",
                "Updated leg workout",
                exercises
        );

        assertEquals(2, result.getExercises().size());

        assertEquals(squat, result.getExercises().get(0).getExercise());
        assertEquals(hipThrust, result.getExercises().get(1).getExercise());

        verify(workoutPlanRepository).save(workoutPlan);
    }

    @Test
    void updateWorkoutPlan_addsNewExercise() {
        WorkoutPlan workoutPlan = new WorkoutPlan(
                "Upper Body Workout",
                "Chest workout"
        );

        Exercise benchPress = mock(Exercise.class);
        Exercise shoulderPress = mock(Exercise.class);

        when(benchPress.getId()).thenReturn(1);
        when(shoulderPress.getId()).thenReturn(3);

        workoutPlan.addExercise(
                new WorkoutPlanExercise(workoutPlan, benchPress, 3, 10, 0)
        );

        when(workoutPlanRepository.findById(1))
                .thenReturn(Optional.of(workoutPlan));

        when(exerciseRepository.findById(3))
                .thenReturn(Optional.of(shoulderPress));

        when(workoutPlanRepository.save(any(WorkoutPlan.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<CreateWorkoutPlanExerciseRequest> exercises = List.of(
                new CreateWorkoutPlanExerciseRequest(1, 3, 10),
                new CreateWorkoutPlanExerciseRequest(3, 4, 8)
        );

        WorkoutPlan result = workoutPlanService.updateWorkoutPlan(
                1,
                "Updated Upper Body Workout",
                "Chest and shoulders",
                exercises
        );

        assertEquals(2, result.getExercises().size());

        WorkoutPlanExercise firstExercise = result.getExercises().get(0);
        assertEquals(benchPress, firstExercise.getExercise());
        assertEquals(0, firstExercise.getPosition());

        WorkoutPlanExercise secondExercise = result.getExercises().get(1);
        assertEquals(shoulderPress, secondExercise.getExercise());
        assertEquals(4, secondExercise.getTargetSets());
        assertEquals(8, secondExercise.getTargetReps());
        assertEquals(1, secondExercise.getPosition());

        verify(exerciseRepository).findById(3);
        verify(workoutPlanRepository).save(workoutPlan);
    }

    @Test
    void updateWorkoutPlan_updatesTargetsWithoutReplacingExistingExercise() {
        WorkoutPlan workoutPlan = new WorkoutPlan(
                "Upper Body Workout",
                "Chest workout"
        );

        Exercise benchPress = mock(Exercise.class);
        when(benchPress.getId()).thenReturn(1);

        WorkoutPlanExercise existingWorkoutExercise =
                new WorkoutPlanExercise(
                        workoutPlan,
                        benchPress,
                        3,
                        10,
                        0
                );

        workoutPlan.addExercise(existingWorkoutExercise);

        when(workoutPlanRepository.findById(1))
                .thenReturn(Optional.of(workoutPlan));

        when(workoutPlanRepository.save(any(WorkoutPlan.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<CreateWorkoutPlanExerciseRequest> exercises = List.of(
                new CreateWorkoutPlanExerciseRequest(1, 4, 8)
        );

        WorkoutPlan result = workoutPlanService.updateWorkoutPlan(
                1,
                "Upper Body Workout",
                "Chest workout",
                exercises
        );

        WorkoutPlanExercise updatedExercise = result.getExercises().get(0);

        assertSame(existingWorkoutExercise, updatedExercise);
        assertEquals(4, updatedExercise.getTargetSets());
        assertEquals(8, updatedExercise.getTargetReps());
        assertEquals(0, updatedExercise.getPosition());

        verify(workoutPlanRepository).save(workoutPlan);
    }

    @Test
    void updateWorkoutPlan_updatesExercisePositionsWhenReordered() {
        WorkoutPlan workoutPlan = new WorkoutPlan(
                "Upper Body Workout",
                "Mixed upper body"
        );

        Exercise benchPress = mock(Exercise.class);
        Exercise shoulderPress = mock(Exercise.class);
        Exercise latPulldown = mock(Exercise.class);

        when(benchPress.getId()).thenReturn(1);
        when(shoulderPress.getId()).thenReturn(3);
        when(latPulldown.getId()).thenReturn(2);

        WorkoutPlanExercise bench =
                new WorkoutPlanExercise(workoutPlan, benchPress, 3, 10, 0);

        WorkoutPlanExercise shoulder =
                new WorkoutPlanExercise(workoutPlan, shoulderPress, 3, 10, 1);

        WorkoutPlanExercise lat =
                new WorkoutPlanExercise(workoutPlan, latPulldown, 3, 10, 2);

        workoutPlan.addExercise(bench);
        workoutPlan.addExercise(shoulder);
        workoutPlan.addExercise(lat);

        when(workoutPlanRepository.findById(1))
                .thenReturn(Optional.of(workoutPlan));

        when(workoutPlanRepository.save(any(WorkoutPlan.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<CreateWorkoutPlanExerciseRequest> exercises = List.of(
                new CreateWorkoutPlanExerciseRequest(2, 3, 10),
                new CreateWorkoutPlanExerciseRequest(1, 3, 10),
                new CreateWorkoutPlanExerciseRequest(3, 3, 10)
        );

        WorkoutPlan result = workoutPlanService.updateWorkoutPlan(
                1,
                "Upper Body Workout",
                "Mixed upper body",
                exercises
        );

        assertSame(workoutPlan, result);
        assertEquals(1, bench.getPosition());
        assertEquals(2, shoulder.getPosition());
        assertEquals(0, lat.getPosition());

        verify(workoutPlanRepository).save(workoutPlan);
    }

    @Test
    void updateWorkoutPlan_throwsWhenWorkoutDoesNotExist() {
        when(workoutPlanRepository.findById(99))
                .thenReturn(Optional.empty());

        List<CreateWorkoutPlanExerciseRequest> exercises = List.of(
                new CreateWorkoutPlanExerciseRequest(1, 3, 10)
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> workoutPlanService.updateWorkoutPlan(
                        99,
                        "Missing Workout",
                        "Should not update",
                        exercises
                )
        );

        verify(workoutPlanRepository).findById(99);
        verify(workoutPlanRepository, never()).save(any(WorkoutPlan.class));
    }

    @Test
    void deleteWorkoutPlan_deletesExistingWorkout() {
        WorkoutPlan workoutPlan = new WorkoutPlan(
                "Workout to Delete",
                "Temporary workout"
        );

        when(workoutPlanRepository.findById(1))
                .thenReturn(Optional.of(workoutPlan));

        workoutPlanService.deleteWorkoutPlan(1);

        verify(workoutPlanRepository).findById(1);
        verify(workoutPlanRepository).delete(workoutPlan);
    }

    @Test
    void deleteWorkoutPlan_throwsWhenWorkoutDoesNotExist() {
        when(workoutPlanRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> workoutPlanService.deleteWorkoutPlan(99)
        );

        verify(workoutPlanRepository).findById(99);
        verify(workoutPlanRepository, never()).delete(any(WorkoutPlan.class));
    }
}