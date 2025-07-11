// import React, { useEffect, useState } from 'react';
// import {
//   getSchedules,
//   getUnassignedVehiclesByZone,
//   getWorkersByRoleForRoute,
//   assignWorkersToVehicle,
//   notifyAllSchedule,
//   getAllAssignments
// } from '../api/workerassignmentApi';
// import { Button, Collapse, Form } from 'react-bootstrap';

// const shiftOptions = ['MORNING', 'EVENING'];

// const ScheduleAssignment = () => {
//   const [schedules, setSchedules] = useState([]);
//   const [vehiclesBySchedule, setVehiclesBySchedule] = useState({});
//   const [expandedSchedules, setExpandedSchedules] = useState({});
//   const [selectedRouteBySchedule, setSelectedRouteBySchedule] = useState({});
//   const [workersByRoute, setWorkersByRoute] = useState({});
//   const [uiState, setUiState] = useState({});
//   const [assignments, setAssignments] = useState([]);
//   const [routeAssignments, setRouteAssignments] = useState({});

//   useEffect(() => {
//     fetchSchedules();
//     fetchAssignments();
//   }, []);
//     const fetchSchedules = async () => {
//     const data = await getSchedules();
//     setSchedules(data);
//   };

//   const fetchAssignments = async () => {
//     const data = await getAllAssignments();
//     setAssignments(data);
//   };

//   const handleExpandSchedule = async (schedule) => {
//     setExpandedSchedules((prev) => ({
//       ...prev,
//       [schedule.scheduleId]: !prev[schedule.scheduleId]
//     }));

//     if (!vehiclesBySchedule[schedule.scheduleId]) {
//       const vehicles = await getUnassignedVehiclesByZone(schedule.zoneId);
//       setVehiclesBySchedule((prev) => ({ ...prev, [schedule.scheduleId]: vehicles }));
//     }
//   };

//   const handleRouteClick = async (scheduleId, routeId, zoneId) => {
//     setSelectedRouteBySchedule((prev) => ({ ...prev, [scheduleId]: routeId }));
//     const key = `${scheduleId}-${routeId}`;
//     if (!workersByRoute[key]) {
//       const [drivers, workers] = await Promise.all([
//         getWorkersByRoleForRoute(zoneId, routeId, 'DRIVER'),
//         getWorkersByRoleForRoute(zoneId, routeId, 'GARBAGECOLLECTOR')
//       ]);
//       setWorkersByRoute((prev) => ({ ...prev, [key]: { drivers, workers } }));
//     }
//   };

//   const handleAssignSelect = (routeKey, role, workerId, shift) => {
//     setRouteAssignments((prev) => {
//       const existing = prev[routeKey] || { driver: null, workers: [] };
//       if (role === 'DRIVER') {
//         return { ...prev, [routeKey]: { ...existing, driver: { workerId, shift } } };
//       } else {
//         const others = existing.workers.filter(w => w.workerId !== workerId);
//         return {
//           ...prev,
//           [routeKey]: {
//             ...existing,
//             workers: [...others, { workerId, shift }]
//           }
//         };
//       }
//     });
//   };

//   const handleUIState = (routeKey, toggleKey) => {
//     setUiState((prev) => ({
//       ...prev,
//       [routeKey]: {
//         ...prev[routeKey],
//         [toggleKey]: !prev[routeKey]?.[toggleKey]
//       }
//     }));
//   };

//   const handleSaveAssignment = async (schedule, routeId, vehicle) => {
//     const routeKey = `${schedule.scheduleId}-${routeId}`;
//     const current = routeAssignments[routeKey];
//     if (!current?.driver) return alert('Assign driver before saving.');

//     const shiftMap = {
//       [current.driver.workerId]: current.driver.shift,
//       ...Object.fromEntries(current.workers.map(w => [w.workerId, w.shift]))
//     };

//     await assignWorkersToVehicle(schedule.zoneId, routeId, {
//       vehicleId: vehicle.vehicleId,
//       vehicleNumber: vehicle.vehicleNumber,
//       driverId: current.driver.workerId,
//       workerIds: current.workers.map(w => w.workerId),
//       shiftTimeByWorker: shiftMap
//     });

//     alert(`✅ Assigned route ${routeId}`);
//     fetchAssignments();
//     fetchSchedules();
//   };

//   const isAllRoutesAssigned = (scheduleId) => {
//     const vehicles = vehiclesBySchedule[scheduleId] || [];
//     const routeIds = [...new Set(vehicles.map(v => v.routeId))];
//     return routeIds.every(routeId => {
//       const key = `${scheduleId}-${routeId}`;
//       return routeAssignments[key]?.driver;
//     });
//   };

//   const handleComplete = async (zoneId) => {
//     await notifyAllSchedule(zoneId);
//     alert(`✅ Schedule completed for zone ${zoneId}`);
//   };

//   return (
//     <div className="p-4">
//       <h3>Schedule Assignment</h3>

//       {schedules.map(schedule => {
//         const vehicles = vehiclesBySchedule[schedule.scheduleId] || [];
//         const routes = [...new Set(vehicles.map(v => v.routeId))];
//         const selectedRoute = selectedRouteBySchedule[schedule.scheduleId];
//         const selectedVehicle = vehicles.find(v => v.routeId === selectedRoute);
//         const routeKey = `${schedule.scheduleId}-${selectedRoute}`;
//         const pool = workersByRoute[routeKey] || {};
//         const ui = uiState[routeKey] || {};
//         const currentAssignment = routeAssignments[routeKey] || {};

//         return (
//           <div key={schedule.scheduleId} className="border rounded p-3 bg-light mb-4">
//             <div className="d-flex justify-content-between align-items-center">
//               <strong>{schedule.zoneName} | {schedule.timeSlot}</strong>
//               <Button size="sm" onClick={() => handleExpandSchedule(schedule)}>
//                 {expandedSchedules[schedule.scheduleId] ? 'Hide' : '➕ Show Routes'}
//               </Button>
//             </div>

//             <Collapse in={expandedSchedules[schedule.scheduleId]}>
//               <div className="mt-3">
//                 {routes.map(routeId => (
//                   <div key={routeId} className="mb-2">
//                     <Button
//                       size="sm"
//                       variant={selectedRoute === routeId ? 'primary' : 'outline-primary'}
//                       onClick={() => handleRouteClick(schedule.scheduleId, routeId, schedule.zoneId)}
//                     >
//                       Route {routeId}
//                     </Button>
//                   </div>
//                 ))}

//                 {selectedRoute && (
//                   <div className="mt-4 p-3 border rounded bg-white">
//                     <strong>Route {selectedRoute}</strong>

//                     <div className="mt-3">
//                       <Button
//                         size="sm"
//                         className="me-2"
//                         onClick={() => handleUIState(routeKey, 'showDriver')}
//                       >
//                         Add Driver
//                       </Button>
//                       <Button
//                         size="sm"
//                         onClick={() => handleUIState(routeKey, 'showWorker')}
//                       >
//                         Add Worker
//                       </Button>
//                     </div>

//                     {ui.showDriver && (
//                       <>
//                         <h6 className="mt-3">Driver</h6>
//                         {pool.drivers?.map(d => {
//   const isSelected = currentAssignment?.driver?.workerId === d.workerId;
//   return (
//     <div key={d.workerId} className="d-flex align-items-center gap-3 mb-2">
//       <span>{d.name}</span>
//       <Form.Select
//         style={{ width: 160 }}
//         value={currentAssignment?.driver?.workerId === d.workerId ? currentAssignment.driver.shift : ''}
//         onChange={(e) => {
//           const shift = e.target.value;
//           handleAssignSelect(routeKey, 'DRIVER', d.workerId, shift);
//         }}
//       >
//         <option value="">Select Shift</option>
//         {shiftOptions.map(opt => (
//           <option key={opt}>{opt}</option>
//         ))}
//       </Form.Select>
//       <Button
//         size="sm"
//         variant={isSelected ? 'success' : 'outline-success'}
//         disabled={!currentAssignment?.driver?.shift || currentAssignment?.driver?.workerId !== d.workerId}
//         onClick={() =>
//           handleAssignSelect(routeKey, 'DRIVER', d.workerId, currentAssignment.driver.shift)
//         }
//       >
//         {isSelected ? '✔ Assigned' : 'Assign'}
//       </Button>
//     </div>
//   );
// })}

//                       </>
//                     )}

//                     {ui.showWorker && (
//                       <>
//                         <h6 className="mt-3">Workers</h6>
//                         {pool.workers?.map(w => {
//   const isSelected = currentAssignment?.workers?.some(aw => aw.workerId === w.workerId);
//   const selectedShift = currentAssignment?.workers?.find(aw => aw.workerId === w.workerId)?.shift || '';

//   return (
//     <div key={w.workerId} className="d-flex align-items-center gap-3 mb-2">
//       <span>{w.name}</span>
//       <Form.Select
//         style={{ width: 160 }}
//         value={selectedShift}
//         onChange={(e) =>
//           handleAssignSelect(routeKey, 'GARBAGECOLLECTOR', w.workerId, e.target.value)
//         }
//       >
//         <option value="">Select Shift</option>
//         {shiftOptions.map(opt => (
//           <option key={opt}>{opt}</option>
//         ))}
//       </Form.Select>
//       <Button
//         size="sm"
//         variant={isSelected ? 'success' : 'outline-success'}
//         disabled={!selectedShift}
//         onClick={() =>
//           handleAssignSelect(routeKey, 'GARBAGECOLLECTOR', w.workerId, selectedShift)
//         }
//       >
//         {isSelected ? '✔ Assigned' : 'Assign'}
//       </Button>
//     </div>
//   );
// })}

//                       </>
//                     )}

//                     <Button
//                       className="mt-3"
//                       variant="success"
//                       onClick={() => handleSaveAssignment(schedule, selectedRoute, selectedVehicle)}
//                     >
//                       💾 Save Assignment
//                     </Button>
//                   </div>
//                 )}

//                 {isAllRoutesAssigned(schedule.scheduleId) && (
//                   <Button
//                     variant="primary"
//                     className="mt-3"
//                     onClick={() => handleComplete(schedule.zoneId)}
//                   >
//                     ✅ Complete Schedule
//                   </Button>
//                 )}
//               </div>
//             </Collapse>
//           </div>
//         );
//       })}

// <hr />
//       <h4>Existing Assignments</h4>
//       {assignments.length === 0 ? (
//         <p>No assignments yet.</p>
//       ) : (
//         <div className="row">
//           {assignments.map((a, index) => (
//             <div key={`${a.vehicleNumber}-${index}`} className="col-md-4 mb-3">
//               <div className="p-3 bg-white border rounded shadow-sm">
//                 <strong>Vehicle:</strong> {a.vehicleNumber}<br />
//                 <strong>Driver:</strong> {a.driverId}<br />
//                 <strong>Workers:</strong> {a.workerIds?.join(', ') || 'None'}<br />
//                 <strong>Route:</strong> {a.routeId}<br />
//                 <strong>Zone:</strong> {a.zoneId}
//               </div>
//             </div>
//           ))}
//         </div>
//       )}
//     </div>
//   );
// };

// export default ScheduleAssignment;


import React, { useEffect, useState } from 'react';
import {
  getSchedules,
  getUnassignedVehiclesByZone,
  getWorkersByRoleForRoute,
  assignWorkersToVehicle,
  notifyAllSchedule,
  updateAssignment,
  getAllAssignments,
  deleteAssignment
} from '../api/workerassignmentApi';
import { Button, Collapse, Form } from 'react-bootstrap';
import AdminHeader from '../components/AdminHeader';
import AdminSidebar from '../components/AdminSidebar';
import { toast } from 'react-toastify';
 
const shiftOptions = ['MORNING', 'EVENING'];
 
const ScheduleAssignment = () => {
  const [schedules, setSchedules] = useState([]);
  const [vehiclesBySchedule, setVehiclesBySchedule] = useState({});
  const [expandedSchedules, setExpandedSchedules] = useState({});
  const [selectedRouteBySchedule, setSelectedRouteBySchedule] = useState({});
  const [workersByRoute, setWorkersByRoute] = useState({});
  const [uiState, setUiState] = useState({});
  const [assignments, setAssignments] = useState([]);
  const [routeAssignments, setRouteAssignments] = useState({});
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [selectedWorkerId, setSelectedWorkerId] = useState('');
  const [selectedAssignmentId, setSelectedAssignmentId] = useState('');
  const [selectedShiftTimings, setSelectedShiftTimings] = useState('');
  const [showUpdateForm, setShowUpdateForm] = useState(false);
 
  useEffect(() => {
    fetchSchedules();
    fetchAssignments();
  }, []);
    const fetchSchedules = async () => {
    const data = await getSchedules();
    setSchedules(data);
  };
 
  const fetchAssignments = async () => {
    const data = await getAllAssignments();

    console.log("Assignments fetched:", data);
    setAssignments(data);
  };
 
  const handleExpandSchedule = async (schedule) => {
    setExpandedSchedules((prev) => ({
      ...prev,
      [schedule.scheduleId]: !prev[schedule.scheduleId]
    }));
 
    if (!vehiclesBySchedule[schedule.scheduleId]) {
      const vehicles = await getUnassignedVehiclesByZone(schedule.zoneId);
      setVehiclesBySchedule((prev) => ({ ...prev, [schedule.scheduleId]: vehicles }));
    }
  };
 
  const handleRouteClick = async (scheduleId, routeId, zoneId) => {
    setSelectedRouteBySchedule((prev) => ({ ...prev, [scheduleId]: routeId }));
    const key = `${scheduleId}-${routeId}`;
    if (!workersByRoute[key]) {
      const [drivers, workers] = await Promise.all([
        getWorkersByRoleForRoute(zoneId, routeId, 'DRIVER'),
        getWorkersByRoleForRoute(zoneId, routeId, 'GARBAGECOLLECTOR')
      ]);
      setWorkersByRoute((prev) => ({ ...prev, [key]: { drivers, workers } }));
    }
  };
 
  const handleAssignSelect = (routeKey, role, workerId, shift) => {
    setRouteAssignments((prev) => {
      const existing = prev[routeKey] || { driver: null, workers: [] };
      if (role === 'DRIVER') {
        return { ...prev, [routeKey]: { ...existing, driver: { workerId, shift } } };
      } else {
        const others = existing.workers.filter(w => w.workerId !== workerId);
        return {
          ...prev,
          [routeKey]: {
            ...existing,
            workers: [...others, { workerId, shift }]
          }
        };
      }
    });
  };
 
  const handleUIState = (routeKey, toggleKey) => {
    setUiState((prev) => ({
      ...prev,
      [routeKey]: {
        ...prev[routeKey],
        [toggleKey]: !prev[routeKey]?.[toggleKey]
      }
    }));
  };
 
  const handleSaveAssignment = async (schedule, routeId, vehicle) => {
    const routeKey = `${schedule.scheduleId}-${routeId}`;
    const current = routeAssignments[routeKey];
    if (!current?.driver) return alert('Assign driver before saving.');
 
    const shiftMap = {
      [current.driver.workerId]: current.driver.shift,
      ...Object.fromEntries(current.workers.map(w => [w.workerId, w.shift]))
    };
 
    await assignWorkersToVehicle(schedule.zoneId, routeId, {
      vehicleId: vehicle.vehicleId,
      vehicleNumber: vehicle.vehicleNumber,
      driverId: current.driver.workerId,
      workerIds: current.workers.map(w => w.workerId),
      shiftTimeByWorker: shiftMap
    });
 
    // alert(`✅ Assigned route ${routeId}`);
    toast.success(`Assigned route ${routeId} successfully`);
    fetchAssignments();
    fetchSchedules();
  };
 
  const isAllRoutesAssigned = (scheduleId) => {
    const vehicles = vehiclesBySchedule[scheduleId] || [];
    const routeIds = [...new Set(vehicles.map(v => v.routeId))];
    return routeIds.every(routeId => {
      const key = `${scheduleId}-${routeId}`;
      return routeAssignments[key]?.driver;
    });
  };
 
  const handleComplete = async (zoneId) => {
    await notifyAllSchedule(zoneId);
    // alert(`✅ Schedule completed for zone ${zoneId}`);
    toast.success(`worker assignment completed for zone ${zoneId}`);
  };
 
  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };
 
  const handleDeleteAssignment = async (assignmentId) => {
    // Implement delete logic here
    //alert(`Delete assignment with ID: ${assignmentId}`);
  };
 
  const handleUpdateAssignment = async (assignmentId) => {
    // Implement update logic here
    
  };
 
  return (
    <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
      <AdminHeader onToggleSidebar={toggleSidebar} />
      <AdminSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
      <div className="dashboard-content">
        <div className="p-4">
          <h3>Schedule Assignment</h3>
 
          {schedules.map(schedule => {
            console.log("Schedule-",schedule);
            
            const vehicles = vehiclesBySchedule[schedule.scheduleId] || [];
            const routes = [...new Set(vehicles.map(v => v.routeId))];
            const selectedRoute = selectedRouteBySchedule[schedule.scheduleId];
            const selectedVehicle = vehicles.find(v => v.routeId === selectedRoute);
            const routeKey = `${schedule.scheduleId}-${selectedRoute}`;
            const pool = workersByRoute[routeKey] || {};
            const ui = uiState[routeKey] || {};
            const currentAssignment = routeAssignments[routeKey] || {};
 
            return (
              <div key={schedule.scheduleId} className="border rounded p-3 bg-light mb-4">
                <div className="d-flex justify-content-between align-items-center">
                  <strong>{schedule.zoneName} | {schedule.timeSlot}</strong>
                  <Button size="sm" onClick={() => handleExpandSchedule(schedule)}>
                    {expandedSchedules[schedule.scheduleId] ? 'Hide' : '➕ Show Routes'}
                  </Button>
                </div>
 
                <Collapse in={expandedSchedules[schedule.scheduleId]}>
                  <div className="mt-3">
                    {routes.map(routeId => (
                      <div key={routeId} className="mb-2">
                        <Button
                          size="sm"
                          variant={selectedRoute === routeId ? 'primary' : 'outline-primary'}
                          onClick={() => handleRouteClick(schedule.scheduleId, routeId, schedule.zoneId)}
                        >
                          Route {routeId}
                        </Button>
                      </div>
                    ))}
 
                    {selectedRoute && (
                      <div className="mt-4 p-3 border rounded bg-white">
                        <strong>Route {selectedRoute}</strong>
 
                        <div className="mt-3">
                          <Button
                            size="sm"
                            className="me-2"
                            onClick={() => handleUIState(routeKey, 'showDriver')}
                          >
                            Add Driver
                          </Button>
                          <Button
                            size="sm"
                            onClick={() => handleUIState(routeKey, 'showWorker')}
                          >
                            Add Worker
                          </Button>
                        </div>
 
                        {ui.showDriver && (
                          <>
                            <h6 className="mt-3">Driver</h6>
                            {pool.drivers?.map(d => {
  const isSelected = currentAssignment?.driver?.workerId === d.workerId;
  return (
    <div key={d.workerId} className="d-flex align-items-center gap-3 mb-2">
      <span>{d.name}</span>
      <Form.Select
        style={{ width: 160 }}
        value={currentAssignment?.driver?.workerId === d.workerId ? currentAssignment.driver.shift : ''}
        onChange={(e) => {
          const shift = e.target.value;
          handleAssignSelect(routeKey, 'DRIVER', d.workerId, shift);
        }}
      >
        <option value="">Select Shift</option>
        {shiftOptions.map(opt => (
          <option key={opt}>{opt}</option>
        ))}
      </Form.Select>
      <Button
        size="sm"
        variant={isSelected ? 'success' : 'outline-success'}
        disabled={!currentAssignment?.driver?.shift || currentAssignment?.driver?.workerId !== d.workerId}
        onClick={() =>
          handleAssignSelect(routeKey, 'DRIVER', d.workerId, currentAssignment.driver.shift)
        }
      >
        {isSelected ? '✔ Assigned' : 'Assign'}
      </Button>
    </div>
  );
})}
 
                          </>
                        )}
 
                        {ui.showWorker && (
                          <>
                            <h6 className="mt-3">Workers</h6>
                            {pool.workers?.map(w => {
  const isSelected = currentAssignment?.workers?.some(aw => aw.workerId === w.workerId);
  const selectedShift = currentAssignment?.workers?.find(aw => aw.workerId === w.workerId)?.shift || '';
 
  return (
    <div key={w.workerId} className="d-flex align-items-center gap-3 mb-2">
      <span>{w.name}</span>
      <Form.Select
        style={{ width: 160 }}
        value={selectedShift}
        onChange={(e) =>
          handleAssignSelect(routeKey, 'GARBAGECOLLECTOR', w.workerId, e.target.value)
        }
      >
        <option value="">Select Shift</option>
        {shiftOptions.map(opt => (
          <option key={opt}>{opt}</option>
        ))}
      </Form.Select>
      <Button
        size="sm"
        variant={isSelected ? 'success' : 'outline-success'}
        disabled={!selectedShift}
        onClick={() =>
          handleAssignSelect(routeKey, 'GARBAGECOLLECTOR', w.workerId, selectedShift)
        }
      >
        {isSelected ? '✔ Assigned' : 'Assign'}
      </Button>
    </div>
  );
})}
                          </>
                        )}
 
                        <Button
                          className="mt-3"
                          variant="success"
                          onClick={() => handleSaveAssignment(schedule, selectedRoute, selectedVehicle)}
                        >
                          💾 Save Assignment
                        </Button>
                      </div>
                    )}
 
                    {isAllRoutesAssigned(schedule.scheduleId) && (
                      <Button
                        variant="primary"
                        className="mt-3"
                        onClick={async () => {
                          await notifyAllSchedule(schedule.zoneId);
                          //alert(`✅ Schedule completed for zone ${schedule.zoneId}`);
                          fetchSchedules();
                        }}
                      >
                        ✅ Complete Schedule
                      </Button>
                    )}
                  </div>
                </Collapse>
              </div>
            );
          })}

          {showUpdateForm && (
          <div className="update-assignment-form mb-4">
            <h4>Update Assignment</h4>
            <Form>
              <Form.Group controlId="workerId" className="mt-3">
                <Form.Label style={{ display: 'block', textAlign: 'left', marginBottom: '5px' }}>Worker ID</Form.Label>
                <Form.Control type="text" value={selectedWorkerId} readOnly style={{ width: '100%', marginBottom: '10px' }} />
              </Form.Group>

              <Form.Group controlId="assignmentId" className="mt-3">
                <Form.Label style={{ display: 'block', textAlign: 'left', marginBottom: '5px' }}>Assignment ID</Form.Label>
                <Form.Control type="text" value={selectedAssignmentId} readOnly style={{ width: '100%', marginBottom: '10px' }} />
              </Form.Group>

              <Form.Group controlId="shiftTimings" className="mt-3">
                <Form.Label style={{ display: 'block', textAlign: 'left', marginBottom: '5px' }}>Shift Timings</Form.Label>
                <Form.Select
                  value={selectedShiftTimings}
                  onChange={(e) => setSelectedShiftTimings(e.target.value)}
                  style={{ width: '100%', marginBottom: '10px' }}
                >
                  <option value="">Select Shift</option>
                  {shiftOptions.map((shift) => (
                    <option key={shift} value={shift}>{shift}</option>
                  ))}
                </Form.Select>
              </Form.Group>

              <div className="mt-3" style={{ display: 'flex', justifyContent: 'space-between' }}>
                <Button
                  variant="primary"
                  size="lg"
                  onClick={async () => {
                    const data = {
                      workerId: selectedWorkerId,
                      shiftTime: selectedShiftTimings
                    };
                    await updateAssignment(selectedAssignmentId, data);
                    fetchAssignments();
                    setShowUpdateForm(false);
                  }}
                  style={{ flex: 1, marginRight: '10px' }}
                >
                  <i className="fas fa-save" title="Save"></i>
                </Button>
                <Button
                  variant="secondary"
                  size="lg"
                  onClick={() => setShowUpdateForm(false)}
                  style={{ flex: 1 }}
                >
                  <i className="fas fa-times" title="Cancel"></i>
                </Button>
              </div>
            </Form>
          </div>
          )}

        <h4>Existing Assignments</h4>
          {assignments.length === 0 ? (
            <p>No assignments yet.</p>
          ) : (
            <div className="row">
              {assignments.map((a, index) => (
                console.log("Workerdata", a),
                
                <div key={`${a.vehicleId}-${index}`} className="col-md-4 mb-3">
                  <div className="p-3 bg-white border rounded shadow-sm">
                  <strong>AssignmentID:</strong> {a.assignmentId}<br />
                    {/* <strong>Vehicle:</strong> {a.vehicleId}<br /> */}
                    <strong>ZoneId:</strong> {a.zoneId}<br />
                    <strong>WorkerId:</strong> {a.workerId}<br />
                    <strong>ShiftTimings:</strong> {a.shiftTime}<br />
                    <Button
                      size="sm"
                      variant="danger"
                      onClick={async () => {
                        await deleteAssignment(a.assignmentId);
                        fetchAssignments();
                      }}
                      className="action-icon delete-icon"
                    >
                      <i className="fas fa-trash-alt" title="Delete Assignment"></i>
                    </Button>

                    <Button
                      size="sm"
                      variant="warning"
                      onClick={() => {setShowUpdateForm(true);handleUpdateAssignment(a.assignmentId,a.workerId,a.shiftTime); setSelectedWorkerId(a.workerId); setSelectedAssignmentId(a.assignmentId); setSelectedShiftTimings(a.shiftTime);}
              }
                      className="action-icon edit-icon ms-2"
                    >
                      <i className="fas fa-edit" title="Update Assignment"></i>
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

      </div>
    </div>
  );
};
 
export default ScheduleAssignment;

