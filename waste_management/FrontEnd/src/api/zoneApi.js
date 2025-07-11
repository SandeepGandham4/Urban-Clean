import axios from 'axios';
import { toast } from 'react-toastify';

const API_URL = 'http://localhost:8087/api/v1/urbanclean/admin/zones';

const getAuthHeaders = () => {
    const token = localStorage.getItem("authToken");
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  };

 
export const getZones = async () => {
    try {
      const response = await axios.get(
        'http://localhost:8087/api/v1/urbanclean/admin/zones/allzones',
        getAuthHeaders()
      );
      return response.data;
    } catch (error) {
      if (error.response?.status === 404) {
        toast.info("No zones found.",{
            toastId: "no-zones"
          });
        return [];
      }
  
      toast.error("Failed to fetch zones.");
      throw error;
    }
  };
  

export const createZone = async (zoneData) => {
    try {
      const response = await axios.post(API_URL, zoneData, getAuthHeaders());
      toast.success("Zone created successfully!");
      return response.data;
    } catch (error) {
      // Generic error toast (ignores 400-specific handling)
      toast.error("Failed to create zone");
      throw error;
    }
  };
  

export const updateZone = async (zoneId, zoneData) => {
    try {
        const response = await axios.put(`${API_URL}/${zoneId}`, zoneData,getAuthHeaders());
        toast.success("Zone updated successfully!");
        return response.data;
    } catch (error) {
        if (error.response?.status === 404) {
            toast.info("Edit the zone.",{
                toastId: "no-zones"
              });
            return [];
          }
      
          toast.error("Failed to edit zone.");
          throw error;
        }
};

export const deleteZone = async (zoneId) => {
    try {
        const response = await axios.delete(`${API_URL}/${zoneId}`,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error;
    }
};

// Fetch total number of routes
export const getNumberOfRoutes = async () => {
  try {
    const response = await axios.get(`${API_URL}/routes`, getAuthHeaders());
    return response.data; // Should return a number
  } catch (error) {
    toast.error("Unable to fetch number of routes.");
    throw error;
  }
};
