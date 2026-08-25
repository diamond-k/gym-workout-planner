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
→ create a workout plan

- **GET /api/workout-plans/{id}**
→ get one workout plan

- **PUT /api/workout-plans/{id}**
→ update a workout plan

- **DELETE /api/workout-plans/{id}**
→ delete a workout plan


## WORKOUT PLAN EXERCISES

- **GET /api/workout-plans/{workoutPlanId}/workout-plan-exercises**
→ get the exercises currently inside one workout plan

- **POST /api/workout-plans/{workoutPlanId}/workout-plan-exercises**
→ add an exercise to that workout plan with target sets/reps

- **PUT /api/workout-plans/{workoutPlanId}/workout-plan-exercises/{workoutPlanExerciseId}**
→ change target sets/reps

- **DELETE /api/workout-plans/{workoutPlanId}/workout-plan-exercises/{workoutPlanExerciseId}**
→ remove that exercise from the plan