import type { MuscleGroup } from './MuscleGroup';

export interface WorkoutPlanExerciseResponse {
  id: number;
  exerciseId: number;
  exerciseName: string;
  muscleGroup: MuscleGroup;
  instructions: string;
  targetSets: number;
  targetReps: number;
}