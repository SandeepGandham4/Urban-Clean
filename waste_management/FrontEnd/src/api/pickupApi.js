import axios from 'axios';
import { toast } from 'react-toastify';

const API_URL = 'http://localhost:8087/api/v1/urbanclean/supervisor/schedules';

const getAuthHeaders = () => {
    const token = localStorage.getItem("authToken");
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  };

// Schedule a new pickup
export const schedulePickup = async (pickupData) => {
    try {
        const response = await axios.post(`${API_URL}/create`, pickupData,getAuthHeaders());
        console.log(response.data);
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Fetch all pickup schedules
export const getPickupSchedule = async () => {
  try {
    const response = await axios.get(API_URL, getAuthHeaders());
    return response.data;
  } catch (error) {
    if (error.response?.status === 404) {
      toast.info("No pickup schedules found.", { toastId: "no-pickups-found" });
      return [];
    }
    toast.error("Failed to fetch pickup schedules.");
    throw error;
  }
};


// Update an existing pickup schedule
export const updatePickup = async (pickupId, updatedData) => {
    try {
        console.log(`Updating pickup with ID: ${pickupId}`, updatedData);
        
        const response = await axios.put(`${API_URL}/${pickupId}`, updatedData,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Delete a pickup schedule by ID
export const deletePickup = async (pickupId) => {
    try {
        const response = await axios.delete(`${API_URL}/${pickupId}`,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Fetch pickup schedules by zone ID
export const getSchedulesByZoneId = async (zoneId) => {
    try {
        const response = await axios.get(`${API_URL}/${zoneId}`,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Delete all schedules for a specific zone
export const deleteSchedulesByZone = async (zoneId) => {
    try {
        const response = await axios.delete(`${API_URL}/zone/${zoneId}`,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Fetch schedules with routes not assigned
export const getSchedulesWithRoutesNotAssigned = async () => {
    try {
        const response = await axios.get(`${API_URL}/notassigned`,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Mark a schedule as assigned with vehicles
export const markScheduleWithVehiclesAssigned = async (scheduleId) => {
    try {
        const response = await axios.post(`${API_URL}/vechiclesassigned`, scheduleId,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Fetch schedules with workers not assigned for all routes
export const getSchedulesWithWorkersNotAssigned = async () => {
    try {
        const response = await axios.get(`${API_URL}/workersnotassigned`,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Mark a schedule as assigned with workers
export const markScheduleWithWorkersAssigned = async (scheduleId) => {
    try {
        const response = await axios.post(`${API_URL}/workersassigned`, scheduleId,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Fetch newly added zones
export const getNewlyAddedZones = async () => {
    try {
        const response = await axios.get(`${API_URL}/notify`,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response.data;
    }
};

// Assign workers to a schedule
export const assignWorkersToSchedule = async (scheduleId) => {
    try {
        await axios.post(`${API_URL}/assign/workers`, { scheduleId },getAuthHeaders());
    } catch (error) {
        throw error.response.data;
    }
};

// Assign vehicles to a schedule
export const assignVehiclesToSchedule = async (scheduleId) => {
    try {
        await axios.post(`${API_URL}/assign/vehicles`, { scheduleId },getAuthHeaders());
    } catch (error) {
        throw error.response.data;
    }
};

// Fetch total number of pickup schedules
export const getSchedulesCount = async () => {
    try {
        const response = await axios.get(`${API_URL}/schedulescount`, getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error.response?.data || "Failed to fetch schedules count";
    }
};
