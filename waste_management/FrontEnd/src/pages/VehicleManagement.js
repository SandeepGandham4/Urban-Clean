import React, { useEffect, useState,useRef } from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import { toast } from 'react-toastify';
import {
  getVehicles,
  createVehicle,
  updateVehicle,
  transferAndDeleteVehicle,
  deleteVehicle,
  updateVehicleStatus,
} from '../api/vehicleApi';
import '../styles/VehicleManagement.css';
import AdminHeader from '../components/AdminHeader';
import AdminSidebar from '../components/AdminSidebar';
 
const VehicleManagement = () => {
  const [vehicles, setVehicles] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [vehicleData, setVehicleData] = useState({
    registrationNo: '',
    type: '',
    status: ''
  });
  const [isEditing, setIsEditing] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [deleteStates, setDeleteStates] = useState({});
  const searchRef = useRef(null);
 

  useEffect(() => {
    fetchVehicles();
  }, []);
 
  const fetchVehicles = async () => {
    const data = await getVehicles();
    setVehicles(data);
  };
 
  const handleShow = (vehicle = { vehicleId: '', registrationNo: '', type: '', status: '' }) => {
    setVehicleData(vehicle);
    setIsEditing(!!vehicle.vehicleId);
    setShowForm(true);
    setTimeout(() => {
      searchRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, 0);
  };
 
  const handleClose = () => {
    setShowForm(false);
    setVehicleData({ vehicleId: '', registrationNo: '', type: '', status: '' });
    setIsEditing(false);
  };
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isEditing) {
        await updateVehicle(vehicleData.vehicleId, {
          registrationNo: vehicleData.registrationNo,
          type: vehicleData.type,
          status: vehicleData.status
        });
        toast.success('Vehicle updated successfully');
      } else {
        await createVehicle(vehicleData);
      }
 
      fetchVehicles();
      handleClose();
    } catch (err) {
      toast.error(err.message || 'Something went wrong');
    }
  };
 
  const handleDelete = async (vehicleId) => {
    try {
      setDeleteStates(prev => ({
        ...prev,
        [vehicleId]: { loading: true, message: '', error: false }
      }));
     
      const result = await deleteVehicle(vehicleId);
      if (result.success) {
        setDeleteStates(prev => ({
          ...prev,
          [vehicleId]: { loading: false, message: result.message, error: false }
        }));
        setTimeout(() => {
          setDeleteStates(prev => {
            const newState = { ...prev };
            delete newState[vehicleId];
            return newState;
          });
          fetchVehicles();
        }, 0);
      }
    } catch (err) {
      setDeleteStates(prev => ({
        ...prev,
        [vehicleId]: { loading: false, message: err.message, error: true }
      }));
    }
  };
 
 
  const handleStatusToggle = async (id, currentStatus) => {
    try {
      const newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
      await updateVehicleStatus(id, { status: newStatus });
      fetchVehicles();
    } catch (err) {
      console.error('Error:', err);
    }
  };
 
 
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };
 
  const filteredVehicles = vehicles.filter(vehicle =>
    vehicle.registrationNo.toLowerCase().includes(searchQuery.toLowerCase()) ||
    vehicle.type.toLowerCase().includes(searchQuery.toLowerCase())
  );
 
  return (
    <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
      <AdminHeader onToggleSidebar={toggleSidebar} />
      <AdminSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
      <div className="dashboard-content">
        <div className="vehicle-management">
          <div className="vehicle-management-header">
            <h2>Vehicle Management</h2>
            <div className="vehicle-controls">
              <InputGroup className="search-bar" ref={searchRef}>
                <Form.Control
                  placeholder="Search vehicles..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
                <InputGroup.Text className="search-icon">
                  <i className="fas fa-search"></i>
                </InputGroup.Text>
              </InputGroup>
              <Button
                variant="primary"
                onClick={() => setShowForm(!showForm)}
              >
                {showForm ? 'Close' : 'Add Vehicle'}
              </Button>
            </div>
          </div>
 
          <div className={`add-vehicle-form-container ${showForm ? 'visible' : 'hidden'}`}>
            <Form onSubmit={handleSubmit} className="add-vehicle-form">
              <Form.Group controlId="formRegistrationNo">
                <Form.Label>Registration No</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter registration number"
                  value={vehicleData.registrationNo}
                  onChange={(e) => setVehicleData({ ...vehicleData, registrationNo: e.target.value })}
                  required
                />
              </Form.Group>
              <Form.Group controlId="formType">
                <Form.Label>Type</Form.Label>
                <Form.Select
                  value={vehicleData.type}
                  onChange={(e) => setVehicleData({ ...vehicleData, type: e.target.value })}
                  required
                >
                  <option value="">Select Type</option>
                  <option value="VAN">Van</option>
                  <option value="TRUCK">Truck</option>
                  <option value="LORRY">Lorry</option>
                </Form.Select>
              </Form.Group>
              <Form.Group controlId="formStatus">
                <Form.Label>Status</Form.Label>
                <Form.Select
                  value={vehicleData.status}
                  onChange={(e) => setVehicleData({ ...vehicleData, status: e.target.value })}
                  required
                >
                  <option value="">Select Status</option>
                  <option value="ACTIVE">ACTIVE</option>
                  <option value="INACTIVE">INACTIVE</option>
                </Form.Select>
              </Form.Group>
              <div className="form-buttons">
                <Button type="submit" className="submit-btn">
                  {isEditing ? 'Update Vehicle' : 'Create Vehicle'}
                </Button>
                <Button type="button" className="cancel-btn" onClick={handleClose}>
                  Cancel
                </Button>
              </div>
            </Form>
          </div>
 
          <div className="vehicle-grid">
            {filteredVehicles.map(vehicle => (
              <div className="vehicle-card" key={vehicle.vehicleId}>
                <h4>Vehicle #{vehicle.vehicleId}</h4>
                <p><strong>Registration:</strong> {vehicle.registrationNo}</p>
                <p><strong>Type:</strong> {vehicle.type}</p>
                <p><strong>Status:</strong> {vehicle.status}</p>
                <div className="vehicle-actions">
                <i className="fas fa-edit action-icon edit-icon" onClick={() => handleShow(vehicle)}></i>
                  <i
                    className="fas fa-trash-alt action-icon delete-icon"
                    onClick={() => handleDelete(vehicle.vehicleId)}
                    style={{ cursor: deleteStates[vehicle.vehicleId]?.loading ? 'not-allowed' : 'pointer' }}
                  ></i>
                </div>
                {deleteStates[vehicle.vehicleId] && (
                  <div className={`delete-message ${deleteStates[vehicle.vehicleId].error ? 'error' : 'success'}`}>
                    {deleteStates[vehicle.vehicleId].message}
                  </div>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
 
export default VehicleManagement;
 
 
 