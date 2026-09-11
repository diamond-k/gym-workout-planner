import { createBrowserRouter } from 'react-router';
import App from './App';
import {
  Dashboard,
  CreateEditWorkoutPlan,
  PlanDetails,
  ExerciseDetails,
} from './LazyPages';

export const router = createBrowserRouter([
  {
    element: <App />,
    children: [
      {
        path: '/',
        element: <Dashboard />,
      },
      {
        path: '/workout-plans/new',
        element: <CreateEditWorkoutPlan />,
      },
      {
        path: '/workout-plans/:id',
        element: <PlanDetails />,
      },
      {
        path: '/workout-plans/:id/exercises/:workoutPlanExerciseId',
        element: <ExerciseDetails />,
      },
      {
        path: '/workout-plans/:id/edit',
        element: <CreateEditWorkoutPlan />,
      },
    ],
  },
]);