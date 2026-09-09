import type { Exercise } from '../types/Exercise';
import type { WorkoutPlanResponse } from '../types/WorkoutPlanResponse';
import type { MuscleGroup } from '../types/MuscleGroup';
import type { WorkoutPlanExerciseResponse } from '../types/WorkoutPlanExerciseResponse';

export type WorkoutPlanRequest = {
  name: string;
  description: string | null;
  exercises: WorkoutPlanExerciseRequest[];
};

export type WorkoutPlanExerciseRequest = {
  exerciseId: number;
  targetSets: number;
  targetReps: number;
};

export interface Api {
  getExercises(muscleGroup?: MuscleGroup): Promise<Exercise[]>;

  getWorkoutPlans(): Promise<WorkoutPlanResponse[]>;
  getWorkoutPlan(id: number): Promise<WorkoutPlanResponse>;
  createWorkoutPlan(input: WorkoutPlanRequest): Promise<WorkoutPlanResponse>;
  updateWorkoutPlan(id: number, input: WorkoutPlanRequest): Promise<WorkoutPlanResponse>;
  deleteWorkoutPlan(id: number): Promise<void>;
  
  getWorkoutPlanExercises(workoutPlanId: number): Promise<WorkoutPlanExerciseResponse[]>;
}

function ensureOk(response: Response, doing: string): void {
  if (!response.ok) {
    throw new Error(
      `Failed to ${doing}: ${response.status} ${response.statusText}`,
    );
  }
}

export const api: Api = {
  
  // #region Exercises
  async getExercises(muscleGroup) {
    const url = muscleGroup
      ? `/api/exercises?muscleGroup=${muscleGroup}`
      : '/api/exercises';

    const response = await fetch(url);
    ensureOk(response, "get exercises");
    return (await response.json()) as Exercise[];
  },
  // #endregion

  // #region Workout Plans
  async getWorkoutPlans() {
    const response = await fetch('/api/workout-plans');
    ensureOk(response, "get workout plans");
    return (await response.json()) as WorkoutPlanResponse[];
  },

  async getWorkoutPlan(id) {
    const response = await fetch(`/api/workout-plans/${id}`);
    ensureOk(response, `get workout plan ${id}`);
    return (await response.json()) as WorkoutPlanResponse;
  },

  async createWorkoutPlan(input){
    const response = await fetch('/api/workout-plans', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(input)
    });
    ensureOk(response, 'create workout plan');
    return (await response.json()) as WorkoutPlanResponse;
  },

  async updateWorkoutPlan(id, input) {
    const response = await fetch(`/api/workout-plans/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(input)
    });
    ensureOk(response, `update workout plan ${id}`);
    return (await response.json()) as WorkoutPlanResponse;
  },

  async deleteWorkoutPlan(id) {
    const response = await fetch(`/api/workout-plans/${id}`, {
      method: 'DELETE',
    });
    ensureOk(response, `delete workout plan ${id}`);
  },
  // #endregion

  // #region Workout Plan Exercises
  async getWorkoutPlanExercises(workoutPlanId) {
    const response = await fetch(
      `/api/workout-plans/${workoutPlanId}/workout-plan-exercises`
    );
    ensureOk(response, `get workout plan exercises for plan ${workoutPlanId}`);
    return (await response.json()) as WorkoutPlanExerciseResponse[];
  }
  // #endregion 
};