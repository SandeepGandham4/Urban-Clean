import axios from 'axios';
import { toast } from 'react-toastify';

const API_BASE = 'http://localhost:8087/api/urbanclean/v1/admin/vehicleassignments';

const getAuthHeaders = () => {
  const token = localStorage.getItem("authToken");
  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

// 1. Get all schedules with unassigned routes
export const getUnassignedSchedules = async () => {
  try {
    const response = await axios.get(`${API_BASE}/schedules`, getAuthHeaders());
    return response.data;
  } catch (error) {
      ///console.log("Error fetching unassigned schedules:", error);
      
    throw error;
}
};

// 2. Create an assignment
export const createAssignment = async (zoneId, routeId, assignmentData) => {
  try {
    console.log(zoneId,routeId,typeof(assignmentData.scheduleDate));
    const response = await axios.post(
      `${API_BASE}/assign/zones/${zoneId}/routes/${routeId}`,
      assignmentData,
      getAuthHeaders()
    );
    
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 3. Get assignments (optionally filtered by date)
export const getAssignments = async (date = null) => {
  try {
    const url = date ? `${API_BASE}?date=${date}` : `${API_BASE}`;
    const response = await axios.get(url, getAuthHeaders());
    return response.data;
  } catch (error) {
    if (error.response?.status === 500) {
      toast.info("No schedules found.", {
        toastId: "no-schedules"
      });
    throw error;
  }
}
};

// 4. Get assignment by ID
export const getAssignmentById = async (assignmentId) => {
  try {
    const response = await axios.get(`${API_BASE}/${assignmentId}`, getAuthHeaders());
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 5. Update assignment
export const updateAssignment = async (assignmentId, updatedData) => {
  try {
    console.log("updateddata",updatedData);
    
    const response = await axios.put(`${API_BASE}/${assignmentId}`, updatedData, getAuthHeaders());
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 6. Delete assignment
export const deleteAssignment = async (assignmentId) => {
  try {
    const response = await axios.delete(`${API_BASE}/${assignmentId}`, getAuthHeaders());
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 7. Get routes by zone
export const getRoutesByZone = async (zoneId) => {
  try {
    const response = await axios.get(`${API_BASE}/assign/zones/${zoneId}/routes`, getAuthHeaders());
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 8. Notify that all routes are scheduled
export const notifyAllRoutesScheduled = async (zoneId) => {
  try {
    const response = await axios.post(
      `${API_BASE}/assign/notify`,
      zoneId,
      {
        ...getAuthHeaders(),
        headers: {
          'Content-Type': 'application/json',
          ...getAuthHeaders().headers // merge auth headers here
        }
      }
    );
    return response.data;
  } catch (error) {
    console.error('403 error:', error.response?.data || error.message);
    throw error;
  }
};


// 9. Get vehicles not assigned to workers
export const getUnassignedWorkerVehicles = async () => {
  try {
    const response = await axios.get(`${API_BASE}/workerassignment`, getAuthHeaders());
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 10. Assign worker to vehicle
export const assignWorkerToVehicle = async (vehicleId) => {
  try {
    const response = await axios.post(`${API_BASE}/worker/assigned`, vehicleId, getAuthHeaders());
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 11. Notify deletion of route assignments
export const deleteAssignmentByRoute = async (routeId) => {
  try {
    const response = await axios.delete(`${API_BASE}/routes/${routeId}`, getAuthHeaders());
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 12. Notify deletion by multiple routes (zone-level bulk)
export const deleteAssignmentsByZoneRoutes = async (routeIdList) => {
  try {
    const response = await axios.delete(`${API_BASE}/routes/delete`, {
      ...getAuthHeaders(),
      data: routeIdList
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};
