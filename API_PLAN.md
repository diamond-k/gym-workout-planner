# Workout Planner API Plan

## EXERCISES

- **GET /api/exercises**  
  → get the exercise catalogue

- **GET /api/exercises?muscleGroup=BACK**  
  → filter exercises by muscle group

## WORKOUT PLANS

- **GET /api/workout-plans**  
  → get all workout plans

- **POST /api/workout-plans**  
  → create a workout plan with its selected exercises and target sets/reps

- **GET /api/workout-plans/{id}**  
  → get a workout plan

- **PUT /api/workout-plans/{id}**  
  → update a workout plan, including adding, changing, or removing exercises

- **DELETE /api/workout-plans/{id}**  
  → delete a workout plan and its exercises

## WORKOUT PLAN EXERCISES

- **GET /api/workout-plans/{workoutPlanId}/workout-plan-exercises**  
  → get the exercises currently inside one workout plan

- **POST /api/workout-plans/{workoutPlanId}/workout-plan-exercises**  
  → add one exercise to an existing workout plan

- **PUT /api/workout-plans/{workoutPlanId}/workout-plan-exercises/{workoutPlanExerciseId}**  
  → update the target sets/reps for one exercise

- **DELETE /api/workout-plans/{workoutPlanId}/workout-plan-exercises/{workoutPlanExerciseId}**  
  → remove one exercise from the plan