import React, { useState, useEffect } from 'react';
import { Form, Button, Table, Alert, Dropdown } from 'react-bootstrap';
import { FaUserCircle, FaCog, FaSignOutAlt, FaUser, FaBell } from 'react-icons/fa';
import {
    getPickupSchedule,
    schedulePickup,
    getNewlyAddedZones,
    deletePickup,
    updatePickup,
} from '../api/pickupApi';
import '../styles/PickupScheduling.css';
 
const PickupScheduling = () => {
    const [zones, setZones] = useState([]);
    const [formData, setFormData] = useState({ zoneId: '', zoneName: '', frequency: '', timeSlot: '', status: '' });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [showProfileMenu, setShowProfileMenu] = useState(false);
    const [notifications] = useState([]);
    const [pickups, setPickups] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [createNewPickup, setCreateNewPickup] = useState(false);
    const [updatescheduleId, setUpdateScheduleId] = useState(null);
    const hasZones = zones.length > 0;
 
    useEffect(() => {
        fetchNewlyAddedZones();
        fetchPickupSchedule();
    }, []);
 
    const fetchNewlyAddedZones = async () => {
        try {
            const data = await getNewlyAddedZones();
            setZones(data);
        } catch (err) {
            setError('Error fetching newly added zones.');
        }
    };
 
    const fetchPickupSchedule = async () => {
        try {
            const data = await getPickupSchedule();
            console.log(data);
            
            setPickups(data);
        } catch (err) {
            setError('Error fetching pickup schedule.');
        }
    };
 
    // Update the form submission handler to invoke updatePickup when updating a schedule
    const handleSchedulePickup = async (e) => {
        e.preventDefault();
        try {
            if (!createNewPickup) {
                // If zoneId exists, it means we are updating an existing schedule
                await updatePickup(updatescheduleId, formData);
                setSuccess('Pickup schedule updated successfully.');
                setUpdateScheduleId(null); 
            } else {
                // Otherwise, we are creating a new schedule
                await schedulePickup(formData);
                setSuccess('Pickup schedule created successfully.');
            }
            setCreateNewPickup(false); 
            setFormData({ zoneId: '', zoneName: '', frequency: '', timeSlot: '', status: '' });
            setShowForm(false); // Hide the form after updating
            fetchNewlyAddedZones();
            fetchPickupSchedule();
        } catch (err) {
            setError('Error scheduling or updating pickup.');
        }
    };
 
    const handleDeletePickup = async (scheduleId) => {
        try {
            await deletePickup(scheduleId);
            setSuccess('Pickup schedule deleted successfully.');
            fetchPickupSchedule();
            //fetchNewlyAddedZones();
        } catch (err) {
            setError('Error deleting pickup schedule.');
        }
    };
 
    const handleLogout = () => {
        localStorage.removeItem('authToken');
        window.location.href = '/login';
    };

    const handleBellClick = async () => {
        setShowForm((prev) => !prev);
        if (!showForm) {
            try {
                const data = await fetchNewlyAddedZones();
                setZones(data);
            } catch (err) {
                setError('Error fetching zones.');
            }
        }
    };

    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    };
 
    const filteredPickups = pickups.filter((pickup) =>
        pickup.zoneId.toString().includes(searchTerm) ||
        pickup.zoneName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        pickup.frequency.toLowerCase().includes(searchTerm.toLowerCase()) ||
        pickup.timeSlot.toLowerCase().includes(searchTerm.toLowerCase()) ||
        pickup.status.toLowerCase().includes(searchTerm.toLowerCase())
    );
 
    // Add logic to show the form with pre-filled data for updating a pickup schedule
    const handleEditPickup = (scheduleId) => {
        console.log("scheduleId",scheduleId);
        
        const pickupToEdit = pickups.find((pickup) => pickup.scheduleId === scheduleId);
        if (pickupToEdit) {
            setUpdateScheduleId(scheduleId);
            setFormData({
                zoneId: pickupToEdit.zoneId,
                zoneName: pickupToEdit.zoneName,
                frequency: pickupToEdit.frequency,
                timeSlot: pickupToEdit.timeSlot,
                status: pickupToEdit.status,
            });
            setShowForm(true);
        }
    };
 
    return (
        <div className="pickup-scheduling-container">
            <header className="admin-header">
                <div className="header-left">
                    <h1 className="admin-title">Urban Clean</h1>
                </div>
                <div className="header-right">
                    <div className="notification-icon">
                        <button
                            className="bell-button"
                            onClick={() => {hasZones && setShowForm((prev) => !prev);setCreateNewPickup(true)}}
                            style={{ position: "relative", background: "none", border: "none", cursor: hasZones ? "pointer" : "default" }}
                            disabled={!hasZones}
                        >
                            <FaBell className="bell-icon" />
                            {hasZones && <span className="red-dot" style={{
                                position: "absolute",
                                top: 2,
                                right: 2,
                                width: 10,
                                height: 10,
                                background: "red",
                                borderRadius: "50%",
                                display: "inline-block"
                            }} />}
                        </button>
                    </div>
                    <Dropdown show={showProfileMenu} onToggle={(isOpen) => setShowProfileMenu(isOpen)}>
                        <Dropdown.Toggle variant="link" className="profile-toggle">
                            <FaUserCircle className="profile-icon" />
                            <span className="admin-name">Supervisor</span>
                        </Dropdown.Toggle>
 
                        <Dropdown.Menu align="end" className="profile-dropdown">
                            <Dropdown.Item>
                                <FaUser className="dropdown-icon" /> Your Profile
                            </Dropdown.Item>
                            <Dropdown.Item>
                                <FaCog className="dropdown-icon" /> Settings
                            </Dropdown.Item>
                            <Dropdown.Divider />
                            <Dropdown.Item onClick={handleLogout} className="signout-item">
                                <FaSignOutAlt className="dropdown-icon" /> Sign Out
                            </Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                </div>
            </header>
 
            <div className="pickup-scheduling">
                {error && <Alert variant="danger">{error}</Alert>}
                {success && <Alert variant="success">{success}</Alert>}
               
                {/* Add Pickup Schedule Form */}
                {showForm && (
                    <>
                        {formData.zoneId ? (
                            <Form onSubmit={handleSchedulePickup}>
                                <Form.Group controlId="formZoneDetails">
                                    <Form.Label>Zone</Form.Label>
                                    {createNewPickup &&<Form.Select
                                        value={formData.zoneId}
                                        onChange={(e) => setFormData({
                                            ...formData,
                                            zoneId: e.target.value,
                                            zoneName: e.target.options[e.target.selectedIndex].text
                                        })}
                                        required
                                    >
                                        <option value="">Select Zone</option>
                                        {zones.map((zone) => (
                                            <option key={zone.id} value={zone.id}>{zone.name}</option>
                                        ))}
                                    </Form.Select>}
                                    {!createNewPickup && (
                                        <Form.Control
                                            type="text"
                                            value={formData.zoneId || 'Zone ID not available'}
                                            readOnly
                                        />
                                    )}
                                </Form.Group>
 
                                <Form.Group controlId="formFrequency">
                                    <Form.Label>Frequency</Form.Label>
                                    <Form.Select
                                        value={formData.frequency}
                                        onChange={(e) => setFormData({ ...formData, frequency: e.target.value })}
                                        required
                                    >
                                        <option value="">Select Frequency</option>
                                        <option value="DAILY">Daily</option>
                                        <option value="WEEKLY_SUNDAY">Weekly - Sunday</option>
                                        <option value="WEEKLY_MONDAY">Weekly - Monday</option>
                                        <option value="WEEKLY_TUESDAY">Weekly - Tuesday</option>
                                        <option value="WEEKLY_WEDNESDAY">Weekly - Wednesday</option>
                                        <option value="WEEKLY_THURSDAY">Weekly - Thursday</option>
                                        <option value="WEEKLY_FRIDAY">Weekly - Friday</option>
                                        <option value="WEEKLY_SATURDAY">Weekly - Saturday</option>
                                    </Form.Select>
                                </Form.Group>
 
                                <Form.Group controlId="formTimeSlot">
                                    <Form.Label>Time Slot</Form.Label>
                                    <Form.Control
                                        type="text"
                                        placeholder="Enter time slot"
                                        name="timeSlot"
                                        value={formData.timeSlot}
                                        onChange={(e) => setFormData({ ...formData, timeSlot: e.target.value })}
                                        required
                                    />
                                </Form.Group>
 
                                <Form.Group controlId="formStatus">
                                    <Form.Label>Status</Form.Label>
                                    <Form.Select
                                        value={formData.status}
                                        onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                                        required
                                    >
                                        <option value="">Select Status</option>
                                        <option value="SCHEDULED">Scheduled</option>
                                        <option value="COMPLETED">Completed</option>
                                    </Form.Select>
                                </Form.Group>
 
                                <div className="form-buttons">
                                    <Button variant="primary" type="submit" className="btn-block">
                                        {createNewPickup ? "Schedule Pickup" : "Update Pickup"}
                                    </Button>
                                    <Button
                                        variant="secondary"
                                        className="btn-block"
                                        onClick={() => {
                                            setShowForm(false);
                                            setFormData({ zoneId: '', zoneName: '', frequency: '', timeSlot: '', status: '' });
                                        }}
                                    >
                                        Cancel
                                    </Button>
                                </div>
                            </Form>
                        ) : (
                            <Form onSubmit={handleSchedulePickup}>
                                <Form.Group controlId="formZoneDetails">
                                    <Form.Label>Zone</Form.Label>
                                    <Form.Select
                                        value={formData.zoneId}
                                        onChange={(e) => setFormData({
                                            ...formData,
                                            zoneId: e.target.value,
                                            zoneName: e.target.options[e.target.selectedIndex].text
                                        })}
                                        required
                                    >
                                        <option value="">Select Zone</option>
                                        {zones.map((zone) => (
                                            <option key={zone.id} value={zone.id}>{zone.name}</option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>
 
                                <Form.Group controlId="formFrequency">
                                    <Form.Label>Frequency</Form.Label>
                                    <Form.Select
                                        value={formData.frequency}
                                        onChange={(e) => setFormData({ ...formData, frequency: e.target.value })}
                                        required
                                    >
                                        <option value="">Select Frequency</option>
                                        <option value="DAILY">Daily</option>
                                        <option value="WEEKLY_SUNDAY">Weekly - Sunday</option>
                                        <option value="WEEKLY_MONDAY">Weekly - Monday</option>
                                        <option value="WEEKLY_TUESDAY">Weekly - Tuesday</option>
                                        <option value="WEEKLY_WEDNESDAY">Weekly - Wednesday</option>
                                        <option value="WEEKLY_THURSDAY">Weekly - Thursday</option>
                                        <option value="WEEKLY_FRIDAY">Weekly - Friday</option>
                                        <option value="WEEKLY_SATURDAY">Weekly - Saturday</option>
                                    </Form.Select>
                                </Form.Group>
 
                                <Form.Group controlId="formTimeSlot">
                                    <Form.Label>Time Slot</Form.Label>
                                    <Form.Control
                                        type="text"
                                        placeholder="Enter time slot"
                                        name="timeSlot"
                                        value={formData.timeSlot}
                                        onChange={(e) => setFormData({ ...formData, timeSlot: e.target.value })}
                                        required
                                    />
                                </Form.Group>
 
                                <Form.Group controlId="formStatus">
                                    <Form.Label>Status</Form.Label>
                                    <Form.Select
                                        value={formData.status}
                                        onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                                        required
                                    >
                                        <option value="">Select Status</option>
                                        <option value="SCHEDULED">Scheduled</option>
                                        <option value="COMPLETED">Completed</option>
                                    </Form.Select>
                                </Form.Group>
 
                                <div className="form-buttons">
                                    <Button variant="primary" type="submit" className="btn-block">
                                        {formData.zoneId ? "Update Pickup" : "Schedule Pickup"}
                                    </Button>
                                    <Button
                                        variant="secondary"
                                        className="btn-block"
                                        onClick={() => {
                                            setShowForm(false);
                                            setFormData({ zoneId: '', zoneName: '', frequency: '', timeSlot: '', status: '' });
                                        }}
                                    >
                                        Cancel
                                    </Button>
                                </div>
                            </Form>
                        )}
                    </>
                )}
 
                {/* Adjust the layout to make the search bar exactly opposite to 'Pickup Schedules' */}
                <div className="table-section">
                    <div className="header-container" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <div className="pickup-schedules-header-container">
                            <h2 className="pickup-schedules-header">Pickup Schedules</h2>
                        </div>
                        <div className="search-bar-container" style={{ display: 'flex', alignItems: 'center' }}>
                            <div className="search-bar-wrapper" style={{ display: 'flex', border: '1px solid #ccc', borderRadius: '4px', overflow: 'hidden' }}>
                                <input
                                    type="text"
                                    className="search-bar"
                                    placeholder="Search..."
                                    value={searchTerm}
                                    onChange={handleSearchChange}
                                    style={{ border: 'none', padding: '0.5rem', flex: 1 }}
                                />
                                <button className="search-icon-button" style={{ background: '#1B5E20', border: 'none', padding: '0.5rem', color: '#fff' }}>
                                    <i className="fa fa-search"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                    <Table striped bordered hover>
                        <thead>
                            <tr>
                                <th>Schedule ID</th>
                                <th>Zone ID</th>
                                <th>Zone Name</th>
                                <th>Frequency</th>
                                <th>Time Slot</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredPickups.map((pickup) => (
                                <tr key={pickup.scheduleId}>
                                    <td>{pickup.scheduleId}</td>
                                    <td>{pickup.zoneId}</td>
                                    <td>{pickup.zoneName}</td>
                                    <td>{pickup.frequency}</td>
                                    <td>{pickup.timeSlot}</td>
                                    <td>{pickup.status}</td>
                                    <td>
                                    {/* <div className="zone-actions"> */}
                                        {/* Update the order of buttons to place the edit button first, followed by the delete button */}
                                        <i className="fas fa-edit action-icon edit-icon" onClick={() => handleEditPickup(pickup.scheduleId)} title="Update Pickup"></i>
                                        <i className="fas fa-trash-alt action-icon delete-icon" onClick={() => handleDeletePickup(pickup.scheduleId)} title="Delete Pickup"></i>
                                        {/* </div> */}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </Table>
                </div>
            </div>
        </div>
    );
};
 
export default PickupScheduling;
