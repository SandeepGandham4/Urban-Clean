// import React from 'react';
// import { FaCar, FaMapMarkedAlt, FaUsers, FaRoute, FaChartBar } from 'react-icons/fa';
// import { NavLink } from 'react-router-dom';
// import '../styles/AdminSidebar.css';
 
// const AdminSidebar = () => {
//   return (
//     <div className="sidebar">
//       <h2>Admin Panel</h2>
//       <ul className="sidebar-menu">
//         <li>
//         <NavLink to="/zone-management" activeClassName="active-link">
//             <FaMapMarkedAlt className="menu-icon" /> Manage Zones
//           </NavLink>
//         </li>
//         <li>
//         <NavLink to="/vehiclemanagement" activeClassName="active-link">
//             <FaRoute className="menu-icon" /> Manage Vehicles
//           </NavLink>
//         </li>
//         <li>
//         <NavLink to="/vehicleassignment" activeClassName="active-link">
//             <FaRoute className="menu-icon" /> Assign Vehicles
//           </NavLink>
//         </li>
 
//         <li>
//         <NavLink to="/workermanagement" activeClassName="active-link">
//             <FaUsers className="menu-icon" /> Manage Workers
//           </NavLink>
//         </li>
 
//         <li>
//         <NavLink to="/workerassignment" activeClassName="active-link">
//             <FaCar className="menu-icon" /> Assign Workers
//           </NavLink>
//         </li>
 
//         <li>
//         <NavLink to="/reports" activeClassName="active-link">
//             <FaChartBar className="menu-icon" /> View Reports
//           </NavLink>
//         </li>
//       </ul>
//     </div>
//   );
// };
 
// export default AdminSidebar;
 
import React from 'react';
import { FaCar, FaMapMarkedAlt, FaUsers, FaRoute, FaChartBar, FaTimes } from 'react-icons/fa';
import { NavLink } from 'react-router-dom';
import '../styles/AdminSidebar.css';
 
const AdminSidebar = ({ onClose, isOpen }) => {
  const handleClose = () => {
    if (onClose) {
      onClose();
    }
  };
 
  return (
    <div className={`sidebar ${!isOpen ? 'closed' : ''}`}>
      <div className="sidebar-header">
        <h2>Admin Panel</h2>
        <button className="close-btn" onClick={handleClose} title="Collapse Sidebar">
          <FaTimes />
        </button>
      </div>
      <ul className="sidebar-menu">
        <li>
          <NavLink to="/zone-management" activeClassName="active-link">
            <FaMapMarkedAlt className="menu-icon" />
            <span className="menu-text">Manage Zones</span>
          </NavLink>
        </li>
        <li>
          <NavLink to="/vehiclemanagement" activeClassName="active-link">
            <FaCar className="menu-icon" />
            <span className="menu-text">Manage Vehicles</span>
          </NavLink>
        </li>
        <li>
          <NavLink to="/vehicleassignment" activeClassName="active-link">
            <FaRoute className="menu-icon" />
            <span className="menu-text">Assign Vehicles</span>
          </NavLink>
        </li>
        <li>
          <NavLink to="/workermanagement" activeClassName="active-link">
            <FaUsers className="menu-icon" />
            <span className="menu-text">Manage Workers</span>
          </NavLink>
        </li>
        <li>
          <NavLink to="/workerassignment" activeClassName="active-link">
            <FaCar className="menu-icon" />
            <span className="menu-text">Assign Workers</span>
          </NavLink>
        </li>
        <li>
          <NavLink to="/reports" activeClassName="active-link">
            <FaChartBar className="menu-icon" />
            <span className="menu-text">View Reports</span>
          </NavLink>
        </li>
      </ul>
    </div>
  );
};
 
export default AdminSidebar;
 
 