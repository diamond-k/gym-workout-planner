import type { Exercise } from './Exercise';
import type { WorkoutPlan } from './WorkoutPlan';

export interface WorkoutPlanExercise {
  id: number;
  exercise: Exercise;
  workoutPlan: WorkoutPlan;
  targetSets: number;
  targetReps: number;
}