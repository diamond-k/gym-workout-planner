package com.diamond.workoutplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity // marks class as a JPA entity that will be stored in the database
@Table(name = "exercises") // maps this class to the 'exercises' table
public class Exercise {

    @Id // marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // lets MySQL automatically generate the ID
    private int id;

    @Column(nullable = false) // name is required and cannot be null
    private String name;

    @Enumerated(EnumType.STRING) // stores the enum value as text, e.g. "CHEST"
    @Column(name = "muscle_group", nullable = false) // maps to 'muscle_group' and makes it required
    private MuscleGroup muscleGroup;

    @Column(length = 1000, nullable = false) // allows up to 1000 characters and makes instructions required
    private String instructions;

    // JPA requires a default, no-argument constructor
    public Exercise() {
    }

    // constructor used when creating a new exercise
    public Exercise(String name, MuscleGroup muscleGroup, String instructions) {
        this.name = name;
        this.muscleGroup = muscleGroup;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}