// import React from 'react';
// import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
// import HomePage from './components/HomePage';
// import Register from './components/Register';
// import Login from './components/Login';
// import AdminDashboard from './components/AdminDashboard';
// import SupervisorDashboard from './components/SupervisorDashboard';
// import WorkerDashboard from './components/WorkerDashboard';
// import ZoneManagement from './pages/ZoneManagement';
// import RouteManagement from './pages/RouteManagement';
// import PickupScheduling from './pages/PickupScheduling';
// import VehicleAssignment from './pages/VehicleAssignment';
// import WorkerManagement from './pages/WorkerManagement';
// import 'bootstrap/dist/css/bootstrap.min.css';
// import './styles/App.css';

// function App() {
//   return (
//     <Router>
//       <div className="App">
//         <Switch>
//           <Route path="/" exact component={HomePage} />
//           <Route path="/register" component={Register} />
//           <Route path="/login" component={Login} />
//           <Route path="/admin" component={AdminDashboard} />
//           <Route path="/supervisor" component={SupervisorDashboard} />
//           <Route path="/worker" component={WorkerDashboard} />
//           <Route path="/zones" component={ZoneManagement} />
//           <Route path="/routes" component={RouteManagement} />
//           <Route path="/pickups" component={PickupScheduling} />
//           <Route path="/vehicles" component={VehicleAssignment} />
//           <Route path="/workers" component={WorkerManagement} />
//         </Switch>
//       </div>
//     </Router>
//   );
// }

// export default App;


import React from 'react';
import { useLocation } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { Route, Routes } from 'react-router-dom';
import '@fortawesome/fontawesome-free/css/all.min.css';

import HomePage from './components/HomePage';
import Register from './components/Register';
import Login from './components/Login';
import AdminDashboard from './components/AdminDashboard';
import SupervisorDashboard from './components/SupervisorDashboard';
import WorkerDashboard from './components/WorkerDashboard';
import ZoneManagement from './pages/ZoneManagement';
import RouteManagement from './pages/RouteManagement';
import PickupScheduling from './pages/PickupScheduling';
import VehicleAssignment from './pages/VehicleAssignment';
import WorkerManagement from './pages/WorkerManagement';

import WorkerReports from './pages/WorkerReports';
import Header from './components/Header';
import VehicleManagement from './pages/VehicleManagement';
import WorkerAssignment from './pages/WorkerAssignment';

import 'bootstrap/dist/css/bootstrap.min.css';
import './styles/App.css';
import AdminReports from './pages/AdminReports';

function App() {
  const location = useLocation();
  const noHeaderRoutes = ['/admin', '/', '/vehiclemanagement', '/zone-management', '/workermanagement'];

  return (
    <div className="App" >
      {/* {!noHeaderRoutes.includes(location.pathname) && <Header />} */}
      
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/supervisor" element={<SupervisorDashboard />} />
        <Route path="/reports" element={<AdminReports />} />
      


        {/* <Route path="/worker" element={<WorkerDashboard />} /> */}
        <Route path="/zone-management" element={<ZoneManagement />} />
        <Route path="/worker" element={<WorkerReports />} />

        <Route path="/route-management/:zoneId" element={<RouteManagement />} />
        <Route path="/pickupscheduling" element={<PickupScheduling />} />
        <Route path="/vehiclemanagement" element={<VehicleManagement />} />
        <Route path="/vehicleassignment" element={<VehicleAssignment />} />
        <Route path="/workermanagement" element={<WorkerManagement />} />
        <Route path="/workerassignment" element={<WorkerAssignment />} />
      </Routes>

      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
}

export default App;
