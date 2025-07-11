// import React, { useEffect, useState } from 'react';
// import { Button, Modal, Form } from 'react-bootstrap';
// import {
//   getAssignments,
//   createAssignment,
//   updateAssignment,
//   deleteAssignment,
//   getRoutesByZone,
//   getUnassignedSchedules,
//   notifyAllRoutesScheduled,
// } from '../api/vehicleassignmentApi';
// import "../styles/VehicleAssignment.css";
// import { getVehicles,getUnassignedVehicles } from '../api/vehicleApi';
// import AdminHeader from '../components/AdminHeader';
// import AdminSidebar from '../components/AdminSidebar';
 
// const VehicleAssignment = () => {
//   const [assignments, setAssignments] = useState([]);
//   const [newSchedules, setNewSchedules] = useState([]);
//   const [showModal, setShowModal] = useState(false);
//   const [isEditing, setIsEditing] = useState(false);
//   const [zoneId, setZoneId] = useState('');
//   const [routeId, setRouteId] = useState('');
//   const [routesByZone, setRoutesByZone] = useState({});
//   const [vehicleType, setVehicleType] = useState('');
//   const [availableVehicles, setAvailableVehicles] = useState([]);
//   const [selectedSchedule, setSelectedSchedule] = useState(null);
//   const [isSidebarOpen, setIsSidebarOpen] = useState(true);
//   const [assignmentData, setAssignmentData] = useState({
//     vehicleId: '',
//     dateAssigned: '',
//     routeId: '',
//   });
//   const [assignedRoutesPerZone, setAssignedRoutesPerZone] = useState({});
 
//   useEffect(() => {
//     fetchAssignments();
//     fetchNewlyAdded();
//   }, []);
 
//   const fetchAssignments = async () => {
//     const data = await getAssignments();
//     setAssignments(data);
//   };
 
//   const fetchNewlyAdded = async () => {
//     const data = await getUnassignedSchedules();
//     setNewSchedules(data);
//   };
 
//   const handleShow = (assignment) => {
//     setIsEditing(true);
//     setAssignmentData({
//       vehicleId: assignment.vehicleId,
//       dateAssigned: assignment.dateAssigned,
//       assignmentId: assignment.assignmentId,
//       routeId: Number(assignment.routeId)
//     });
//     setZoneId(assignment.zoneId);
//     setRouteId(Number(assignment.routeId));
//     setVehicleType('');
//     setAvailableVehicles([]);
//     setShowModal(true);
//   };
 
//   const handleScheduleClick = async (schedule) => {
//     const zone = schedule.zoneId;
//     const response = await getRoutesByZone(zone);
//     setSelectedSchedule(schedule);
//     setZoneId(zone);
//     setRoutesByZone(prev => ({
//       ...prev,
//       [zone]: response
//     }));
//   };
 
//   const handleAddFromRoute = (zone, route) => {
//     setZoneId(zone);
//     setRouteId(route);
//     setVehicleType('');
//     setAvailableVehicles([]);
//     setAssignmentData({
//       vehicleId: '',
//       dateAssigned: selectedSchedule?.dateAssigned || ''
//     });
//     setIsEditing(false);
//     setShowModal(true);
//   };
 
//   const handleVehicleTypeChange = async (e) => {
//     const type = e.target.value;
//     setVehicleType(type);
//     const response = await getUnassignedVehicles();
//     const filtered = response.filter(v => v.type === type && v.status === 'ACTIVE');
//     setAvailableVehicles(filtered);
//   };
 
//   const selectVehicle = (id) => {
//     setAssignmentData({ ...assignmentData, vehicleId: id });
//   };
 
//   const handleClose = () => {
//     setShowModal(false);
//     setAssignmentData({ vehicleId: '', dateAssigned: '' });
//     setZoneId('');
//     setRouteId('');
//     setVehicleType('');
//     setAvailableVehicles([]);
//     setIsEditing(false);
//   };
 
//   const handleSubmit = async (e) => {
//     e.preventDefault();
//     try {
//       if (isEditing) {
//         await updateAssignment(assignmentData.assignmentId, assignmentData);
//       } else {
//         await createAssignment(zoneId, routeId, assignmentData);
//         console.log(routesByZone[zoneId].length);
//         const updatedRoutes = await getRoutesByZone(zoneId);
//       setRoutesByZone(prev => ({ ...prev, [zoneId]: updatedRoutes }));
 
//       setAssignedRoutesPerZone(prev => {
//         const updated = new Set(prev[zoneId] || []);
//         updated.add(routeId);
//         return { ...prev, [zoneId]: [...updated] };
//       });
//       }
//       fetchAssignments();
//       handleClose();
//     } catch (error) {
//       alert(error.message || 'Assignment operation failed');
//     }
//   };
 
//   const handleDelete = async (assignmentId) => {
//     await deleteAssignment(assignmentId);
//     fetchAssignments();
//   };
 
//   const handleSaveComplete = async (zoneId) => {
//     alert(`✅ All routes in Zone ${zoneId} have been assigned!`);
//     await notifyAllRoutesScheduled(zoneId);
//     fetchNewlyAdded();
//   };
 
//   const toggleSidebar = () => {
//     setIsSidebarOpen(!isSidebarOpen);
//   };
 
//   return (
//     <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
//       <AdminHeader onToggleSidebar={toggleSidebar} />
//       <AdminSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
//       <div className="dashboard-content">
//         <div className="assignment-management">
//           <h2>Assignment Management</h2>
 
//           <div className="new-schedule-section">
//             <h3>Newly Added Schedules</h3>
//             <div className="schedule-grid">
//               {newSchedules.map(schedule => (
//                 <div className="schedule-card" key={schedule.scheduleId}>
//                   <p><strong>Zone:</strong> {schedule.zoneName}</p>
//                   <p><strong>Date:</strong> {schedule.dateAssigned}</p>
//                   <Button variant="success" onClick={() => handleScheduleClick(schedule)}>+</Button>
 
//                   {routesByZone[schedule.zoneId] && (
//                     <div className="routes-section">
//                       {routesByZone[schedule.zoneId].map(route => (
//                         <div key={route.id} className="route-card">
//                           <p>{route.pathDetails}</p>
//                           <Button
//                             size="sm"
//                             variant="outline-primary"
//                             onClick={() => handleAddFromRoute(schedule.zoneId, route.id)}
//                           >
//                             Assign
//                           </Button>
//                         </div>
//                       ))}
//                       {routesByZone[schedule.zoneId].length === 0 && (
//                         <div className="text-center mt-3">
//                           <Button variant="success" onClick={() => handleSaveComplete(schedule.zoneId)}>
//                             ✅ Save Assignments
//                           </Button>
//                         </div>
//                       )}
//                     </div>
//                   )}
//                 </div>
//               ))}
//             </div>
//           </div>
 
//           <div className="assignment-grid">
//             {assignments.map((assignment) => (
//               <div className="assignment-card" key={assignment.assignmentId}>
//                 <h4>Assignment #{assignment.assignmentId}</h4>
//                 <p><strong>Route ID:</strong> {assignment.routeId}</p>
//                 <p><strong>Vehicle ID:</strong> {assignment.vehicleId}</p>
//                 <p><strong>Vehicle Reg No:</strong> {assignment.vehicleRegistrationNo}</p>
//                 <p><strong>Schedule Date:</strong> {assignment.dateAssigned}</p>
//                 <p><strong>Vehicle Status:</strong> {
//   assignment.vehicleStatus === '0'
//     ? "Not Started"
//     : assignment.vehicleStatus === 'Completed'
//     ? "Waste Collection Completed"
//     : "Completed Collecting " + assignment.vehicleStatus + "km"
// }</p>
 
//                 <div className="assignment-actions">
//                   <Button variant="warning" onClick={() => handleShow(assignment)}>Edit</Button>
//                   <Button variant="danger" onClick={() => handleDelete(assignment.assignmentId)}>Delete</Button>
//                 </div>
//               </div>
//             ))}
//           </div>
 
//           <Modal show={showModal} onHide={handleClose}>
//             <Modal.Header closeButton>
//               <Modal.Title>{isEditing ? 'Edit Assignment' : 'Add Assignment'}</Modal.Title>
//             </Modal.Header>
//             <Modal.Body>
//               <Form onSubmit={handleSubmit}>
//                 {!isEditing && (
//                   <>
//                     <Form.Group>
//                       <Form.Label>Zone</Form.Label>
//                       <Form.Control value={zoneId} readOnly />
//                     </Form.Group>
//                     <Form.Group>
//                       <Form.Label>Route</Form.Label>
//                       <Form.Control value={routeId} readOnly />
//                     </Form.Group>
//                   </>
//                 )}
 
//                 <Form.Group>
//                   <Form.Label>Select Vehicle Type</Form.Label>
//                   <Form.Control
//                     as="select"
//                     value={vehicleType}
//                     onChange={handleVehicleTypeChange}
//                   >
//                     <option value="">Select Vehicle Type</option>
//                     <option value="VAN">VAN</option>
//                     <option value="TRUCK">TRUCK</option>
//                     <option value="LORRY">LORRY</option>
//                   </Form.Control>
//                 </Form.Group>
 
//                 {vehicleType && (
//                   <div className="vehicle-selection-list">
//                     {availableVehicles.map(vehicle => (
//                       <div key={vehicle.vehicleId} className="vehicle-option">
//                         <span>{vehicle.registrationNo} (ID: {vehicle.vehicleId})</span>
//                         <Button
//                           variant="outline-success"
//                           size="sm"
//                           onClick={() => selectVehicle(vehicle.vehicleId)}
//                         >
//                           +
//                         </Button>
//                       </div>
//                     ))}
//                   </div>
//                 )}
 
//                 {assignmentData.vehicleId && (
//                   <div className="selected-vehicle-info">
//                     ✅ Vehicle selected: <strong>{assignmentData.vehicleId}</strong>
//                   </div>
//                 )}
 
//                 <Form.Group>
//                   <Form.Label>Schedule Date</Form.Label>
//                   <Form.Control
//                     type="date"
//                     value={assignmentData.dateAssigned}
//                     onChange={(e) =>
//                       setAssignmentData({ ...assignmentData, dateAssigned: e.target.value })
//                     }
//                     required
//                   />
//                 </Form.Group>
 
//                 <Button type="submit" variant="primary">
//                   {isEditing ? 'Update Assignment' : 'Create Assignment'}
//                 </Button>
//               </Form>
//             </Modal.Body>
//           </Modal>
//         </div>
//       </div>
//     </div>
//   );
// };
 
// export default VehicleAssignment;
 
 
 
import React, { useEffect, useState, useRef } from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import {
  getAssignments,
  createAssignment,
  updateAssignment,
  deleteAssignment,
  getRoutesByZone,
  getUnassignedSchedules,
  notifyAllRoutesScheduled,
} from '../api/vehicleassignmentApi';
import "../styles/VehicleAssignment.css";
import { getVehicles,getUnassignedVehicles,getVehicleById } from '../api/vehicleApi';
import AdminHeader from '../components/AdminHeader';
import AdminSidebar from '../components/AdminSidebar';
import { toast } from 'react-toastify';
 
const VehicleAssignment = () => {
  const [assignments, setAssignments] = useState([]);
  const [newSchedules, setNewSchedules] = useState([]);
  const [showForms, setShowForms] = useState({});
  const [editForms, setEditForms] = useState({});
  
  const [zoneId, setZoneId] = useState('');
  const [routeId, setRouteId] = useState('');
  const [routesByZone, setRoutesByZone] = useState({});
  const [vehicleType, setVehicleType] = useState('');
  const [availableVehicles, setAvailableVehicles] = useState([]);
  const [selectedSchedule, setSelectedSchedule] = useState(null);
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [assignmentData, setAssignmentData] = useState({
    vehicleId: '',
    dateAssigned: '',
    routeId: '',
  });
  const [assignedRoutesPerZone, setAssignedRoutesPerZone] = useState({});
  const [isEditing, setIsEditing] = useState(false);
  const [expandedSchedules, setExpandedSchedules] = useState({});
  const editFormRef = useRef(null);
 

  useEffect(() => {
    fetchAssignments();
    fetchNewlyAdded();
  }, []);
 
  const fetchAssignments = async () => {
    const data = await getAssignments();
    setAssignments(data);
  };
 
  const fetchNewlyAdded = async () => {
    try{
    const data = await getUnassignedSchedules();
    setNewSchedules(data);
    }catch (error) {
      if (error.response?.status === 500) {
        toast.info("No schedules found.", {
          toastId: "no-schedules"
        });
      } else {
        console.error("Error fetching unassigned schedules:", error);
      }
    }
  };
 
  const toggleAssignForm = (zoneId, routeId) => {
    setShowForms(prev => ({
      ...prev,
      [`${zoneId}-${routeId}`]: !prev[`${zoneId}-${routeId}`]
    }));
    if (!showForms[`${zoneId}-${routeId}`]) {
      setIsEditing(false);
      setZoneId(zoneId);
      setRouteId(routeId);
      setVehicleType('');
      setAvailableVehicles([]);
      setAssignmentData({
        vehicleId: '',
        dateAssigned: selectedSchedule?.dateAssigned || ''
      });
    }
  };
 
  const toggleEditForm = (assignmentId) => {
    setEditForms(prev => {
      const newState = {};
      // Close all other forms
      Object.keys(prev).forEach(key => {
        newState[key] = false;
      });
      // Toggle the clicked form
      newState[assignmentId] = !prev[assignmentId];
      return newState;
    });
  };
 
  const handleShow = async (assignment) => {
    // Toggle the form
    toggleEditForm(assignment.assignmentId);
   
    // Set editing mode and assignment data
 
    setIsEditing(true);
    setAssignmentData({
      vehicleId: assignment.vehicleId,
      dateAssigned: assignment.dateAssigned,
      assignmentId: assignment.assignmentId,
      routeId: Number(assignment.routeId)
    });
    setZoneId(assignment.zoneId);
    setRouteId(Number(assignment.routeId));
    // Get the vehicle details to set the correct vehicle type
    try {
      const vehicle = await getVehicleById(assignment.vehicleId);
      setVehicleType(vehicle.type);
     
      // Get unassigned vehicles of the same type
      const unassignedResponse = await getUnassignedVehicles();
      const filtered = unassignedResponse.filter(v => v.type === vehicle.type && v.status === 'ACTIVE');
      // Include the currently assigned vehicle in the list
      const currentVehicle = {
        vehicleId: assignment.vehicleId,
        registrationNo: assignment.vehicleRegistrationNo,
        type: vehicle.type,
        status: 'ACTIVE'
      };
      setAvailableVehicles([currentVehicle, ...filtered]);
    } catch (error) {
      console.error('Error loading vehicle details:', error);
    }
   
    // Scroll to the edit form
    setTimeout(() => {
      if (editFormRef.current) {
        editFormRef.current.scrollIntoView({
          behavior: 'smooth',
          block: 'center',
          inline: 'nearest'
        });
      }
    }, 300);
  };
 
  const handleScheduleClick = async (schedule) => {
    const zone = schedule.zoneId;
    if (!expandedSchedules[zone]) {
      const response = await getRoutesByZone(zone);
      setSelectedSchedule(schedule);
      setZoneId(zone);
      setRoutesByZone(prev => ({
        ...prev,
        [zone]: response || []
      }));
    }
    setExpandedSchedules(prev => ({
      ...prev,
      [zone]: !prev[zone]
    }));
  };
 
 
  const handleAddFromRoute = (zone, route) => {
    setZoneId(zone);
    setRouteId(route);
    setVehicleType('');
    setAvailableVehicles([]);
    setAssignmentData({
      vehicleId: '',
      dateAssigned: selectedSchedule?.dateAssigned || ''
    });
    setIsEditing(false);
    toggleAssignForm(zone, route);
  };
 
  const handleVehicleTypeChange = async (e) => {
    const type = e.target.value;
    setVehicleType(type);
    if (type) {
      try {
        const response = await getUnassignedVehicles();
        const filtered = response.filter(v => v.type === type && v.status === 'ACTIVE');
        setAvailableVehicles(filtered);
      } catch (error) {
        console.error('Error fetching unassigned vehicles:', error);
        setAvailableVehicles([]);
      }
    } else {
      setAvailableVehicles([]);
    }
  };
 
 
  const selectVehicle = (id) => {
    setAssignmentData({ ...assignmentData, vehicleId: id });
  };
 
  const handleClose = () => {
    setAssignmentData({ vehicleId: '', dateAssigned: '' });
    setZoneId('');
    setRouteId('');
    setVehicleType('');
    setAvailableVehicles([]);
    setIsEditing(false);
  };
 
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isEditing) {
        // For editing, we need to update the assignment with the new vehicle and date
        await updateAssignment(assignmentData.assignmentId,assignmentData);
        toggleEditForm(assignmentData.assignmentId); // Close the edit form
      } else {
        await createAssignment(zoneId, routeId, assignmentData);
        const updatedRoutes = await getRoutesByZone(zoneId);
        setRoutesByZone(prev => ({ ...prev, [zoneId]: updatedRoutes }));
 
        setAssignedRoutesPerZone(prev => {
          const updated = new Set(prev[zoneId] || []);
          updated.add(routeId);
          return { ...prev, [zoneId]: [...updated] };
        });
      }
      fetchAssignments();
      handleClose();
    } catch (error) {
      alert(error.message || 'Assignment operation failed');
    }
  };
 
  const handleDelete = async (assignmentId) => {
    await deleteAssignment(assignmentId);
    fetchAssignments();
  };
 
  const handleSaveComplete = async (zoneId) => {
    toast.success(`✅ All routes in Zone ${zoneId} have been assigned!`);
    await notifyAllRoutesScheduled(zoneId);
    fetchNewlyAdded();
  };
 
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };
 
  const filteredAssignments = assignments.filter(assignment =>
    assignment.vehicleRegistrationNo?.toLowerCase().includes(searchQuery.toLowerCase()) ||
    assignment.routeId?.toString().includes(searchQuery) ||
    assignment.dateAssigned?.includes(searchQuery)
  );
 
  return (
    <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
      <AdminHeader onToggleSidebar={toggleSidebar} />
      <AdminSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
      <div className="dashboard-content">
        <div className="assignment-management">
          <div className="assignment-management-header">
            <h2>Schedule Assignment</h2>
            <div className="assignment-controls">
              <InputGroup className="search-bar">
                <Form.Control
                  placeholder="Search assignments..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
                <InputGroup.Text className="search-icon">
                  <i className="fas fa-search"></i>
                </InputGroup.Text>
              </InputGroup>
            </div>
          </div>
 
          <div className="new-schedule-section">
            <div className="schedule-grid">
              {newSchedules.map(schedule => (
                <div className="schedule-card" key={schedule.scheduleId}>
                  <div className="schedule-info">
                    <div className="zone-info">
                      <strong>{schedule.zoneName}</strong>
                      <span className="time-slot"> | {schedule.timeSlot}</span>
                    </div>
                    <Button
                      className="show-routes-btn"
                      onClick={() => handleScheduleClick(schedule)}
                    >
                      {expandedSchedules[schedule.zoneId] ? '- Hide Routes' : '+ Show Routes'}
                    </Button>
                  </div>
 
                  {expandedSchedules[schedule.zoneId] && (
                    <div className="routes-section">
                      {routesByZone[schedule.zoneId] && routesByZone[schedule.zoneId].length === 0 ? (
                        <div className="text-center mt-3">
                          <Button variant="success" onClick={() => handleSaveComplete(schedule.zoneId)}>
                            ✅ Save Assignments
                          </Button>
                        </div>
                      ): (
                        routesByZone[schedule.zoneId]?.map(route => (
                          <div key={route.id} className="route-row">
                            <div className="route-info">
                              <p>{route.pathDetails}</p>
                              <Button
                                variant="outline-primary"
                                onClick={() => toggleAssignForm(schedule.zoneId, route.id)}
                              >
                                {showForms[`${schedule.zoneId}-${route.id}`] ? 'Cancel' : 'Assign'}
                              </Button>
                            </div>
                           
                            {showForms[`${schedule.zoneId}-${route.id}`] && (
                              <Form className="inline-assignment-form" onSubmit={handleSubmit}>
                                <Form.Group>
                                  <Form.Label>Select Vehicle Type</Form.Label>
                                  <Form.Select
                                    value={vehicleType}
                                    onChange={handleVehicleTypeChange}
                                  >
                                    <option value="">Select Vehicle Type</option>
                                    <option value="VAN">VAN</option>
                                    <option value="TRUCK">TRUCK</option>
                                    <option value="LORRY">LORRY</option>
                                  </Form.Select>
                                </Form.Group>
 
                                {vehicleType && (
                                  <div className="vehicle-selection-list">
                                    {availableVehicles.map(vehicle => (
                                      <div key={vehicle.vehicleId} className="vehicle-option">
                                        <span>{vehicle.registrationNo} (ID: {vehicle.vehicleId})</span>
                                        <Button
                                          variant="outline-success"
                                          size="sm"
                                          onClick={() => selectVehicle(vehicle.vehicleId)}
                                        >
                                        +
                                        </Button>
                                      </div>
                                    ))}
                                  </div>
                                )}
 
                                {assignmentData.vehicleId && (
                                  <div className="selected-vehicle-info">
                                    ✅ Vehicle selected: <strong>{assignmentData.vehicleId}</strong>
                                  </div>
                                )}
 
                                <Form.Group>
                                  <Form.Label>Schedule Date</Form.Label>
                                  <Form.Control
                                    type="date"
                                    value={assignmentData.dateAssigned}
                                    onChange={(e) =>
                                      setAssignmentData({ ...assignmentData, dateAssigned: e.target.value })
                                    }
                                    required
                                  />
                                </Form.Group>
 
                                <div className="form-actions">
                                  <Button type="submit" variant="success">
                                    Create Assignment
                                  </Button>
                                </div>
                              </Form>
                            )}
                          </div>
                        ))
                      )}
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
 
          <h3>Existing Assignments</h3>
          <div className="assignment-grid">
            {filteredAssignments.map((assignment) => (
              <div className="assignment-card" key={assignment.assignmentId}>
                <div className="assignment-info">
                  <div className="assignment-details">
                    <p><strong>Assignment ID:</strong> #{assignment.assignmentId}</p>
                    <p><strong>Route:</strong> {assignment.routeId}</p>
                    <p><strong>Vehicle:</strong> {assignment.vehicleRegistrationNo} (ID: {assignment.vehicleId})</p>
                    <p><strong>Date:</strong> {assignment.dateAssigned}</p>
                    <p><strong>Status:</strong>
                      <span className={`assignment-status ${
                        assignment.vehicleStatus === '0'
                          ? 'status-not-started'
                          : assignment.vehicleStatus === 'Completed'
                          ? 'status-completed'
                          : 'status-in-progress'
                      }`}>
                        {assignment.vehicleStatus === '0'
                          ? "Not Started"
                          : assignment.vehicleStatus === 'Completed'
                          ? "Completed"
                          : `${assignment.vehicleStatus}km collected`}
                      </span>
                    </p>
                  </div>
                  <div className="assignment-actions">
                    <button
                      className="action-icon edit-icon"
                      onClick={() => handleShow(assignment)}
                      title="Edit Assignment"
                    >
                      <i className="fas fa-edit"></i>
                    </button>
                    <button
                      className="action-icon delete-icon"
                      onClick={() => handleDelete(assignment.assignmentId)}
                      title="Delete Assignment"
                    >
                      <i className="fas fa-trash-alt"></i>
                    </button>
                  </div>
                </div>
 
                {editForms[assignment.assignmentId] && (
                  <Form className="inline-edit-form" onSubmit={handleSubmit} ref={editFormRef}>
                    <div className="form-group">
                      <Form.Label>Select Vehicle Type</Form.Label>
                      <Form.Select
                        value={vehicleType}
                        onChange={handleVehicleTypeChange}
                        className="form-select"
                      >
                        <option value="">Select Vehicle Type</option>
                        <option value="VAN">VAN</option>
                        <option value="TRUCK">TRUCK</option>
                        <option value="LORRY">LORRY</option>
                      </Form.Select>
                    </div>
 
                    {vehicleType && (
                      <div className="vehicle-selection-list">
                        {availableVehicles.map(vehicle => (
                          <div key={vehicle.vehicleId} className="vehicle-option">
                            <span>{vehicle.registrationNo} (ID: {vehicle.vehicleId})</span>
                            <Button
                              variant="outline-success"
                              size="sm"
                              onClick={() => selectVehicle(vehicle.vehicleId)}
                            >
                              Select
                            </Button>
                          </div>
                        ))}
                      </div>
                    )}
 
                    {assignmentData.vehicleId && (
                      <div className="selected-vehicle-info">
                        <i className="fas fa-check-circle"></i> Vehicle selected: <strong>{assignmentData.vehicleId}</strong>
                      </div>
                    )}
 
                    <div className="form-group">
                      <Form.Label>Schedule Date</Form.Label>
                      <Form.Control
                        type="date"
                        value={assignmentData.dateAssigned}
                        onChange={(e) =>
                          setAssignmentData({ ...assignmentData, dateAssigned: e.target.value })
                        }
                        required
                        className="form-control"
                      />
                    </div>
 
                    <div className="form-actions d-flex justify-content-between mt-3">
                      <Button type="submit" variant="primary" className="flex-grow-1 me-2">
                        Update Assignment
                      </Button>
                      <Button
                        variant="secondary"
                        onClick={() => toggleEditForm(assignment.assignmentId)}
                        className="flex-grow-1 ms-2"
                      >
                        Cancel
                      </Button>
                    </div>
                  </Form>
                )}
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
 
export default VehicleAssignment;


