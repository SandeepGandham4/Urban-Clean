// import React from 'react';
// import { FaBars } from 'react-icons/fa'; // Import the hamburger menu icon
// import '../styles/AdminHeader.css'; // Add styles for the Admin Header
 
// const AdminHeader = ({ onToggleSidebar }) => {
//   return (
//     <header className="admin-header">
//       <button className="menu-btn" onClick={onToggleSidebar}>
//         <FaBars />
//       </button>
//       <h1 className="admin-title">Admin Dashboard</h1>
//     </header>
//   );
// };
 
// export default AdminHeader;

import React, { useState } from 'react';
import { FaBars, FaUserCircle, FaCog, FaSignOutAlt, FaUser } from 'react-icons/fa';
import { Dropdown } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import '../styles/AdminHeader.css';
 
const AdminHeader = ({ onToggleSidebar }) => {
  const [showProfileMenu, setShowProfileMenu] = useState(false);
 
  const handleLogout = () => {
    localStorage.removeItem('authToken');
    window.location.href = '/login';
  };
 
  return (
    <header className="admin-header">
      <div className="header-left">
        <button className="menu-btn" onClick={onToggleSidebar}>
          <FaBars />
        </button>
        <h1 className="admin-title">
          <Link to="/admin" style={{ textDecoration: 'none', color: 'inherit' }}>Urban Clean</Link>
        </h1>
      </div>
 
      <div className="header-right">
        <Dropdown show={showProfileMenu} onToggle={(isOpen) => setShowProfileMenu(isOpen)}>
          <Dropdown.Toggle variant="link" className="profile-toggle">
            <FaUserCircle className="profile-icon" />
            <span className="admin-name">Admin</span>
          </Dropdown.Toggle>
 
          <Dropdown.Menu align="end" className="profile-dropdown">
            <Dropdown.Item onClick={handleLogout} className="signout-item">
              <FaSignOutAlt className="dropdown-icon" /> Sign Out
            </Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
      </div>
    </header>
  );
};
 
export default AdminHeader;
