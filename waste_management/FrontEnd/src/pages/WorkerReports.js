// import React, { useState, useEffect } from 'react';
// import '../styles/WorkerReports.css';
// import {
//     fetchWorkerAssignmentDetails,
//     enterLogData,
//     getWasteCollectionByWorkerId,
//     downloadWasteCollectionReportByWorkerPdf,
//     updateVehicleLiveStatus,
//     fetchWorkerIdByEmail
// } from '../api/workerReportsApi';
// import WorkerHeader from '../components/WorkerHeader';
 
// const WorkerDashboard = () => {
//     // localStorage.setItem('authToken',eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJyb2xlIjoiQWRtaW4iLCJpYXQiOjE3NTEzOTM5NTQsImV4cCI6MTc1MTM5NzU1NH0.Y_rUKzZxf_1d0u65rUpeIw8pSiv037MyZVZF7TqSoeM);
//     // State for Worker's context
//     const [workerContext, setWorkerContext] = useState(null);
//     const [loadingWorkerContext, setLoadingWorkerContext] = useState(true);
//     const [workerContextError, setWorkerContextError] = useState('');
 
//     // State for Enter Log Data Form
//     const [logDataForm, setLogDataForm] = useState({
//         weightCollected: '',
//         collectionTime: '',
//     });
//     const [enterLogMessage, setEnterLogMessage] = useState('');
//     // NEW: State to track if log data has been successfully submitted in this session
//     const [hasLoggedForSession, setHasLoggedForSession] = useState(false);
 
 
//     // State for Worker's Report Filter Form
 
//     const [workerReportData, setWorkerReportData] = useState([]);
//     const [workerReportMessage, setWorkerReportMessage] = useState('');
//     const [workerCurrentPage, setWorkerCurrentPage] = useState(0);
//     const [workerTotalPages, setWorkerTotalPages] = useState(0);
//     const [workerPageSize] = useState(10);
//     const [workerSortField] = useState('collectionTime');
//     const [vehicleStatusMessage, setVehicleStatusMessage] = useState('');
   
//     const email = localStorage.getItem("email");
//     console.log(`Email from localStorage: ${email}`);
    
//     // Effect to fetch worker assignment details on component mount
//     useEffect(() => {
//         const getWorkerContext = async () => {
//             try {
//                 setLoadingWorkerContext(true);
//                 setWorkerContextError('');  
//                 console.log(email);
                              
//                 const data1 = await fetchWorkerIdByEmail(email);
//                 console.log(`Worker ID for ${email}:`, data1);
//                 const data = await fetchWorkerAssignmentDetails(data1);
 
//                 if (!data) {
//                     setWorkerContextError("You are not assigned to any vehicle or zone. Please contact your supervisor.");
//                     //window.location.href = '/unassigned-worker'; // Redirect to UnassignedWorker page
//                     return;
//                 }
 
//                 setWorkerContext(data);
 
//                 if (data.weightCollected != null) {
//                     setHasLoggedForSession(true);
//                 }
//             } catch (error) {
//                 console.error("Error fetching worker assignment details:", error);
//                 setWorkerContextError(error.response?.data || error.message);
//                 // window.location.href = '/unassigned-worker'; // Redirect to UnassignedWorker page on error
//             } finally {
//                 setLoadingWorkerContext(false);
//             }
//         };
 
//         getWorkerContext();
//     }, []);
 
//     // Effect to refetch report when workerContext or filters change
//     useEffect(() => {
//         if (workerContext && workerContext.workerId) {
//             fetchWorkerReportData(workerCurrentPage);
//         }
//     }, [workerCurrentPage, workerContext]);
 
//     // Fetch Worker's Report Data function
//     const fetchWorkerReportData = async (page = 0) => {
//         setWorkerReportMessage('');
//         if (!workerContext?.workerId) {
//             setWorkerReportMessage('Worker ID is not available.');
//             setWorkerReportData([]);
//             return;
//         }
 
//         try {
//             const params = {
//                 page,
//                 size: workerPageSize,
//                 sort: workerSortField,
//             };
           
 
//             const response = await getWasteCollectionByWorkerId(workerContext.workerId, params);
//             setWorkerReportData(response.data.content);
//             setWorkerCurrentPage(response.data.number);
//             setWorkerTotalPages(response.data.totalPages);
 
//         } catch (error) {
//             setWorkerReportData([]);
           
//                 setWorkerReportMessage(`Error fetching your report: ${error.response?.data?.message || error.message}`);
//                 console.error('Error fetching worker report:', error.response?.data || error);
           
//         }
//     };
 
//     // Handle Enter Log Data Submission
//     const handleEnterLogDataSubmit = async (e) => {
//         e.preventDefault();
//         setEnterLogMessage('');
//         if (!workerContext?.workerId) {
//             setEnterLogMessage('Worker ID is not available. Cannot submit log data.');
//             return;
//         }
//         try {
           
//             const formattedCollectionTime = logDataForm.collectionTime;
 
//             await enterLogData(
//                 workerContext.workerId,
//                 parseFloat(logDataForm.weightCollected),
//                 formattedCollectionTime
//             );
//             setEnterLogMessage('Log data entered successfully!');
//             setLogDataForm({ weightCollected: '', collectionTime: '' });
//             setHasLoggedForSession(true);
//             fetchWorkerReportData();
//         } catch (error) {
//             setEnterLogMessage(`Error entering log data: ${error.response?.data?.message || error.message}`);
//             console.error('Error entering log data:', error.response?.data || error);
//         }
//     };
 
   
 
//     // Handle Worker's Report Actions (Generate/Download)
//     const handleWorkerReportActions = (actionType) => {
//         if (actionType === 'generate') {
         
//             setWorkerCurrentPage(0);
//             fetchWorkerReportData(0);
//         } else if (actionType === 'download') {
           
           
//             handleDownloadWorkerPdf();
//         }
//     };
 
//     // Handle Worker PDF Download
//     const handleDownloadWorkerPdf = async () => {
//         setWorkerReportMessage('');
     
//         try {
           
//             const pdfBlob = await downloadWasteCollectionReportByWorkerPdf(workerContext.workerId);
//             const url = window.URL.createObjectURL(pdfBlob);
//             const a = document.createElement('a');
//             a.href = url;
//             a.download = `Worker_${workerContext.workerId}_WasteCollectionReport.pdf`;
//             document.body.appendChild(a);
//             a.click();
//             a.remove();
//             window.URL.revokeObjectURL(url);
//             setWorkerReportMessage('PDF report downloaded successfully.');
//         } catch (error) {
//             setWorkerReportMessage(`Error downloading PDF: ${error.response?.data?.message || error.message}`);
//             console.error('Error downloading PDF:', error.response?.data || error);
//         }
//     };
 
//     const handleUpdateVehicleStatus = async () => {
//         setVehicleStatusMessage(''); // Clear previous messages
//         if (!workerContext?.vehicleId) {
//             setVehicleStatusMessage('Vehicle ID is not available. Cannot update status.');
//             return;
//         }
//         try {
//             console.log('Updating vehicle live status for Vehicle ID:', workerContext.vehicleId);
            
//             const response=await updateVehicleLiveStatus(workerContext.vehicleId);
//             console.log('Vehicle status update response:', response);
            
//             setVehicleStatusMessage(response);
//         } catch (error) {
//             setVehicleStatusMessage(`Error updating vehicle status: ${error.response?.data?.message || error.message}`);
//             console.error('Error updating vehicle status:', error.response?.data || error);
//         }
//     };
 
//     if (loadingWorkerContext) {
//         return <div className="container">Loading worker details from microservice...</div>;
//     }
 
//     if (workerContextError) {
//         return <div className="container"><p className="message error">{workerContextError}</p></div>;
//     }
 
//     if (!workerContext || !workerContext.workerId) {
//         return <div className="container"><p className="message error">Worker context not fully loaded. Cannot display dashboard.</p></div>;
//     }
 
//     return (
//         <div className="container">
//             <WorkerHeader />
//             <h1>Worker Dashboard</h1>
//             <p>
//                 Your Worker ID: <strong>{workerContext.workerId}</strong> |
//                 Zone ID: <strong>{workerContext.zoneId?workerContext.zoneId:"NA"}</strong> |
//                 Vehicle ID: <strong>{workerContext.vehicleId?workerContext.vehicleId:"NA"}</strong>
//             </p>
//             <div className="section">
               
//                 <button
//                     onClick={handleUpdateVehicleStatus}
//                     disabled={!workerContext.vehicleId} // Disable if vehicleId is not available
//                 >
//                     Update Vehicle Status
//                 </button>
//                 {vehicleStatusMessage && <div className={`message ${vehicleStatusMessage.includes('Error') ? 'error' : 'success'}`}>{vehicleStatusMessage}</div>}
//             </div>
 
//                 <div className="section">
//                 <h2>Enter Waste Log Data</h2>
//                 {hasLoggedForSession ? (
//                     <p className="message success">You have successfully logged your reports.</p>
//                 ) : (
//                     <form onSubmit={handleEnterLogDataSubmit}>
//                         <label>Your Worker ID:</label>
//                         <input
//                             type="number"
//                             value={workerContext.workerId}
//                             required
//                             disabled
//                         />
//                         <label>Weight Collected (kg):</label>
//                         <input
//                             type="int"
//                             // step="1"
//                             value={logDataForm.weightCollected}
//                             onChange={(e) => setLogDataForm({ ...logDataForm, weightCollected: e.target.value })}
//                             required
//                         />
//                         <label>Collection Time:</label>
//                         <input
//                             type="datetime-local"
//                             value={logDataForm.collectionTime}
//                             onChange={(e) => setLogDataForm({ ...logDataForm, collectionTime: e.target.value })}
//                             required
//                         />
//                         <button type="submit">Submit Log Data</button>
//                         {enterLogMessage && <div className={`message ${enterLogMessage.includes('Error') ? 'error' : 'success'}`}>{enterLogMessage}</div>}
//                     </form>
//                 )}
//             </div>
 
//             <div className="section">
//                 <h2>My Waste Collection Report</h2>
//                 <div style={{ marginBottom: '15px' }}>
//                     <button onClick={() => handleWorkerReportActions('generate')}>Generate My Report</button>
//                     <button onClick={() => handleWorkerReportActions('download')}>Download My PDF</button>
               
//                 </div>
 
               
//                 {workerReportMessage && <div className={`message ${workerReportMessage.includes('Error') ? 'error' : 'success'}`}>{workerReportMessage}</div>}
 
//                 <table>
//                     <thead>
//                         <tr>
//                             <th>ID</th>
//                             <th>Zone</th>
//                             <th>Vehicle</th>
//                             <th>Worker</th>
//                             <th>Weight</th>
//                             <th>Time</th>
//                         </tr>
//                     </thead>
//                     <tbody>
//                         {workerReportData.length > 0 ? (
//                             workerReportData.map((log) => (
//                                 <tr key={log.logId}>
//                                     <td>{log.logId}</td>
//                                     <td>{log.zoneId}</td>
//                                     <td>{log.vehicleId}</td>
//                                     <td>{log.workerId}</td>
//                                     <td>{log.weightCollected} kg</td>
//                                     <td>{new Date(log.collectionTime).toLocaleString()}</td>
//                                 </tr>
//                             ))
//                         ) : (
//                             <tr><td colSpan="6">No data available.</td></tr>
//                         )}
//                     </tbody>
//                 </table>
//                 <div className="pagination-controls">
//                     <button onClick={() => setWorkerCurrentPage(prev => prev - 1)} disabled={workerCurrentPage === 0}>
//                         Previous
//                     </button>
//                     <span>Page {workerCurrentPage + 1} of {workerTotalPages}</span>
//                     <button onClick={() => setWorkerCurrentPage(prev => prev + 1)} disabled={workerCurrentPage >= workerTotalPages - 1}>
//                         Next
//                     </button>
//                 </div>
//             </div>
//         </div>
//     );
// };
 
// export default WorkerDashboard;
 

import React, { useState, useEffect, use } from 'react';
import '../styles/WorkerReports.css';
import {
    fetchWorkerAssignmentDetails,
    enterLogData,
    getWasteCollectionByWorkerId,
    downloadWasteCollectionReportByWorkerPdf,
    updateVehicleLiveStatus,
    fetchWorkerIdByEmail,
    getVehicleStatus
} from '../api/workerReportsApi';
import WorkerHeader from '../components/WorkerHeader';
//import truckIcon from '../assets/truck.png'; // Assuming you have a truck.png in src/assets
 
const WorkerDashboard = () => {
    // localStorage.setItem('authToken',eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJyb2xlIjoiQWRtaW4iLCJpYXQiOjE3NTEzOTM5NTQsImV4cCI6MTc1MTM5NzU1NH0.Y_rUKzZxf_1d0u65rUpeIw8pSiv037MyZVZF7TqSoeM);
    // State for Worker's context
    const [workerContext, setWorkerContext] = useState(null);
    const [loadingWorkerContext, setLoadingWorkerContext] = useState(true);
    const [workerContextError, setWorkerContextError] = useState('');
 
    // State for Enter Log Data Form
    const [logDataForm, setLogDataForm] = useState({
        weightCollected: '',
        collectionTime: '',
    });
    const [enterLogMessage, setEnterLogMessage] = useState('');
    // NEW: State to track if log data has been successfully submitted in this session
    const [hasLoggedForSession, setHasLoggedForSession] = useState(false);
 
 
    // State for Worker's Report Filter Form
 
    const [workerReportData, setWorkerReportData] = useState([]);
    const [worker_Id, setWorker_Id] = useState(0);
    const [workerReportMessage, setWorkerReportMessage] = useState('');
    const [workerCurrentPage, setWorkerCurrentPage] = useState(0);
    const [workerTotalPages, setWorkerTotalPages] = useState(0);
    const [workerPageSize] = useState(10);
    const [workerSortField] = useState('collectionTime');
    const [vehicleStatusMessage, setVehicleStatusMessage] = useState('');
   
    // New states for vehicle progress bar
    const [collectionProgress, setCollectionProgress] = useState(0); // 0 to 100
    const [collectionStatusText, setCollectionStatusText] = useState("");
 
    const email = localStorage.getItem("email");
    // Effect to fetch worker assignment details on component mount
    useEffect(() => {
        const getWorkerContext = async () => {
            try {
                setLoadingWorkerContext(true);
                setWorkerContextError('');
                const data1 = await fetchWorkerIdByEmail(email);
                console.log(`Worker ID for ${email}:`, data1);
                setWorker_Id(data1);
                console.log("W", worker_Id);
                setWorkerContext({ workerId: data1, zoneId: null, vehicleId: null });
                const data = await fetchWorkerAssignmentDetails(data1);
 
                console.log("Worker Assignment Details:", data);
                if (!data) {
                    setWorkerContextError("You are not assigned to any vehicle or zone. Please contact your supervisor.");
                    //window.location.href = '/unassigned-worker'; // Redirect to UnassignedWorker page
                    return;
                }
                const message=await getVehicleStatus(data.vehicleId);
 
                console.log('Vehicle Status:', message);
                if(message!="Completed"){
                const numbers = message.match(/\d+/g);
            console.log('Extracted numbers:', numbers);
            const [covered_distance,total_distance]=numbers;
            console.log('Covered Distance:', covered_distance, 'Total Distance:', total_distance);
            const percentage = (covered_distance / total_distance) * 100;
            console.log('Percentage:', percentage);
            setCollectionProgress(percentage);
                }
                else{
                    setCollectionProgress(100);
                    setCollectionStatusText("Waste collection completed");
                    console.log('Collection completed from console');
                }

                console.log("Worker Context Data:", data);
                
                setWorkerContext(data);
 
                if (data.weightCollected != null) {
                    setHasLoggedForSession(true);
                }
            } catch (error) {
                console.error("Error fetching worker assignment details:", error);
                const data1 = await fetchWorkerIdByEmail(email);
                console.log(`Worker ID for ${email}:`, data1);
                setWorker_Id(data1);
                // setWorkerContextError(error.response?.data || error.message);
               
                console.log("WOrker:", workerContext);
               
                // window.location.href = '/unassigned-worker'; // Redirect to UnassignedWorker page on error
            } finally {
                setLoadingWorkerContext(false);
            }
        };
 
        getWorkerContext();
    }, []);
    useEffect(() => {
        if (workerContext && workerContext.workerId) {
            fetchWorkerReportData(workerCurrentPage);
        }
    }, [workerCurrentPage, workerContext]);
 
    // Fetch Worker's Report Data function
    const fetchWorkerReportData = async (page = 0) => {
        setWorkerReportMessage('');
        if (!workerContext?.workerId) {
            setWorkerReportMessage('Worker ID is not available.');
            setWorkerReportData([]);
            return;
        }
 
        try {
            const params = {
                page,
                size: workerPageSize,
                sort: workerSortField,
            };
 
 
            const response = await getWasteCollectionByWorkerId(workerContext.workerId, params);
            setWorkerReportData(response.data.content);
            setWorkerCurrentPage(response.data.number);
            setWorkerTotalPages(response.data.totalPages);
 
        } catch (error) {
            setWorkerReportData([]);
 
            setWorkerReportMessage(`Error fetching your report: ${error.response?.data || error.message}`);
            console.error('Error fetching worker report:', error.response?.data || error);
 
        }
    };
 
    // Handle Enter Log Data Submission
    const handleEnterLogDataSubmit = async (e) => {
        e.preventDefault();
        setEnterLogMessage('');
        if (!workerContext?.workerId) {
            setEnterLogMessage('Worker ID is not available. Cannot submit log data.');
            return;
        }
        try {
 
            const formattedCollectionTime = logDataForm.collectionTime;
 
            await enterLogData(
                workerContext.workerId,
                parseFloat(logDataForm.weightCollected),
                formattedCollectionTime
            );
            setEnterLogMessage('Log data entered successfully!');
            setLogDataForm({ weightCollected: '', collectionTime: '' });
            setHasLoggedForSession(true);
            fetchWorkerReportData();
        } catch (error) {
            setEnterLogMessage(`Error entering log data: ${error.response?.data?.message || error.message}`);
            console.error('Error entering log data:', error.response?.data || error);
        }
    };
 
 
 
    // Handle Worker's Report Actions (Generate/Download)
    const handleWorkerReportActions = (actionType) => {
        if (actionType === 'generate') {
 
            setWorkerCurrentPage(0);
            fetchWorkerReportData(0);
        } else if (actionType === 'download') {
 
 
            handleDownloadWorkerPdf();
        }
    };
 
    // Handle Worker PDF Download
    const handleDownloadWorkerPdf = async () => {
        setWorkerReportMessage('');
 
        try {
 
            const pdfBlob = await downloadWasteCollectionReportByWorkerPdf(workerContext.workerId);
            const url = window.URL.createObjectURL(pdfBlob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `Worker_${workerContext.workerId}_WasteCollectionReport.pdf`;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
            setWorkerReportMessage('PDF report downloaded successfully.');
        } catch (error) {
            setWorkerReportMessage(`Error downloading PDF: ${error.response?.data?.message || error.message}`);
            console.error('Error downloading PDF:', error.response?.data || error);
        }
    };
 
    const handleUpdateVehicleStatus = async () => {
        setVehicleStatusMessage(''); // Clear previous messages
        if (!workerContext?.vehicleId) {
            setVehicleStatusMessage('Vehicle ID is not available. Cannot update status.');
            return;
        }
 
        try {
            console.log('Updating vehicle live status for Vehicle ID:', workerContext.vehicleId);
 
            const response = await updateVehicleLiveStatus(workerContext.vehicleId);
            console.log('Vehicle status update response:', response);
           
            setVehicleStatusMessage(response);
           
           
            if(response!='Waste Collection Completed'){
            const numbers = response.match(/\d+/g);
            console.log('Extracted numbers:', numbers);
            const [covered_distance,total_distance]=numbers;
            console.log('Covered Distance:', covered_distance, 'Total Distance:', total_distance);
            const percentage = (covered_distance / total_distance) * 100;
            console.log('Percentage:', percentage);
            setCollectionProgress(percentage);}
            else{
                setCollectionProgress(100);
                setCollectionStatusText("Waste collection completed");
                console.log('Collection completed from console');
            }
       
           
        } catch (error) {
            setVehicleStatusMessage(`Error updating vehicle status: ${error.response?.data?.message || error.message}`);
            setCollectionStatusText("Error updating status");
            console.error('Error updating vehicle status:', error.response?.data || error);
        }
    };
 
    if (loadingWorkerContext) {
        return <div className="container">Loading worker details from microservice...</div>;
    }
 
    if (workerContextError) {
        return <div className="container"><p className="message error">{workerContextError}</p></div>;
    }
 
    if (!workerContext || !workerContext.workerId) {
        return <div className="container"><p className="message error">Worker context not fully loaded. Cannot display dashboard.</p></div>;
    }
    const isCollectionCompleted = vehicleStatusMessage === 'Waste collection completed';
    console.log("Woorker Context:", workerContext);
    
    return (
        <div className="container">
            <WorkerHeader />
            <h1>Worker Dashboard</h1>
            <p>
                Your Worker ID: <strong>{workerContext.workerId}</strong> |
                Zone ID: <strong>{workerContext.zoneId ? workerContext.zoneId : "NA"}</strong> |
                Vehicle ID: <strong>{workerContext.vehicleId ? workerContext.vehicleId : "NA"}</strong>
            </p>
            <div className="section vehicle-status-section">
                <button
                    onClick={handleUpdateVehicleStatus}
                    disabled={!workerContext.vehicleId || collectionProgress >= 100}
                >
                    Update Vehicle Status
                </button>
                <div className="status-container">
                    <div className="progress-bar-container">
                        <div className="progress-bar" style={{ width: `${collectionProgress}%` }}>
                            <i className="fas fa-truck progress-icon"></i>
                        </div>
                        <span className="progress-text">{collectionStatusText || `Progress: ${Math.round(collectionProgress)}%`}</span>
                    </div>
                    {vehicleStatusMessage && !vehicleStatusMessage.includes('Error') && (
                        <span className="distance-text">{vehicleStatusMessage}</span>
                    )}
                </div>
                {vehicleStatusMessage && vehicleStatusMessage.includes('Error') && (
                    <div className="message error">{vehicleStatusMessage}</div>
                )}
            </div>
 
            <div className="section">
                {!workerContext.zoneId || !workerContext.vehicleId ? (
                    <p className='message '>You are not assigned to any zone or vehicle. Please contact your supervisor.</p>
                ) : hasLoggedForSession ? (
                    <p className="message success">You have successfully logged your reports.</p>
                ) : (<>
                    <h2>Enter Waste Log Data</h2>
 
                    <form onSubmit={handleEnterLogDataSubmit}>
    <label>Your Worker ID:</label>
    <input
        type="number" // Changed from type="int" to type="number"
        value={workerContext.workerId}
        required
        disabled
        min="0" // Added min="0" to ensure non-negative for worker ID (though it's disabled so not strictly necessary for user input)
    />
    <label>Weight Collected (kg):</label>
    <input
        type="number" // Changed from type="int" to type="number"
        value={logDataForm.weightCollected}
        min="0" // Already there, good!
        step="any" // Recommended to allow decimal weights
        onChange={(e) => {
            const value = parseFloat(e.target.value);
            // Allow empty string or non-negative numbers
            if (e.target.value === '' || (value >= 0 && !isNaN(value))) {
                setLogDataForm({ ...logDataForm, weightCollected: e.target.value });
            }
            // Optional: Add a visual indicator or message if invalid input is attempted
            // else {
            //   setEnterLogMessage('Weight must be a non-negative number.');
            // }
        }}
        required
    />
    <label>Collection Time:</label>
    <input
        type="datetime-local"
        value={logDataForm.collectionTime}
        onChange={(e) => setLogDataForm({ ...logDataForm, collectionTime: e.target.value })}
        required
    />
    <button type="submit">Submit Log Data</button>
    {enterLogMessage && <div className={`message ${enterLogMessage.includes('Error') ? 'error' : 'success'}`}>{enterLogMessage}</div>}
</form></>
                )}
            </div>
 
            <div className="section">
                <h2>My Waste Collection Report</h2>
                <div style={{ marginBottom: '15px' }}>
                    <button onClick={() => handleWorkerReportActions('generate')}>Generate My Report</button>
                    <button onClick={() => handleWorkerReportActions('download')}>Download My PDF</button>
 
                </div>
 
 
                {workerReportMessage && <div className={`message ${workerReportMessage.includes('Error') ? 'error' : 'success'}`}>{workerReportMessage}</div>}
 
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Zone</th>
                            <th>Vehicle</th>
                            <th>Worker</th>
                            <th>Weight</th>
                            <th>Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        {workerReportData.length > 0 ? (
                            workerReportData.map((log) => (
                                <tr key={log.logId}>
                                    <td>{log.logId}</td>
                                    <td>{log.zoneId}</td>
                                    <td>{log.vehicleId}</td>
                                    <td>{log.workerId}</td>
                                    <td>{log.weightCollected} kg</td>
                                    <td>{new Date(log.collectionTime).toLocaleString()}</td>
                                </tr>
                            ))
                        ) : (
                            <tr><td colSpan="6">No data available.</td></tr>
                        )}
                    </tbody>
                </table>
                <div className="pagination-controls">
                    <button onClick={() => setWorkerCurrentPage(prev => prev - 1)} disabled={workerCurrentPage === 0}>
                        Previous
                    </button>
                    <span>Page {workerCurrentPage + 1} of {workerTotalPages}</span>
                    <button onClick={() => setWorkerCurrentPage(prev => prev + 1)} disabled={workerCurrentPage >= workerTotalPages - 1}>
                        Next
                    </button>
                </div>
            </div>
        </div>
    );
};
 
export default WorkerDashboard;
 
 