import React, { useEffect, useState ,useRef} from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import {
  getAllWorkers,
  createWorker,
  updateWorker,
  deleteWorker,
  getWorkerByName,
  getWorkersByRole
} from '../api/workerApi';
import '../styles/WorkerManagement.css';
import AdminHeader from '../components/AdminHeader';
import AdminSidebar from '../components/AdminSidebar';
 
const roles = ['DRIVER', 'GARBAGECOLLECTOR'];
 
const WorkerManagement = () => {
  const [workers, setWorkers] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [workerData, setWorkerData] = useState({ id: '', name: '', contactInfo: '', role: '' });
  const [isEditing, setIsEditing] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [searchMode, setSearchMode] = useState('name');
  const formRef = useRef(null);
 
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
 
  useEffect(() => {
    fetchWorkers();
  }, []);
 
  const fetchWorkers = async () => {
    const data = await getAllWorkers();
    setWorkers(data);
  };
 
  const handleShow = (worker = { workerId: '', name: '', contactInfo: '', role: '' }) => {
    setWorkerData(worker);
    setIsEditing(!!worker.workerId);
    setShowForm(true);
    setTimeout(() => {
      if (formRef.current) {
        formRef.current.scrollIntoView({ behavior: 'smooth' });
      }
    }, 0);
 
  };
 
  const handleClose = () => {
    setShowForm(false);
    setWorkerData({ id: '', name: '', contactInfo: '', role: '' });
    setIsEditing(false);
  };
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isEditing) {
        await updateWorker(workerData.workerId, workerData);
      } else {
        const { name, contactInfo, role } = workerData;
        await createWorker({ name, contactInfo, role });
      }
      fetchWorkers();
      handleClose();
    } catch (err) {
      alert(err.message || 'Operation failed');
    }
  };
 
  const handleDelete = async (id) => {
    try {
      await deleteWorker(id);
      fetchWorkers();
    } catch (err) {
      alert(err.message || 'Delete operation failed');
    }
  };
 
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };
 
  const filteredWorkers = workers.filter(worker =>
    worker.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    worker.role.toLowerCase().includes(searchQuery.toLowerCase()) ||
    worker.contactInfo.toLowerCase().includes(searchQuery.toLowerCase())
  );
 
  return (
    <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
      <AdminHeader onToggleSidebar={toggleSidebar} />
      <AdminSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
      <div className="dashboard-content">
        <div className="worker-management">
          <div className="worker-management-header">
            <h2>Worker Management</h2>
            <div className="worker-controls">
              <InputGroup className="search-bar">
                <Form.Control
                  placeholder="Search workers..."
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
                {showForm ? 'Close' : 'Add Worker'}
              </Button>
            </div>
          </div>
 
          <div ref={formRef} className={`add-worker-form-container ${showForm ? 'visible' : 'hidden'}`}>
            <Form onSubmit={handleSubmit} className="add-worker-form">
              <Form.Group controlId="formName">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter worker name"
                  value={workerData.name}
                  onChange={(e) => setWorkerData({ ...workerData, name: e.target.value })}
                  required
                />
              </Form.Group>
 
              <Form.Group controlId="formContactInfo">
                <Form.Label>Contact Info</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter contact information"
                  value={workerData.contactInfo}
                  onChange={(e) => setWorkerData({ ...workerData, contactInfo: e.target.value })}
                  required
                />
              </Form.Group>
 
              <Form.Group controlId="formRole">
                <Form.Label>Role</Form.Label>
                <Form.Select
                  value={workerData.role}
                  onChange={(e) => setWorkerData({ ...workerData, role: e.target.value })}
                  required
                >
                  <option value="">Select Role</option>
                  {roles.map(role => (
                    <option key={role} value={role}>{role}</option>
                  ))}
                </Form.Select>
              </Form.Group>
 
              <div className="form-buttons">
                <Button type="submit" className="submit-btn">
                  {isEditing ? 'Update Worker' : 'Create Worker'}
                </Button>
                <Button type="button" className="cancel-btn" onClick={handleClose}>
                  Cancel
                </Button>
              </div>
            </Form>
          </div>
 
          <div className="worker-grid">
            {filteredWorkers.map(worker => (
              <div className="worker-card" key={worker.workerId}>
                <h4>Worker #{worker.workerId}</h4>
                <p><strong>Name:</strong> {worker.name}</p>
                <p><strong>Role:</strong> {worker.role}</p>
                <p><strong>Contact:</strong> {worker.contactInfo}</p>
                <div className="worker-actions">
                  <i className="fas fa-edit action-icon edit-icon" onClick={() => handleShow(worker)}></i>
                  <i className="fas fa-trash-alt action-icon delete-icon" onClick={() => handleDelete(worker.workerId)}></i>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
 
 
 
export default WorkerManagement;
 
 