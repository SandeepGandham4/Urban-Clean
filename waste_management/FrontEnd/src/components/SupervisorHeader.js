import React, { useState } from 'react';
import { FaBars, FaUserCircle, FaCog, FaSignOutAlt, FaUser } from 'react-icons/fa';
import { Dropdown } from 'react-bootstrap';
import '../styles/AdminHeader.css';
 
const SupervisorHeader = ({ onToggleSidebar }) => {
  const [showProfileMenu, setShowProfileMenu] = useState(false);
 
  const handleLogout = () => {
    localStorage.removeItem('authToken');
    window.location.href = '/login';
  };
 
  return (
    <header className="supervisor-header">
      <div className="header-left">
       
        <h1 className="admin-title">Urban Clean</h1>
      </div>
 
      <div className="header-right">
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
  );
};
 
export default SupervisorHeader;
 