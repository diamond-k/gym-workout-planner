package com.diamond.workoutplanner.repository;

import java.util.List;
import com.diamond.workoutplanner.entity.Exercise;
import com.diamond.workoutplanner.entity.MuscleGroup;
import org.springframework.data.repository.ListCrudRepository;

public interface ExerciseRepository extends ListCrudRepository<Exercise, Integer> {
    // derived query: Spring Data JPA creates the query from the method name
    List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup);
}