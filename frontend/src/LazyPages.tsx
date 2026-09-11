import { lazy } from 'react';

export const Dashboard = lazy(() => import('./pages/Dashboard'));
export const CreateEditWorkoutPlan = lazy(() => import('./pages/CreateEditWorkoutPlan'));
export const PlanDetails = lazy(() => import('./pages/PlanDetails'));
export const ExerciseDetails = lazy(() => import('./pages/ExerciseDetails'));