import React, { useEffect, useState } from 'react';
import { Card } from 'react-bootstrap';
import AdminSidebar from "./AdminSidebar";
import AdminHeader from './AdminHeader';
import axios from 'axios';
import '../styles/AdminDashboard.css';
import { FaTruck, FaUsers, FaCalendarCheck, FaMapMarkedAlt, FaRoute, FaWeight } from 'react-icons/fa';
import { getActiveVehicleCount } from '../api/vehicleApi'; 
import {getAllAssignments} from '../api/workerassignmentApi'; 
import {getSchedulesCount} from '../api/pickupApi';
import {getZones} from '../api/zoneApi';
import {getNumberOfRoutes} from '../api/zoneApi';
const AdminDashboard = () => {
  const [vehicles, setVehicles] = useState(0);
  const [workers, setWorkers] = useState(0);
  const [pickups, setPickups] = useState(0);
  const [zones, setZones] = useState(0);
  const [routes, setRoutes] = useState(0);
  const [weightCollected, setWeightCollected] = useState(0);
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
 
  // useEffect(() => {
  //   fetchDashboardData();
  // }, []);

  useEffect(() => {
    const fetchCount = async () => {
      try {
        const vehiclecount = await getActiveVehicleCount();
        setVehicles(vehiclecount);
        const workercount = await getAllAssignments();
        setWorkers(workercount.length);
        const scheduledPickups = await getSchedulesCount();
        setPickups(scheduledPickups);
        const zonesWithSchedules = await getZones();
        setZones(zonesWithSchedules.length);
        const routesWithSchedules = await getNumberOfRoutes();
        setRoutes(routesWithSchedules);
        console.log("Active vehicles:", vehiclecount);
        console.log("Active workers:", workercount);
        console.log("Scheduled pickups:", scheduledPickups);
        console.log("Zones with schedules:", zonesWithSchedules);
        console.log("Routes with schedules:", routesWithSchedules);
      } catch (err) {
        console.error("Failed to fetch active vehicle count:", err);
      }
    };
  
    fetchCount();
  }, []);
 
  // const fetchDashboardData = async () => {
  //   try {
  //     const [
  //       assignedVehicles,
  //       assignedWorkers,
  //       scheduledPickups,
  //       zonesWithSchedules,
  //       routesWithSchedules,
  //     ] = await Promise.all([
  //       axios.get('/api/urbanclean/v1/admin/vehicles/activestatus'),
  //       axios.get('/api/urbanclean/v1/admin/workerassignments'),
  //       axios.get('/api/urbanclean/v1/admin/vehicleassignments/schedules'),
  //       axios.get('/api/urbanclean/v1/admin/vehicleassignments/schedules'),
  //       axios.get('/api/urbanclean/v1/admin/routes/with-schedules'),
  //     ]);
 
  //     setVehicles({ assigned: assignedVehicles.data });
  //     setWorkers({ assigned: assignedWorkers.data });
  //     setPickups({ scheduled: scheduledPickups.data });
  //     setZones({ withSchedules: zonesWithSchedules.data });
  //     setRoutes({ rassigned: routesWithSchedules.data });
  //     setWeightCollected(Math.floor(Math.random() * 1000));
  //   } catch (error) {
  //     console.error('Error fetching dashboard data:', error);
  //   }
  // };
 
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };
 
  const closeSidebar = () => {
    setIsSidebarOpen(false);
  };
 
  return (
    <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
      <AdminHeader onToggleSidebar={toggleSidebar} />
      <AdminSidebar isOpen={isSidebarOpen} onClose={closeSidebar} />
 
      <div className="dashboard-content">
        <h1 className="dashboard-title">Admin Dashboard</h1>
 
        <div className="dashboard-grid">
          <Card className="dashboard-card">
            <Card.Body>
              <div className="card-icon-title">
                <FaTruck className="dashboard-icon" />
                <Card.Title>Active Vehicles</Card.Title>
              </div>
              <p>Assigned Vehicles: {vehicles}</p>
            </Card.Body>
          </Card>
 
          <Card className="dashboard-card">
            <Card.Body>
              <div className="card-icon-title">
                <FaUsers className="dashboard-icon" />
                <Card.Title>Active Workers</Card.Title>
              </div>
              <p>Assigned Workers: {workers}</p>
            </Card.Body>
          </Card>
 
          <Card className="dashboard-card">
            <Card.Body>
              <div className="card-icon-title">
                <FaCalendarCheck className="dashboard-icon" />
                <Card.Title>Scheduled Pickups</Card.Title>
              </div>
              <p>Scheduled: {pickups}</p>
            </Card.Body>
          </Card>
 
          <Card className="dashboard-card">
            <Card.Body>
              <div className="card-icon-title">
                <FaMapMarkedAlt className="dashboard-icon" />
                <Card.Title>Zones Covered</Card.Title>
              </div>
              <p>Zones with Schedules: {zones}</p>
            </Card.Body>
          </Card>
 
          <Card className="dashboard-card">
            <Card.Body>
              <div className="card-icon-title">
                <FaRoute className="dashboard-icon" />
                <Card.Title>Total Routes</Card.Title>
              </div>
              <p>Routes with Schedules: {routes}</p>
            </Card.Body>
          </Card>
 
          <Card className="dashboard-card">
            <Card.Body>
              <div className="card-icon-title">
                <FaWeight className="dashboard-icon" />
                <Card.Title>Weight Collected</Card.Title>
              </div>
              <p>{weightCollected} kg</p>
            </Card.Body>
          </Card>
        </div>
      </div>
    </div>
  );
};
 
export default AdminDashboard;
 
 