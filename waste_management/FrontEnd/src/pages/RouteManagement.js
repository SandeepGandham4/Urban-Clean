import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Table, Button, Form } from 'react-bootstrap';
import { getRoutes, createRoute, updateRoute, deleteRoute } from '../api/routeApi';
import { toast } from 'react-toastify';
import '../styles/RouteManagement.css';
import AdminHeader from '../components/AdminHeader';
import AdminSidebar from '../components/AdminSidebar';
 
const RouteManagement = () => {
  const { zoneId } = useParams(); // Get zoneId from the URL
  const [routes, setRoutes] = useState([]);
  const [newRoute, setNewRoute] = useState({ pathDetails: '', estimatedTime: '' });
  const [editingRouteId, setEditingRouteId] = useState(null);
  const [updatedRoute, setUpdatedRoute] = useState({ pathDetails: '', estimatedTime: '' });
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
 
  useEffect(() => {
    fetchRoutesForZone(zoneId);
  }, [zoneId, refreshTrigger]);
 
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };
 
  const closeSidebar = () => {
    setIsSidebarOpen(false);
  };
 
  const fetchRoutesForZone = async (zoneId) => {
    try {
      const data = await getRoutes(zoneId);
      setRoutes(data);
    } catch (error) {
      if (error.response?.status === 404) {
        toast.info("No routes found for this zone.",{
          toastId: "no-routes"
        });
        setRoutes([]);
      }else{
      console.error('Error fetching routes:', error);
      }
    }
  };
 
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewRoute({ ...newRoute, [name]: value });
  };
 
  const handleAddRoute = async (e) => {
    e.preventDefault();
    try {
      await createRoute(zoneId, newRoute);
      setNewRoute({ pathDetails: '', estimatedTime: '' });
      setRefreshTrigger(prev => prev + 1);
      toast.success("Route added successfully!");
    } catch (error) {
      toast.error("Failed to create route. Please try again.");
      //console.error('Error creating route:', error);
    }
  };
 
  const handleEditClick = (route) => {
    setEditingRouteId(route.id);
    setUpdatedRoute({ pathDetails: route.pathDetails, estimatedTime: route.estimatedTime });
  };
 
  const handleUpdateRoute = async (e) => {
    e.preventDefault();
    try {
      await updateRoute(zoneId, editingRouteId, updatedRoute);
      toast.success("Route updated successfully!");
      setEditingRouteId(null);
      fetchRoutesForZone(zoneId);
    } catch (error) {
      console.error('Error updating route:', error);
    }
  };
 
  const handleDeleteRoute = async (id) => {
    try {
      await deleteRoute(zoneId, id);
      toast.success("Route deleted successfully!");
      fetchRoutesForZone(zoneId);
    } catch (error) {
      console.error('Error deleting route:', error);
      const msg = error.response.data || "Something went wrong";
      toast.error(msg);
    }
  };
 
  return (
    <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
      <AdminHeader onToggleSidebar={toggleSidebar} />
      <AdminSidebar isOpen={isSidebarOpen} onClose={closeSidebar} />
      <div className="dashboard-content">
        <h2>Route Management for Zone {zoneId}</h2>
 
        {/* Add Route Form */}
        <Form onSubmit={handleAddRoute}>
          <Form.Group controlId="formPathDetails">
            <Form.Label>Path Details</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter path details"
              name="pathDetails"
              value={newRoute.pathDetails}
              onChange={handleInputChange}
              required
            />
          </Form.Group>
 
          <Form.Group controlId="formEstimatedTime">
            <Form.Label>Estimated Time</Form.Label>
            <Form.Control
              type="text"
              placeholder="Enter estimated time"
              name="estimatedTime"
              value={newRoute.estimatedTime}
              onChange={handleInputChange}
              required
            />
          </Form.Group>
 
          <Form.Group controlId="formZone">
            <Form.Label>Zone</Form.Label>
            <Form.Control type="text" value={zoneId} readOnly />
          </Form.Group>
 
          <Button variant="primary" type="submit" className="btn-block">
            Add Route
          </Button>
        </Form>
 
        {/* Routes Table */}
        <div className="table-responsive">
          <Table striped bordered hover className="route-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Path Details</th>
                <th>Estimated Time</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {routes.map((route) => (
                <tr key={route.id}>
                  <td>{route.id}</td>
                  <td>
                    {editingRouteId === route.id ? (
                      <Form.Control
                        type="text"
                        name="pathDetails"
                        value={updatedRoute.pathDetails}
                        onChange={(e) =>
                          setUpdatedRoute({ ...updatedRoute, pathDetails: e.target.value })
                        }
                      />
                    ) : (
                      route.pathDetails
                    )}
                  </td>
                  <td>
                    {editingRouteId === route.id ? (
                      <Form.Control
                        type="text"
                        name="estimatedTime"
                        value={updatedRoute.estimatedTime}
                        onChange={(e) =>
                          setUpdatedRoute({ ...updatedRoute, estimatedTime: e.target.value })
                        }
                      />
                    ) : (
                      route.estimatedTime
                    )}
                  </td>
                  <td>
                    {editingRouteId === route.id ? (
                      <div className="actions-container">
                      <i className="fas fa-save action-icon" onClick={handleUpdateRoute} title="Save Changes"></i>
                      <i className="fas fa-times action-icon" onClick={() => setEditingRouteId(null)} title="Cancel"></i>
                    </div>
                  ) : (
                    <div className="actions-container">
                      <i className="fas fa-edit action-icon edit-icon" onClick={() => handleEditClick(route)} title="Edit Route"></i>
                      <i className="fas fa-trash-alt action-icon delete-icon" onClick={() => handleDeleteRoute(route.id)} title="Delete Route"></i>
                    </div>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>
    </div>
  </div>
);
};

export default RouteManagement;


