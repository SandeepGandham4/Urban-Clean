// import React, { useEffect, useState } from 'react';
// import { Container, Table, Button } from 'react-bootstrap';
// import { getWorkerAssignments } from '../api/workerApi';

// const WorkerDashboard = () => {
//     const [assignments, setAssignments] = useState([]);

//     useEffect(() => {
//         const fetchAssignments = async () => {
//             const data = await getWorkerAssignments();
//             setAssignments(data);
//         };

//         fetchAssignments();
//     }, []);

//     return (
//         <Container>
//             <h1>Worker Dashboard</h1>
//             <Table striped bordered hover>
//                 <thead>
//                     <tr>
//                         <th>Zone</th>
//                         <th>Route</th>
//                         <th>Pickup Time</th>
//                         <th>Vehicle</th>
//                         <th>Actions</th>
//                     </tr>
//                 </thead>
//                 <tbody>
//                     {assignments.map((assignment) => (
//                         <tr key={assignment.id}>
//                             <td>{assignment.zone}</td>
//                             <td>{assignment.route}</td>
//                             <td>{assignment.pickupTime}</td>
//                             <td>{assignment.vehicle}</td>
//                             <td>
//                                 <Button variant="primary">View Details</Button>
//                             </td>
//                         </tr>
//                     ))}
//                 </tbody>
//             </Table>
//         </Container>
//     );
// };

// export default WorkerDashboard;



// import React, { useEffect, useState } from 'react';
// import { Container, Table, Button } from 'react-bootstrap';
// import {  getAllWorkers } from '../api/workerApi';

// const WorkerDashboard = () => {
//     const [assignments, setAssignments] = useState([]);
//     const [workers, setWorkers] = useState([]);
//     const [selectedWorkerId, setSelectedWorkerId] = useState(null);

//     useEffect(() => {
//         fetchWorkers();
//     }, []);

//     useEffect(() => {
//         if (selectedWorkerId) {
//             fetchAssignments(selectedWorkerId);
//         }
//     }, [selectedWorkerId]);

//     const fetchWorkers = async () => {
//         try {
//             const data = await getAllWorkers();
//             setWorkers(data);
//         } catch (error) {
//             console.error('Error fetching workers:', error);
//         }
//     };

//     const fetchAssignments = async (workerId) => {
//         try {
//             const data = await getWorkerAssignments(workerId);
//             setAssignments(data);
//         } catch (error) {
//             console.error('Error fetching assignments:', error);
//         }
//     };

//     const handleWorkerSelection = (workerId) => {
//         setSelectedWorkerId(workerId);
//     };

//     return (
//         <Container>
//             <h1>Worker Dashboard</h1>
//             <h2>Select a Worker</h2>
//             <Table striped bordered hover>
//                 <thead>
//                     <tr>
//                         <th>Worker Name</th>
//                         <th>Role</th>
//                         <th>Actions</th>
//                     </tr>
//                 </thead>
//                 <tbody>
//                     {workers.map((worker) => (
//                         <tr key={worker.id}>
//                             <td>{worker.name}</td>
//                             <td>{worker.role}</td>
//                             <td>
//                                 <Button variant="primary" onClick={() => handleWorkerSelection(worker.id)}>
//                                     View Assignments
//                                 </Button>
//                             </td>
//                         </tr>
//                     ))}
//                 </tbody>
//             </Table>

//             {selectedWorkerId && (
//                 <>
//                     <h2>Assignments for Worker ID: {selectedWorkerId}</h2>
//                     <Table striped bordered hover>
//                         <thead>
//                             <tr>
//                                 <th>Zone</th>
//                                 <th>Route</th>
//                                 <th>Pickup Time</th>
//                                 <th>Vehicle</th>
//                             </tr>
//                         </thead>
//                         <tbody>
//                             {assignments.map((assignment) => (
//                                 <tr key={assignment.id}>
//                                     <td>{assignment.zone}</td>
//                                     <td>{assignment.route}</td>
//                                     <td>{assignment.pickupTime}</td>
//                                     <td>{assignment.vehicle}</td>
//                                 </tr>
//                             ))}
//                         </tbody>
//                     </Table>
//                 </>
//             )}
//         </Container>
//     );
// };

// export default WorkerDashboard;