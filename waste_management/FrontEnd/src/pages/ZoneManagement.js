import React, { useEffect, useState, useRef } from 'react';
import { Button, Form, InputGroup } from 'react-bootstrap';
import { getZones, createZone, updateZone, deleteZone } from '../api/zoneApi';
import { Link } from 'react-router-dom';
import '../styles/ZoneManagement.css';
import AdminHeader from '../components/AdminHeader';
import AdminSidebar from '../components/AdminSidebar';
import { toast } from 'react-toastify';
 
const ZoneManagement = () => {
    const [zones, setZones] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [zoneData, setZoneData] = useState({ name: '', areaCoverage: '' });
    const [isEditing, setIsEditing] = useState(false);
    const [isSidebarOpen, setIsSidebarOpen] = useState(true);
    const [searchQuery, setSearchQuery] = useState('');

    const formRef = useRef(null); // Reference to the form
    const searchRef = useRef(null); // Reference to the search section

    useEffect(() => {
        fetchZones();
    }, []);

    const fetchZones = async () => {
        const data = await getZones();
        setZones(data);
    };

    const handleShow = (zone = { id: '', name: '', areaCoverage: '' }) => {
        setZoneData(zone);
        setIsEditing(!!zone.id);
        setShowForm(true);

        // Scroll to the search section
        setTimeout(() => {
            searchRef.current?.scrollIntoView({ behavior: 'smooth' });
        }, 0);
    };

    const handleClose = () => {
        setShowForm(false);
        setZoneData({ id: '', name: '', areaCoverage: '' });
        setIsEditing(false);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (isEditing) {
            await updateZone(zoneData.id, zoneData);
        } else {
            await createZone(zoneData);
        }
        fetchZones();
        handleClose();
    };

    const handleDelete = async (id) => {
        await deleteZone(id);
        toast.success("Zone deleted successfully!");
        fetchZones();
    };

    const toggleSidebar = () => {
        setIsSidebarOpen(!isSidebarOpen);
    };

    const filteredZones = zones.filter(zone =>
        zone.name.toLowerCase().includes(searchQuery.toLowerCase())
    );

    // Add scroll to top functionality for the search page
    const handleSearchFocus = () => {
        const searchRef = document.querySelector('.zone-management-header'); // Target the search section
        if (searchRef) {
            searchRef.scrollIntoView({ behavior: 'smooth' }); // Scroll to the search section
        }
    };

    // Call handleSearchFocus when needed, such as on a button click or page load
    useEffect(() => {
        handleSearchFocus(); // Ensure the page scrolls to the search section on load
    }, []);
 
    return (
        <div className={`admin-dashboard ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
            <AdminHeader onToggleSidebar={toggleSidebar} />
            <AdminSidebar isOpen={isSidebarOpen} onClose={() => setIsSidebarOpen(false)} />
            <div className="dashboard-content">
                <div className="zone-management">
                    <div ref={searchRef} className="zone-management-header">
                        <h2>Zone Management</h2>
                        <div className="zone-controls">
                            <InputGroup className="search-bar">
                                <Form.Control
                                    placeholder="Search zones..."
                                    value={searchQuery}
                                    onChange={(e) => setSearchQuery(e.target.value)}
                                />
                                <InputGroup.Text className="search-icon">
                                    <i className="fas fa-search"></i>
                                </InputGroup.Text>
                            </InputGroup>
                            <Button
                                variant="primary"
                                onClick={() => setShowForm(!showForm)}
                            >
                                {showForm ? 'Hide Form' : 'Add Zone'}
                            </Button>
                        </div>
                    </div>
 
                    <div ref={formRef} className={`add-zone-form-container ${showForm ? 'visible' : 'hidden'}`}>
                        <Form onSubmit={handleSubmit} className="add-zone-form">
                            <Form.Group controlId="formZoneName">
                                <Form.Label>Zone Name</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Enter zone name"
                                    value={zoneData.name}
                                    onChange={(e) => setZoneData({ ...zoneData, name: e.target.value })}
                                    required
                                />
                            </Form.Group>
                            <Form.Group controlId="formAreaCoverage">
                                <Form.Label>Area Coverage</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Enter area coverage"
                                    value={zoneData.areaCoverage}
                                    onChange={(e) => setZoneData({ ...zoneData, areaCoverage: e.target.value })}
                                    required
                                />
                            </Form.Group>
                            <div className="form-buttons">
                                <Button type="submit" className="submit-btn">
                                    {isEditing ? 'Update Zone' : 'Create Zone'}
                                </Button>
                                <Button type="button" className="cancel-btn" onClick={handleClose}>
                                    Cancel
                                </Button>
                            </div>
                        </Form>
                    </div>
 
                    <div className="zone-grid">
                        {filteredZones.map(zone => (
                            <div className="zone-block" key={zone.id}>
                                <h3 className="zone-name">{zone.name}</h3>
                                <p className="zone-details">ID: {zone.id}</p>
                                <p className="zone-details">Area Coverage: {zone.areaCoverage}</p>
                                <div className="zone-actions">
                                <i className="fas fa-edit action-icon edit-icon" onClick={() => handleShow(zone)} title="Update Zone"></i>
                                    <i className="fas fa-trash-alt action-icon delete-icon" onClick={() => handleDelete(zone.id)} title="Delete Zone"></i>
                                    <Link to={`/route-management/${zone.id}`}>
                                        <i className="fas fa-route action-icon" title="Manage Routes"></i>
                                    </Link>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};
 
export default ZoneManagement;
