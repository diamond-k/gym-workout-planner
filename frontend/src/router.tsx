import { createBrowserRouter } from 'react-router';
import App from './App';
import Dashboard from './pages/Dashboard';
import CreateEditWorkoutPlan from './pages/CreateEditWorkoutPlan';
import PlanDetails from './pages/PlanDetails';
import ExerciseDetails from './pages/ExerciseDetails';

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