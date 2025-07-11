import React, { useState } from 'react';
import { FaUserCircle, FaUser, FaCog, FaSignOutAlt } from 'react-icons/fa';
import { Dropdown } from 'react-bootstrap';
import '../styles/WorkerHeader.css';
 
const WorkerHeader = () => {
    const [showProfileMenu, setShowProfileMenu] = useState(false);
 
    const handleLogout = () => {
        localStorage.removeItem('authToken');
        window.location.href = '/login';
    };
 
    return (
        <header className="worker-header">
            <div className="worker-header-title">UrbanClean</div>
 
            <div className="worker-header-actions">
                <Dropdown show={showProfileMenu} onToggle={(isOpen) => setShowProfileMenu(isOpen)}>
                    <Dropdown.Toggle variant="link" className="profile-toggle">
                        <FaUserCircle className="profile-icon" />
                        <span className="worker-name">Worker</span>
                    </Dropdown.Toggle>
 
                    <Dropdown.Menu align="end" className="profile-dropdown">
               
                        <Dropdown.Divider />
                        <Dropdown.Item onClick={handleLogout} className="signout-item">
                            <FaSignOutAlt className="dropdown-icon" /> Sign Out
                        </Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            </div>
        </header>
    );
};
 
export default WorkerHeader;
 