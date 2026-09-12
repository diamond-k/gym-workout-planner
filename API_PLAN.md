# Workout Planner API Plan

## EXERCISES

- **GET /api/exercises**  
  → get all exercises

- **GET /api/exercises?muscleGroup=BACK**  
  → get exercises by muscle group

## WORKOUTS

- **GET /api/workout-plans**  
  → get all workouts

- **GET /api/workout-plans/{id}**  
  → get a workout

- **POST /api/workout-plans**  
  → create a workout with selected exercises and target sets/reps

- **PUT /api/workout-plans/{id}**  
  → update a workout, including changing its details, adding or removing exercises, and updating sets/reps

- **DELETE /api/workout-plans/{id}**  
  → delete a workout and its saved exercise relationships

## WORKOUT EXERCISES

- **GET /api/workout-plans/{workoutPlanId}/workout-plan-exercises**  
  → get the exercises saved in a workout, in their saved order