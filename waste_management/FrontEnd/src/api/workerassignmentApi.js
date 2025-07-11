import axios from 'axios';

const API_URL = 'http://localhost:8087/api/urbanclean/v1/admin/workerassignments';

const getAuthHeaders = () => {
  const token = localStorage.getItem('authToken');
  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

// 1. Assign a worker to a route
export const assignWorker = async (zoneId, routeId, workerId, typeOfWorker, assignmentDTO) => {
    try{
  const url = `${API_URL}/assign/zones/${zoneId}/routes/${routeId}/worker/${workerId}?typeOfWorker=${typeOfWorker}`;
  const response = await axios.post(url, assignmentDTO, getAuthHeaders());
  return response.data;
    }catch(err){
        throw err;
    }
};

// 2. Assign multiple workers to a vehicle
export const assignWorkersToVehicle = async (zoneId, routeId, AssignmentRequestDTO) => {
    try{
        
  const url = `${API_URL}/assign/zones/${zoneId}/routes/${routeId}/save`;
  console.log("url",url,AssignmentRequestDTO);
  AssignmentRequestDTO={
    driverId : AssignmentRequestDTO.driverId,
    vehicleId:AssignmentRequestDTO.vehicleId,
    shiftTimeByWorker:AssignmentRequestDTO.shiftTimeByWorker,
    workerIds:AssignmentRequestDTO.workerIds
  }
  console.log(AssignmentRequestDTO);
  
  const response = await axios.post(url, AssignmentRequestDTO, getAuthHeaders());
  return response.data;
    }catch(err){
        throw err;
    }
};

// 3. Notify system that all workers are scheduled
export const notifyAllSchedule = async (zoneId) => {
  console.log(zoneId);
  
  const url = `${API_URL}/assign/notify/${zoneId}`;
  const response = await axios.post(url, null, getAuthHeaders());
  return response.data;
};

// 4. Get workers by role for zone and route
export const getWorkersByRoleForRoute = async (zoneId, routeId, typeOfWorker) => {
    console.log(zoneId,routeId,typeOfWorker);
    console.log("hii");
    
  const url = `${API_URL}/assign/zones/${zoneId}/routes/${routeId}/worker?typeOfWorker=${typeOfWorker}`;
  const response = await axios.get(url, getAuthHeaders());
  console.log(response);
  
  return response.data;
};

// 5. Get a specific assignment by ID
export const getAssignmentById = async (assignmentId) => {
  const response = await axios.get(`${API_URL}/${assignmentId}`, getAuthHeaders());
  return response.data;
};

// 6. Get all assignments for a worker
export const getAssignmentsByWorkerId = async (workerId) => {
  const response = await axios.get(`${API_URL}/worker/${workerId}`, getAuthHeaders());
  return response.data;
};

// 7. Get all assignments
export const getAllAssignments = async () => {
  const response = await axios.get(API_URL, getAuthHeaders());
  return response.data;
};

// 8. Get schedules missing full worker assignment
export const getSchedules = async () => {
    try{
  const response = await axios.get(`${API_URL}/schedules`, getAuthHeaders());
  return response.data;
    }catch(err){
      // console.log("hiiii");
      
        throw err;
    }
};

// 9. Update an assignment
export const updateAssignment = async (assignmentId, assignmentDTO) => {
  console.log("assignmentDTO",assignmentDTO);
  
  const response = await axios.put(`${API_URL}/${assignmentId}`, assignmentDTO, getAuthHeaders());
  return response.data;
};

// 10. Delete an assignment
export const deleteAssignment = async (assignmentId) => {
  console.log("Deleting assignment with ID:", assignmentId);
  
  const response = await axios.delete(`${API_URL}/${assignmentId}`, getAuthHeaders());
  console.log("Response after deletion:", response);
  
  return response.data;
};

// 11. Get all vehicles not yet assigned workers in a zone
export const getUnassignedVehiclesByZone = async (zoneId) => {
    try{
        // console.log(zoneId);
        
        //console.log("hiii");
        
  const response = await axios.get(`${API_URL}/assign/zones/${zoneId}`, getAuthHeaders());
  // console.log("RESPONSE",response);
  
  return response.data;
    }catch(err){   
      console.log("Error in getUnassignedVehiclesByZone", err);
        throw err;
    }
};

// 12. Delete all assignments linked to a vehicle
export const deleteAssignmentsByVehicleId = async (vehicleId) => {
  const response = await axios.delete(`${API_URL}/assignments/${vehicleId}`, getAuthHeaders());
  return response.data;
};

// 13. Delete all assignments in a zone
export const deleteAssignmentsByZoneId = async (zoneId) => {
  const response = await axios.delete(`${API_URL}/assignments/${zoneId}`, getAuthHeaders());
  return response.data;
};
