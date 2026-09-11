import { Suspense } from 'react';
import { Outlet } from 'react-router';
import Navbar from './components/Navbar';

function App() {
  return (
    <>
      <Navbar />

      <Suspense fallback={null}>
        <Outlet />
      </Suspense>
    </>
  );
}

export default App;