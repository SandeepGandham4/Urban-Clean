import React from 'react';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

const SupervisorDashboard = () => {
    return (
        <div className="container">
            <h1 className="mt-4">Supervisor Dashboard</h1>
            <div className="mt-4">
                <h2>Manage Operations</h2>
                <ul className="list-group">
                    <li className="list-group-item">
                        <Link to="/zone-management" className="btn btn-primary">Zone Management</Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="/route-management" className="btn btn-primary">Route Management</Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="/pickupscheduling" className="btn btn-primary">Pickup Scheduling</Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="/vehicle-assignment" className="btn btn-primary">Vehicle Assignment</Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="/worker-management" className="btn btn-primary">Worker Management</Link>
                    </li>
                </ul>
            </div>
        </div>
    );
};

export default SupervisorDashboard;