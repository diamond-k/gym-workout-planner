import { Route, Routes } from 'react-router';
import Navbar from './components/Navbar';
import Dashboard from './pages/Dashboard';
import CreateEditWorkoutPlan from './pages/CreateEditWorkoutPlan'
import PlanDetails from './pages/PlanDetails'

function App() {
  return (
    <>
      <Navbar />
      <Routes>
        <Route path="/" element= {<Dashboard />}/>
        <Route path="/workout-plans/new" element={<CreateEditWorkoutPlan />}/>
        <Route path="/workout-plans/:id" element={<PlanDetails />}/>
      </Routes>
    </>
  );
}

export default App;