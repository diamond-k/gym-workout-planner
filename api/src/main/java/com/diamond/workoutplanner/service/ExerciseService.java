package com.diamond.workoutplanner.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.diamond.workoutplanner.entity.Exercise;
import com.diamond.workoutplanner.entity.MuscleGroup;
import com.diamond.workoutplanner.repository.ExerciseRepository;

@Service // create and manage an instance of this class as part of the service/business-logic layer.
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    // repository dependency is supplied through the constructor
    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    public Exercise getExerciseById(int id) {
        return exerciseRepository.findById(id).orElseThrow();
    }

    public List<Exercise> getByMuscleGroup(MuscleGroup muscleGroup) {
        return exerciseRepository.findByMuscleGroup(muscleGroup);
    }
}