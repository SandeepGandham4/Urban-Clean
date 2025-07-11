import axios from 'axios';
import { toast } from 'react-toastify';

const API_URL = 'http://localhost:8087/api/urbanclean/v1/admin/vehicles';

const getAuthHeaders = () => {
  const token = localStorage.getItem('authToken');
  return {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };
};

// Create a new vehicle
export const createVehicle = async (vehicleData) => {
    try{
  const response = await axios.post(API_URL, vehicleData, getAuthHeaders());
  return response.data;
} catch (error) {
    throw error.response.data;
}
};

export const getUnassignedVehicles= async ()=>{
  try{
    const response=await axios.get(`${API_URL}/unassignedvehicles`,getAuthHeaders());
    return response.data;
  }catch(error){
    throw error.response.data;
  }
}
// Get all vehicles
export const getVehicles = async () => {
    try{
  const response = await axios.get(API_URL, getAuthHeaders());
  return response.data;
} catch (error) {
  if (error.response?.status === 404) {
          toast.info("No vehicles found.",{
              toastId: "no-vehicles"
            });
          return [];
        }
    throw error.response.data;
}
};

// Get a vehicle by ID
export const getVehicleById = async (id) => {
  const response = await axios.get(`${API_URL}/${id}`, getAuthHeaders());
  return response.data;
};

// Get vehicle status by ID
export const getVehicleStatus = async (id) => {
  const response = await axios.get(`${API_URL}/${id}/status`, getAuthHeaders());
  return response.data;
};

// Update vehicle by ID
export const updateVehicle = async (id, vehicleData) => {
  try {
    console.log("vehicledata",vehicleData);
  const response = await axios.put(`${API_URL}/${id}`, vehicleData, getAuthHeaders());
  return response.data;
  }catch (error) {
    throw error.response.data;
  }
};

// Update vehicle status/location by ID
export const updateVehicleStatus = async (id, statusData) => {
  const response = await axios.put(`${API_URL}/${id}/status`, statusData, getAuthHeaders());
  return response.data;
};

// Delete or transfer-and-delete a vehicle
export const transferAndDeleteVehicle = async (id, toVehicleId = null) => {
  const params = toVehicleId ? { toVehicleId } : {};
  const response = await axios.delete(`${API_URL}/${id}/transfer`, {
    ...getAuthHeaders(),
    params,
  });
  return response.data;
};

export const getActiveVehicleCount = async () => {
  try {
    const response = await axios.get(`${API_URL}/activestatus`, getAuthHeaders());
    return response.data; // Should be a Long (number)
  } catch (error) {
    throw error.response?.data || "Error fetching active vehicle count";
  }
};

export const deleteVehicle = async (vehicleId) => {
  try {
    const response = await axios.delete(`${API_URL}/${vehicleId}`, getAuthHeaders());
    return { success: true, message: response.data };
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data);
    }
    throw new Error('Failed to delete vehicle');
  }
};
 
 