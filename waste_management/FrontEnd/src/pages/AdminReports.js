// import React, { useState, useEffect } from 'react';
// import '../styles/AdminReports.css';
// import {
//     createWasteLog,
//     getAdminWasteCollectionReport,
//     downloadAdminWasteCollectionReportPdf
// } from '../api/adminApi';
// import AdminHeader from '../components/AdminHeader';
// import AdminSidebar from '../components/AdminSidebar';
// import { Button, Form } from 'react-bootstrap';
 
// const AdminReports = () => {
//     const [isSidebarOpen, setIsSidebarOpen] = useState(true);
//     const toggleSidebar = () => {
//         setIsSidebarOpen(!isSidebarOpen);
//     };
 
//     const [newLogData, setNewLogData] = useState({
//         zoneId: '',
//         vehicleId: '',
//         workerId: '',
//         weightCollected: '',
//         collectionTime: '',
//     });
//     const [createLogMessage, setCreateLogMessage] = useState('');
//     const [reportData, setReportData] = useState([]);
//     const [reportMessage, setReportMessage] = useState('');
//     const [showForm, setShowForm] = useState(false);
//     const [showFilters, setShowFilters] = useState(false);
//     const [reportFilters, setReportFilters] = useState({
//         startDate: '',
//         endDate: '',
//         zoneId: '',
//         vehicleId: '',
//         workerId: '',
//         page: 0
//     });
//     const [totalPages, setTotalPages] = useState(0);
 
//     const handleCreateLogSubmit = async (e) => {
//         e.preventDefault();
//         setCreateLogMessage('');
 
//         try {
//             const submissionData = {
//                 ...newLogData,
//                 weightCollected: parseFloat(newLogData.weightCollected),
//                 collectionTime: newLogData.collectionTime ? newLogData.collectionTime + ':00' : null,
//             };
 
//             const response = await createWasteLog(submissionData);
//             setCreateLogMessage(`Log created successfully! ID: ${response.logId}`);
//             setNewLogData({ zoneId: '', vehicleId: '', workerId: '', weightCollected: '', collectionTime: '' });
//             setShowForm(false);
//             // Refresh the report data after creating a new log
//             await fetchReportData();
//         } catch (error) {
//             console.error("Error submitting new log:", error);
//             setCreateLogMessage(`Error creating log: ${error.message}`);
//         }
//     };
 
//     const fetchReportData = async () => {
//         setReportMessage('Fetching report...');
//         try {
//             const filters = {};
//             for (const key in reportFilters) {
//                 if (reportFilters[key] !== '' && reportFilters[key] !== undefined && reportFilters[key] !== null) {
//                     filters[key] = reportFilters[key];
//                 }
//             }
           
//             const data = await getAdminWasteCollectionReport(filters);
//             setReportData(data.content || []);
//             setTotalPages(Math.ceil((data.totalElements || 0) / 10));
//             setReportMessage('Report data fetched successfully.');
//         } catch (error) {
//             setReportMessage(`Error fetching report: ${error.message}`);
//             setReportData([]);
//         }
//     };
 
//     const handleDownloadPdf = async () => {
//         try {
//             const filters = {};
//             for (const key in reportFilters) {
//                 if (reportFilters[key] !== '' && reportFilters[key] !== undefined && reportFilters[key] !== null) {
//                     filters[key] = reportFilters[key];
//                 }
//             }
           
//             const pdfBlob = await downloadAdminWasteCollectionReportPdf(filters);
//             const url = window.URL.createObjectURL(pdfBlob);
//             const a = document.createElement('a');
//             a.href = url;
//             a.download = `WasteCollectionReport_${new Date().toISOString().slice(0, 10)}.pdf`;
//             document.body.appendChild(a);
//             a.click();
//             a.remove();
//             window.URL.revokeObjectURL(url);
//             setReportMessage('PDF report downloaded successfully.');
//         } catch (error) {
//             console.error("Error downloading PDF:", error);
//             setReportMessage(`Error downloading PDF: ${error.message}`);
//         }
//     };
 
//     const handleApplyFilters = async () => {
//         setReportFilters(prev => ({ ...prev, page: 0 }));
//         await fetchReportData();
//     };
 
//     // Define handlePageChange function
//     const handlePageChange = (newPage) => {
//         setReportFilters((prevFilters) => ({
//             ...prevFilters,
//             page: newPage,
//         }));
//     };
 
//     useEffect(() => {
//         fetchReportData();
//     }, []);
 
//     return (
//         <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
//             <AdminHeader onToggleSidebar={toggleSidebar} />
//             <AdminSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
//             <div className="dashboard-content">
//                 <div className="admin-reports">
//                     <div className="admin-reports-header">
//                         <h2 className="page-title">Admin Reports</h2>
//                         <Button
//                             variant="primary"
//                             onClick={() => setShowForm(!showForm)}
//                             className="add-waste-log-btn"
//                         >
//                             {showForm ? 'Close' : 'Add Waste Log'}
//                         </Button>
//                     </div>
 
//                     {showForm && (
//                         <div className="add-waste-log-form-container">
//                             <Form onSubmit={handleCreateLogSubmit} className="add-waste-log-form">
//                                 <Form.Group controlId="formZoneId">
//                                     <Form.Label>Zone ID</Form.Label>
//                                     <Form.Control
//                                         type="number"
//                                         placeholder="Enter zone ID"
//                                         value={newLogData.zoneId}
//                                         onChange={(e) => setNewLogData({ ...newLogData, zoneId: e.target.value })}
//                                         required
//                                     />
//                                 </Form.Group>
//                                 <Form.Group controlId="formVehicleId">
//                                     <Form.Label>Vehicle ID</Form.Label>
//                                     <Form.Control
//                                         type="number"
//                                         placeholder="Enter vehicle ID"
//                                         value={newLogData.vehicleId}
//                                         onChange={(e) => setNewLogData({ ...newLogData, vehicleId: e.target.value })}
//                                         required
//                                     />
//                                 </Form.Group>
//                                 <Form.Group controlId="formWorkerId">
//                                     <Form.Label>Worker ID</Form.Label>
//                                     <Form.Control
//                                         type="number"
//                                         placeholder="Enter worker ID"
//                                         value={newLogData.workerId}
//                                         onChange={(e) => setNewLogData({ ...newLogData, workerId: e.target.value })}
//                                         required
//                                     />
//                                 </Form.Group>
//                                 <Form.Group controlId="formWeightCollected">
//                                     <Form.Label>Weight Collected (kg)</Form.Label>
//                                     <Form.Control
//                                         type="number"
//                                         step="0.01"
//                                         placeholder="Enter weight collected"
//                                         value={newLogData.weightCollected}
//                                         onChange={(e) => setNewLogData({ ...newLogData, weightCollected: e.target.value })}
//                                         required
//                                     />
//                                 </Form.Group>
//                                 <Form.Group controlId="formCollectionTime">
//                                     <Form.Label>Collection Time</Form.Label>
//                                     <Form.Control
//                                         type="datetime-local"
//                                         value={newLogData.collectionTime}
//                                         onChange={(e) => setNewLogData({ ...newLogData, collectionTime: e.target.value })}
//                                         required
//                                     />
//                                 </Form.Group>
//                                 <div className="form-buttons">
//                                     <Button type="submit" className="submit-btn">
//                                         Create Log
//                                     </Button>
//                                     <Button type="button" className="cancel-btn" onClick={() => setShowForm(false)}>
//                                         Cancel
//                                     </Button>
//                                 </div>
//                             </Form>
//                         </div>
//                     )}
 
//                     <div className="section">
//                         <h2>Waste Collection Report (Admin View)</h2>
 
//                         <div className="filters-form">
//                             <button
//                                 className="toggle-filters-btn"
//                                 onClick={() => setShowFilters(!showFilters)}
//                             >
//                                 {showFilters ? 'Hide Filters' : 'Show Filters'}
//                             </button>
 
//                             <div className="action-buttons">
//                                 <button type="button" onClick={handleDownloadPdf}>Download PDF Report</button>
//                                 <button type="submit" onClick={handleApplyFilters}>Apply Filters & Refresh</button>
//                             </div>
//                         </div>
 
//                         {showFilters && (
//                             <form className="filters-form">
//                                 <label>Start Date:</label>
//                                 <input type="date" value={reportFilters.startDate} onChange={(e) => setReportFilters({ ...reportFilters, startDate: e.target.value })} />
//                                 <label>End Date:</label>
//                                 <input type="date" value={reportFilters.endDate} onChange={(e) => setReportFilters({ ...reportFilters, endDate: e.target.value })} />
//                                 <label>Zone ID:</label>
//                                 <input type="number" value={reportFilters.zoneId} onChange={(e) => setReportFilters({ ...reportFilters, zoneId: parseInt(e.target.value) || '' })} placeholder="Optional" />
//                                 <label>Vehicle ID:</label>
//                                 <input type="number" value={reportFilters.vehicleId} onChange={(e) => setReportFilters({ ...reportFilters, vehicleId: parseInt(e.target.value) || '' })} placeholder="Optional" />
//                                 <label>Worker ID:</label>
//                                 <input type="number" value={reportFilters.workerId} onChange={(e) => setReportFilters({ ...reportFilters, workerId: parseInt(e.target.value) || '' })} placeholder="Optional" />
//                             </form>
//                         )}
 
//                         {reportMessage && <div className={`message ${reportMessage.includes('Error') ? 'error' : 'success'}`}>{reportMessage}</div>}
 
//                         <table>
//                             <thead>
//                                 <tr>
//                                     <th>ID</th>
//                                     <th>Zone</th>
//                                     <th>Vehicle</th>
//                                     <th>Worker</th>
//                                     <th>Weight</th>
//                                     <th>Time</th>
//                                 </tr>
//                             </thead>
//                             <tbody>
//                                 {reportData.length > 0 ? (
//                                     reportData.map((log) => (
//                                         <tr key={log.logId}>
//                                             <td>{log.logId}</td>
//                                             <td>{log.zoneId}</td>
//                                             <td>{log.vehicleId}</td>
//                                             <td>{log.workerId}</td>
//                                             <td>{log.weightCollected} kg</td>
//                                             <td>{new Date(log.collectionTime).toLocaleString()}</td>
//                                         </tr>
//                                     ))
//                                 ) : (
//                                     <tr><td colSpan="6">No data available for the selected filters.</td></tr>
//                                 )}
//                             </tbody>
//                         </table>
 
//                         <div className="pagination-controls">
//                             <button onClick={() => handlePageChange(reportFilters.page - 1)} disabled={reportFilters.page === 0}>
//                                 Previous
//                             </button>
//                             <span>Page {reportFilters.page + 1} of {totalPages}</span>
//                             <button onClick={() => handlePageChange(reportFilters.page + 1)} disabled={reportFilters.page >= totalPages - 1}>
//                                 Next
//                             </button>
//                         </div>
//                     </div>
//                 </div>
//             </div>
//         </div>
//     );
// };
 
// export default AdminReports;
 
import React, { useState, useEffect } from 'react';
import '../styles/AdminReports.css';
import {
    createWasteLog,
    getAdminWasteCollectionReport,
    downloadAdminWasteCollectionReportPdf
} from '../api/adminApi';
import AdminHeader from '../components/AdminHeader';
import AdminSidebar from '../components/AdminSidebar';
import { Button, Form } from 'react-bootstrap';
 
const AdminReports = () => {
    const [isSidebarOpen, setIsSidebarOpen] = useState(true);
    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };
 
    const [newLogData, setNewLogData] = useState({
        zoneId: '',
        vehicleId: '',
        workerId: '',
        weightCollected: '',
        collectionTime: '',
    });
    const [createLogMessage, setCreateLogMessage] = useState('');
    const [reportData, setReportData] = useState([]);
    const [reportMessage, setReportMessage] = useState('');
    const [showForm, setShowForm] = useState(false);
    const [showFilters, setShowFilters] = useState(false);
    const [reportFilters, setReportFilters] = useState({
        startDate: '',
        endDate: '',
        zoneId: '',
        vehicleId: '',
        workerId: '',
        page: 0
    });
    const [totalPages, setTotalPages] = useState(0);
 
    const handleCreateLogSubmit = async (e) => {
        e.preventDefault();
        setCreateLogMessage('');
 
        try {
            const submissionData = {
                ...newLogData,
                weightCollected: parseFloat(newLogData.weightCollected),
                collectionTime: newLogData.collectionTime ? newLogData.collectionTime + ':00' : null,
            };
 
            const response = await createWasteLog(submissionData);
            setCreateLogMessage(`Log created successfully! ID: ${response.logId}`);
            setNewLogData({ zoneId: '', vehicleId: '', workerId: '', weightCollected: '', collectionTime: '' });
            setShowForm(false);
            // Refresh the report data after creating a new log
            await fetchReportData();
        } catch (error) {
            console.error("Error submitting new log:", error);
            setCreateLogMessage(`Error creating log: ${error.message}`);
        }
    };
 
    const fetchReportData = async () => {
        setReportMessage('Fetching report...');
        try {
            const filters = {};
            for (const key in reportFilters) {
                if (reportFilters[key] !== '' && reportFilters[key] !== undefined && reportFilters[key] !== null) {
                    filters[key] = reportFilters[key];
                }
            }
           
            const data = await getAdminWasteCollectionReport(filters);
            setReportData(data.content || []);
            setTotalPages(Math.ceil((data.totalElements || 0) / 10));
            setReportMessage('Report data fetched successfully.');
        } catch (error) {
            setReportMessage(`Error fetching report: ${error.response.data}`);
            setReportData([]);
        }
    };
 
    const handleDownloadPdf = async () => {
        try {
            const filters = {};
            for (const key in reportFilters) {
                if (reportFilters[key] !== '' && reportFilters[key] !== undefined && reportFilters[key] !== null) {
                    filters[key] = reportFilters[key];
                }
            }
           
            const pdfBlob = await downloadAdminWasteCollectionReportPdf(filters);
            const url = window.URL.createObjectURL(pdfBlob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `WasteCollectionReport_${new Date().toISOString().slice(0, 10)}.pdf`;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);
            setReportMessage('PDF report downloaded successfully.');
        } catch (error) {
            console.error("Error downloading PDF:", error);
            setReportMessage(`Error downloading PDF: ${error.message}`);
        }
    };
 
    const handleApplyFilters = async (e) => {
        e.preventDefault();
        setReportFilters(prev => ({ ...prev, page: 0 }));
        await fetchReportData();
    };
 
    // Define handlePageChange function
    const handlePageChange = (newPage) => {
        setReportFilters((prevFilters) => ({
            ...prevFilters,
            page: newPage,
        }));
    };
 
    useEffect(() => {
        fetchReportData();
    }, [reportFilters.page]);
 
    return (
        <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
            <AdminHeader onToggleSidebar={toggleSidebar} />
            <AdminSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
            <div className="dashboard-content">
                <div className="admin-reports">
 
 
                   
                        {/* <h2>Waste Collection Report (Admin View)</h2> */}
                        <div className="report-header">
    <h1>Waste Collection Report </h1>
    <button type="button" onClick={handleDownloadPdf} className="download-pdf-btn">Download PDF Report</button>
</div>
                        <div className="filters-form">
                            <button
                                className="toggle-filters-btn"
                                onClick={() => setShowFilters(!showFilters)}
                            >
                                {showFilters ? 'Hide Filters' : 'Show Filters'}
                            </button>
 
                           
                        </div>
 
                        {showFilters && (
                            <form className="filters-form">
                                <label>Start Date:</label>
                                <input type="date" value={reportFilters.startDate} onChange={(e) => setReportFilters({ ...reportFilters, startDate: e.target.value })} />
                                <label>End Date:</label>
                                <input type="date" value={reportFilters.endDate} onChange={(e) => setReportFilters({ ...reportFilters, endDate: e.target.value })} />
                                <label>Zone ID:</label>
                                <input type="int"  value={reportFilters.zoneId} onChange={(e) => setReportFilters({ ...reportFilters, zoneId: parseInt(e.target.value) || '' })} placeholder="Optional" />
                                <label>Vehicle ID:</label>
                                <input type="int"  value={reportFilters.vehicleId} onChange={(e) => setReportFilters({ ...reportFilters, vehicleId: parseInt(e.target.value) || '' })} placeholder="Optional" />
                                <label>Worker ID:</label>
                                <input type="int" value={reportFilters.workerId} onChange={(e) => setReportFilters({ ...reportFilters, workerId: parseInt(e.target.value) || '' })} placeholder="Optional" />
                                <div className="action-buttons">
                                {/* <button type="button" onClick={handleDownloadPdf}>Download PDF Report</button> */}
                                <button type="submit" onClick={handleApplyFilters}>Apply Filters & Refresh</button>
                            </div>
                            </form>
                        )}
 
                        {reportMessage && <div className={`message ${reportMessage.includes('Error') ? 'error' : 'success'}`}>{reportMessage}</div>}
 
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
                                {reportData.length > 0 ? (
                                    reportData.map((log) => (
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
                                    <tr><td colSpan="6">No data available for the selected filters.</td></tr>
                                )}
                            </tbody>
                        </table>
 
                        <div className="pagination-controls">
                            <button onClick={() => handlePageChange(reportFilters.page - 1)} disabled={reportFilters.page === 0}>
                                Previous
                            </button>
                            <span>Page {reportFilters.page + 1} of {totalPages}</span>
                            <button onClick={() => handlePageChange(reportFilters.page + 1)} disabled={reportFilters.page >= totalPages - 1}>
                                Next
                            </button>
                        </div>
                    </div>
                </div>
            </div>
     
    );
};
 
export default AdminReports;
 
 
 
