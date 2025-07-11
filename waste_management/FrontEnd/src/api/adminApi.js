// src/api/adminApi.js
import axios from 'axios'; // Import the axios library for making HTTP requests
 
// --- Configuration ---
// IMPORTANT: Update this URL to match the exact base URL of your Spring Boot backend's controller
// IF YOUR BACKEND IS RUNNING ON 8086, CHANGE THIS:
const API_BASE_URL = 'http://localhost:8087/api/v1/urbanclean/wastelog/admin'; // <--- CHANGED THIS PORT TO 8086
const getAuthHeaders = () => {
    const token = localStorage.getItem("authToken");//
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  };
// Create an Axios instance with a base URL and common headers.
// This simplifies future requests by pre-setting parts of the URL and headers.
const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json', // We're sending/receiving JSON data
        // If your admin panel requires authentication (e.g., JWT),
        // you would add an Authorization header here. For example:
        // 'Authorization': `Bearer ${localStorage.getItem('adminJwtToken')}`
    },
});
 
// --- API Functions (These remain the same as before) ---
 
/**
 * Sends a POST request to create a new waste log entry.
 * Corresponds to your backend's @PostMapping("/waste-logs") endpoint.
 * @param {object} logData - An object containing the waste log details (zoneId, vehicleId, workerId, weightCollected, collectionTime).
 * @returns {Promise<object>} A promise that resolves to the created WasteLogResponseDTO.
 */
export const createWasteLog = async (logData) => {
    try {
        // The full URL will be API_BASE_URL + '/api/urbanclean/v1/waste-logs'
        const response = await api.post('/waste-logs', logData,getAuthHeaders());
        return response.data; // Return the data from the response
    } catch (error) {
        console.error('Error creating waste log:', error.response?.data || error.message);
        throw error; // Re-throw the error for the calling component to handle
    }
};
 
/**
 * Sends a GET request to retrieve a paginated waste collection report for admin view.
 * Corresponds to your backend's @GetMapping("/reports/waste-collection") endpoint.
 * @param {object} filters - An object containing optional filter parameters (startDate, endDate, zoneId, vehicleId, workerId, page, size, sort).
 * @returns {Promise<object>} A promise that resolves to a Spring Page object containing WasteLogResponseDTOs.
 */
export const getAdminWasteCollectionReport = async (filters) => {
    try {
        const params = {};
        for (const key in filters) {
            if (filters[key] !== '' && filters[key] !== undefined && filters[key] !== null) {
                params[key] = filters[key];
            }
        }
       
        // The full URL will be API_BASE_URL + '/api/urbanclean/v1/reports/waste-collection'
        const response = await axios.get(`${API_BASE_URL}/reports/waste-collection`, { params ,...getAuthHeaders()});
        return response.data; // Returns the Page object
    } catch (error) {
        console.error(`Error fetching admin waste collection report:${error.response.data}`);
        throw error;
    }
};
 
/**
 * Sends a GET request to download a PDF report of waste collection data.
 * Corresponds to your backend's @GetMapping("/reports/waste-collection/pdf") endpoint.
 * @param {object} filters - An object containing optional filter parameters (startDate, endDate, zoneId, vehicleId, workerId).
 * @returns {Promise<Blob>} A promise that resolves to a Blob object representing the PDF file.
 */
export const downloadAdminWasteCollectionReportPdf = async (filters) => {
    try {
        const params = {};
        for (const key in filters) {
            if (filters[key] !== '' && filters[key] !== undefined && filters[key] !== null) {
                params[key] = filters[key];
            }
        }
 
        // The full URL will be API_BASE_URL + '/api/urbanclean/v1/reports/waste-collection/pdf'
        const response = await api.get('/reports/waste-collection/pdf', {
            params,
            responseType: 'blob', // Crucial for downloading binary data like PDFs
            ...getAuthHeaders()});
        return response.data; // Returns the Blob object
    } catch (error) {
        console.error('Error downloading admin waste collection report PDF:', error.response?.data || error.message);
        throw error;
    }
};
 
 