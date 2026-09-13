import { lazy } from 'react';

export const Dashboard = lazy(() => import('./pages/Dashboard'));
export const CreateEditWorkoutPlan = lazy(() => import('./pages/CreateEditWorkoutPlan'));
export const WorkoutDetails = lazy(() => import('./pages/WorkoutDetails'));
export const ExerciseDetails = lazy(() => import('./pages/ExerciseDetails'));