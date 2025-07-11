// import React, { useState } from 'react';
// import { useNavigate } from 'react-router-dom';
// import 'bootstrap/dist/css/bootstrap.min.css';
// import '../styles/Login.css';
// import logo from './img.png'; // Adjust the path to your logo image
// import { Link } from 'react-router-dom';
// import axios from 'axios';
 
// const Login = () => {
//     const [email, setEmail] = useState('');
//     const [password, setPassword] = useState('');
//     const [error, setError] = useState('');
//     const [role, setRole] = useState('');
//     const navigate = useNavigate();
 
//     const handleLogin = async (e) => {
//         e.preventDefault();
//         try {
//             const response = await axios.post('http://localhost:8081/api/urbanclean/v1/login', {
//                 email,
//                 password,
//             });
//             console.log('Login response:', response.data);
 
//             const { token, role } = response.data;
//             //console.log('Role:', token); // Debugging: Check the role value
//             // Store the token in localStorage
//             localStorage.setItem('authToken', token);
//             localStorage.setItem('email', email);
//             console.log('Email:', email); 
            
//             // Redirect based on role
//             if (role === 'Admin') {
//                 navigate('/admin');
//             } else if (role === 'Supervisor') {
//                 navigate('/pickupscheduling');
//             } else if (role === 'Worker') {
//                 navigate('/worker');
//             } else {
//                 setError('Invalid role.');
//             }
//         } catch (err) {
//             setError(err.response?.data?.message || 'Login failed.');
//         }
//     };
//         // Add login logic here (e.g., API call)
//         console.log('Logging in with:', { email, password, role });
//         // Redirect based on role
 
//     return (
       
//         <div className="container">
// <nav className="navbar">
//         <div className="navbar-left">
//           <img src={logo} alt="Logo" className="logo" />
//           <span className="site-title">Urban Clean</span>
//         </div>
//         <div className="navbar-right">
       
//           <Link to="/" className="btn login">Home</Link>
//           <Link to="/register" className="btn register">Register</Link>
//         </div>
//       </nav>
//             <h2 className="mt-5">Login</h2>
//             <form onSubmit={handleLogin}>
//                 <div className="form-group">
//                     <label>Email</label>
//                     <input
//                         type="email"
//                         className="form-control"
//                         value={email}
//                         onChange={(e) => setEmail(e.target.value)}
//                         required
//                     />
//                 </div>
//                 <div className="form-group">
//                     <label>Password</label>
//                     <input
//                         type="password"
//                         className="form-control"
//                         value={password}
//                         onChange={(e) => setPassword(e.target.value)}
//                         required
//                     />
//                 </div>
//                 {/* <div className="form-group">
//                     <label>Role</label>
//                     <select
//                         className="form-control"
//                         value={role}
//                         onChange={(e) => setRole(e.target.value)}
//                     >
//                         <option value="admin">Admin</option>
//                         <option value="supervisor">Supervisor</option>
//                         <option value="worker">Worker</option>
//                     </select>
//                 </div> */}
//                 <button type="submit" className="btn btn-primary mt-3">Login</button>
//             </form>
//         </div>
//     );
// };
 
// export default Login;
 

import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../styles/Login.css';
import Header from './Header';
import axios from 'axios';
 
const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [role, setRole] = useState('');
    const navigate = useNavigate();
 
    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8087/api/urbanclean/v1/auth/login', {
                email,
                password,
            });
            console.log('Login response:', response.data);
 
            const { token, role } = response.data;
            localStorage.setItem('authToken', token);
            localStorage.setItem('email', email);
           
            if (role === 'Admin') {
                navigate('/admin');
            } else if (role === 'Supervisor') {
                navigate('/pickupscheduling');
            } else if (role === 'Worker') {
                navigate('/worker');
            } else {
                setError('Invalid role.');
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Login failed.');
        }
    };
 
    return (
        <>
            <Header />
            <div className="container login-container">
                <h2 className="mt-5">Login</h2>
                {error && <div className="alert alert-danger">{error}</div>}
                <form onSubmit={handleLogin}>
                    <div className="form-group">
                        <label>Email</label>
                        <input
                            type="email"
                            className="form-control"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Password</label>
                        <input
                            type="password"
                            className="form-control"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary mt-3">Login</button>
                </form>
            </div>
        </>
    );
};
 
export default Login;
 
 
