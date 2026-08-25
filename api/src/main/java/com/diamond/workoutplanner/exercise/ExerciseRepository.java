package com.diamond.workoutplanner.exercise;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

public interface ExerciseRepository extends ListCrudRepository<Exercise, Integer> {
    // derived query: Spring Data JPA creates the query from the method name
    List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup);
}