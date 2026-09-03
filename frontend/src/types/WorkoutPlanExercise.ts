import type { MuscleGroup } from './MuscleGroup';

export interface WorkoutPlanExercise {
  id: number;
  exerciseId: number;
  exerciseName: string;
  muscleGroup: MuscleGroup;
  instructions: string;
  targetSets: number;
  targetReps: number;
}