import axios from 'axios';

const API_URL = 'http://localhost:8087/api/v1/urbanclean/admin/zones';

const getAuthHeaders = () => {
    const token = localStorage.getItem("authToken");
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  };

export const getRoutes = async (zoneId) => {
    try {
        const response = await axios.get(`${API_URL}/${zoneId}/routes`,getAuthHeaders());
        return response.data;
    } catch (error) {
        // if (error.response?.status === 404) {
        //     //console.warn("No routes found for this zone.");
        //     //return []; 
        //   }
        throw error;
    }
};

export const createRoute = async (zoneId, routeData) => {
    try {
        const response = await axios.post(`${API_URL}/${zoneId}/routes`, routeData,getAuthHeaders());
        return response.data;
    } catch (error) {
        
        throw error;
    }
};

export const updateRoute = async (zoneId, routeId, routeData) => {
    try {
        const response = await axios.put(`${API_URL}/${zoneId}/routes/${routeId}`, routeData,getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const deleteRoute = async (zoneId, routeId) => {
    try {
        const response = await axios.delete(`${API_URL}/${zoneId}/routes/${routeId}`,getAuthHeaders());
        return response.data;

    } catch (error) {
        console.log(error.response);
        
        throw error;
    }
};