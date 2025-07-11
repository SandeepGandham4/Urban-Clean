import axios from 'axios';
import { toast } from 'react-toastify';

const API_URL = 'http://localhost:8087/api/urbanclean/v1/admin/workers';

const getAuthHeaders = () => {
    const token = localStorage.getItem("authToken");
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  };
// Fetch all workers
export const getAllWorkers = async () => {
    try {
        const response = await axios.get(API_URL, getAuthHeaders());
        return response.data;
    } catch (error) {
        if (error.response?.status === 404) {
                  toast.info("No workers found.",{
                      toastId: "no-workers"
                    });
                  return [];
                }
        throw error;
    }
};

// Fetch a worker by ID
export const getWorkerById = async (id) => {
    try {
        const response = await axios.get(`${API_URL}/${id}`, getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error;
    }
};

// Create a new worker
export const createWorker = async (workerData) => {
    try {
        console.log("worker",workerData);
        const response = await axios.post(API_URL, workerData, getAuthHeaders());
        console.log(response.data);
        
        return response.data;
    } catch (error) {
        throw error;
    }
};

// Update an existing worker
export const updateWorker = async (id, workerData) => {
    try {
        console.log("Update",id,workerData);
        
        const response = await axios.put(`${API_URL}/${id}`, workerData, getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error;
    }
};

// Delete a worker by ID
export const deleteWorker = async (id) => {
    try {
        const response = await axios.delete(`${API_URL}/${id}`, getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error;
    }
};

// Fetch workers by name
export const getWorkerByName = async (name) => {
    try {
        const response = await axios.get(`${API_URL}/name/${name}`, getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error;
    }
};

// Fetch workers by role
export const getWorkersByRole = async (role) => {
    try {
        const response = await axios.get(`${API_URL}/role/${role}`, getAuthHeaders());
        return response.data;
    } catch (error) {
        throw error;
    }
};