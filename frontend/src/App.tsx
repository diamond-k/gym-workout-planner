import { Route, Routes } from 'react-router';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import CreateEditWorkoutPlan from './pages/CreateEditWorkoutPlan'
import PlanDetails from './pages/PlanDetails'
import ExerciseDetails from './pages/ExerciseDetails'

function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element= {<Dashboard />}/>
        <Route path="/workout-plans/new" element={<CreateEditWorkoutPlan />}/>
        <Route path="/workout-plans/:id" element={<PlanDetails />}/>
        <Route path="/workout-plans/:id/exercises/:workoutPlanExerciseId" element={<ExerciseDetails />}/>
        <Route path="/workout-plans/:id/edit" element={<CreateEditWorkoutPlan/>}/>
      </Routes>
    </>
  );
}

export default App;