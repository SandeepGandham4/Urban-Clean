// import axios from 'axios';
 
// const API_BASE_URL = 'http://localhost:8086/api/v1/urbanclean/worker'; // Your Spring Boot API base URL
 
 
// const getAuthHeaders = () => {
//     const token = localStorage.getItem("authToken");
//     return {
//       headers: {
//         Authorization: `Bearer ${token}`,
//       },
//     };
//   };
// const workerApi = axios.create({
//     baseURL: API_BASE_URL,
//     headers: {
//         'Content-Type': 'application/json',
//     },
// });
// // --- No interceptors for Authorization header, as requested ---
 
// // --- SIMULATED MICROSERVICE CALL for Worker Assignment ---
// // In a real scenario, this would be an actual call to your separate microservice,
// // e.g., axios.get('http://your-assignment-microservice/api/worker-assignments/me')
// // which would return the worker's assigned IDs for the current user/session.
 
// // export const fetchWorkerId = async =(email) => {
// //     const response = await workerApi.get(`/worker-id`, getAuthHeaders());
// //     return response.data;
 
// export const fetchWorkerAssignmentDetails = async (workerId) => {
//     try{    
//     const response=await workerApi.get(`/${workerId}`, getAuthHeaders());
//     return response.data;
//     }catch (error) {
//         console.error(`Error fetching worker assignment details for worker ID ${workerId}:`, error.response?.data || error.message);
//         throw error;
//     }
// };
// export const fetchWorkerIdByEmail = async (emailId) => {
//     try {
//       console.log(`Fetching worker ID for email: ${emailId}`);
      
//       const response = await workerApi.get(`/email`, {
//         params: { email: emailId },
//         ...getAuthHeaders(),
//       });
//       console.log(`Worker ID for ${emailId}:`, response.data);
//       return response.data;
//     } catch (error) {
//       console.error(`Error fetching worker ID for ${emailId}:`, error.response?.data || error.message);
//       throw error;
//     }
//   };
  
  
 
 
// // --- Worker-Specific API Functions ---
 
// // @PutMapping("/waste-logs/{workerId}")
// export const enterLogData = (workerId, weightCollected, collectionTime) =>{
//     console.log(`Entering log data for worker ${workerId} with weight ${weightCollected} at ${collectionTime}`) ;
   
//     workerApi.put(`/waste-logs/${workerId}`, null, {
//         params: { weightCollected, collectionTime },
//         ...getAuthHeaders()});
// }
 
// // @GetMapping("/reports/worker/{id}")
// export const getWasteCollectionByWorkerId = (workerId, params) =>
//     workerApi.get(`/reports/${workerId}`, { params ,...getAuthHeaders()});
 
// // @GetMapping("/reports/waste-collection/{w_id}/pdf")
// export const downloadWasteCollectionReportByWorkerPdf = async (workerId, params) => {
//     const response = await workerApi.get(`/reports/waste-collection/${workerId}/pdf`, {
//         params,
//         responseType: 'blob', // Important for binary data like PDF
//         ...getAuthHeaders()});
//     return response.data;
// };
 
// export const updateVehicleLiveStatus = async (vehicleId) => {
//     try {
//         const response = await workerApi.post(`/vehicle/${vehicleId}/updatestatus`, null, getAuthHeaders());
//          console.log(response.data);
       
//         return response.data;
//     } catch (error) {
//         console.error(`Error updating vehicle live status for vehicle ID ${vehicleId}:`, error.response?.data || error.message);
//         throw error;
//     }
// };
 
// export default workerApi;
 
import axios from 'axios';
 
const API_BASE_URL = 'http://localhost:8087/api/v1/urbanclean/wastelog/worker'; // Your Spring Boot API base URL
 
 
const getAuthHeaders = () => {
    const token = localStorage.getItem("authToken");
    return {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
  };
const workerApi = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});
 
export const fetchWorkerAssignmentDetails = async (workerId) => {
    try{    
    const response=await workerApi.get(`/${workerId}`, getAuthHeaders());
    return response.data;
    }catch (error) {
        console.error(`Error fetching worker assignment details for worker ID ${workerId}:`, error.response?.data || error.message);
        throw error;
    }
};
export const fetchWorkerIdByEmail = async (emailId) => {
    try {
      const response = await workerApi.get(`/email`, {
        params: { email: emailId },
        ...getAuthHeaders(),
      });
      console.log(`Worker ID for ${emailId}:`, response.data);
      return response.data;
    } catch (error) {
      console.error(`Error fetching worker ID for ${emailId}:`, error.response?.data || error.message);
      throw error;
    }
  };
 
export const enterLogData = (workerId, weightCollected, collectionTime) =>{
    console.log(`Entering log data for worker ${workerId} with weight ${weightCollected} at ${collectionTime}`) ;
   
    workerApi.put(`/waste-logs/${workerId}`, null, {
        params: { weightCollected, collectionTime },
        ...getAuthHeaders()});
}

export const getWasteCollectionByWorkerId = (workerId, params) =>
    workerApi.get(`/reports/${workerId}`, { params ,...getAuthHeaders()});
 
export const downloadWasteCollectionReportByWorkerPdf = async (workerId, params) => {
    const response = await workerApi.get(`/reports/waste-collection/${workerId}/pdf`, {
        params,
        responseType: 'blob', 
        ...getAuthHeaders()});
    return response.data;
};
 
export const updateVehicleLiveStatus = async (vehicleId) => {
    try {
        const response = await workerApi.post(`/vehicle/${vehicleId}/updatestatus`, null, getAuthHeaders());
         console.log(response.data);
       
        return response.data;
    } catch (error) {
        console.error(`Error updating vehicle live status for vehicle ID ${vehicleId}:`, error.response?.data || error.message);
        throw error;
    }
};

export const getVehicleStatus = async (vehicleId) => {
  try {
      const response = await workerApi.get(`/vehicle/${vehicleId}/getstatus`, getAuthHeaders());
      console.log(`Vehicle status for ID ${vehicleId}:`, response.data);
      return response.data;
  } catch (error) {
      console.error(`Error fetching vehicle status for ID ${vehicleId}:`, error.response?.data || error.message);
      throw error;
  }
};
 
 
export default workerApi;
 
 
 