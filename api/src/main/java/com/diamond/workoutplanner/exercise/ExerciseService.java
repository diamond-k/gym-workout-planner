package com.diamond.workoutplanner.exercise;

import java.util.List;
import org.springframework.stereotype.Service;

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