package com.diamond.workoutplanner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity // marks this class as a JPA entity
@Table(name = "workout_plans") // maps this class to the 'workout_plans' table
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false) // name is required and cannot be null
    private String name; 
    
    @Column(length = 1000) // allows up to 1000 characters for the description
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public WorkoutPlan() {
    }

    public WorkoutPlan(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}
