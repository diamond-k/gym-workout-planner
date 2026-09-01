import type { Exercise } from '../types/Exercise';
import type { WorkoutPlan } from '../types/WorkoutPlan';
import type { MuscleGroup } from '../types/MuscleGroup';
import type { WorkoutPlanExercise } from '../types/WorkoutPlanExercise';
/*
  WorkoutPlan              = data returned from backend
  WorkoutPlanInput         = data sent to create/update plan

  WorkoutPlanExercise      = data returned from backend
  WorkoutPlanExerciseInput = data sent when adding one
  WorkoutPlanExerciseUpdate = data sent when changing sets/reps
*/
export type WorkoutPlanInput = {
  name: string;
  description: string | null;
};

export type WorkoutPlanExerciseInput = {
  exerciseId: number;
  targetSets: number;
  targetReps: number;
};

export type WorkoutPlanExerciseUpdate = {
  targetSets: number;
  targetReps: number;
};

export interface Api {
  getExercises(muscleGroup?: MuscleGroup): Promise<Exercise[]>;

  getWorkoutPlans(): Promise<WorkoutPlan[]>;
  getWorkoutPlan(id: number): Promise<WorkoutPlan>;
  createWorkoutPlan(input: WorkoutPlanInput): Promise<WorkoutPlan>;
  updateWorkoutPlan(id: number, input: WorkoutPlanInput): Promise<WorkoutPlan>;
  deleteWorkoutPlan(id: number): Promise<void>;

  getWorkoutPlanExercises(workoutPlanId: number): Promise<WorkoutPlanExercise[]>;
  addWorkoutPlanExercise(workoutPlanId: number, input: WorkoutPlanExerciseInput): Promise<WorkoutPlanExercise>;
  updateWorkoutPlanExercise(workoutPlanId: number, workoutPlanExerciseId: number, input: WorkoutPlanExerciseUpdate): Promise<WorkoutPlanExercise>;
  deleteWorkoutPlanExercise(workoutPlanId: number, workoutPlanExerciseId: number): Promise<void>;
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
    return (await response.json()) as WorkoutPlan[];
  },

  async getWorkoutPlan(id) {
    const response = await fetch(`/api/workout-plans/${id}`);
    ensureOk(response, `get workout plan ${id}`);
    return (await response.json()) as WorkoutPlan;
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
    return (await response.json()) as WorkoutPlan;
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
    return (await response.json()) as WorkoutPlan;
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
    return (await response.json()) as WorkoutPlanExercise[];
  },

  async addWorkoutPlanExercise(workoutPlanId, input) {
    const response = await fetch(`/api/workout-plans/${workoutPlanId}/workout-plan-exercises`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(input)
      },
    );
    ensureOk(response, `add exercise to workout plan ${workoutPlanId}`);
    return (await response.json()) as WorkoutPlanExercise;
  },

  async updateWorkoutPlanExercise(workoutPlanId, workoutPlanExerciseId, input) {
    const response = await fetch(
      `/api/workout-plans/${workoutPlanId}/workout-plan-exercises/${workoutPlanExerciseId}`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(input)
      },
    );
    ensureOk(response,`update workout plan exercise ${workoutPlanExerciseId}`);
    return (await response.json()) as WorkoutPlanExercise;
  },

  async deleteWorkoutPlanExercise(workoutPlanId, workoutPlanExerciseId) {
    const response = await fetch(
      `/api/workout-plans/${workoutPlanId}/workout-plan-exercises/${workoutPlanExerciseId}`, {
        method: 'DELETE',
      }
    );
    ensureOk(response, `delete workout plan exercise ${workoutPlanExerciseId}`);
  }
  // #endregion 
};