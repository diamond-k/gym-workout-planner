package com.diamond.workoutplanner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreetingRepository extends JpaRepository<Greeting, Integer> {
    // Spring Boot automatically implements this at runtime!
}