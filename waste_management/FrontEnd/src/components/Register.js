// import React, { useState } from 'react';
// import { Form, Button, Dropdown, Alert } from 'react-bootstrap';
// import axios from 'axios';
// import { useNavigate , Link} from 'react-router-dom';
// import 'bootstrap/dist/css/bootstrap.min.css';
// import logo from './img.png'; 

// const Register = () => {
//     const [name, setName] = useState('');
//     const [email, setEmail] = useState('');
//     const [password, setPassword] = useState('');
//     const [role, setRole] = useState('Select Role');
//     const [success, setSuccess] = useState('');
//     const [error, setError] = useState('');
//     const navigate = useNavigate();

//     const handleRegister = async (e) => {
//         e.preventDefault();
//         try {
//             const response = await axios.post('http://localhost:8081/api/urbanclean/v1/register/user', {
//                 name,
//                 email,
//                 password,
//                 role, // Backend expects role as an object with roleName
//             });
//             console.log(response.data);
//             alert(response.data);
//             setSuccess('Registration successful!');
//             setError('');
//             setTimeout(() => {
//                 navigate('/login'); // Redirect to the login page after 2 seconds
//             }, 2000);
//         } catch (err) {
//             // alert(err.response.data.password);
//             // console.log(err.response.data);
//             setError(err.response?.data?.message || 'Registration failed.');
//             setSuccess('');
//         }
//     };

//     return (
//         <div className="container mt-5">
//             <nav className="navbar">
//         <div className="navbar-left">
//           <img src={logo} alt="Logo" className="logo" />
//           <span className="site-title">Urban Clean</span>
//         </div>
//         <div className="navbar-right">
       
//           <Link to="/" className="btn login">Home</Link>
//           <Link to="/register" className="btn register">Register</Link>
//         </div>
//       </nav>
//             <h2>Register</h2>
//             {success && <Alert variant="success">{success}</Alert>}
//             {error && <Alert variant="danger">{error}</Alert>}
//             <Form onSubmit={handleRegister}>
//                 <Form.Group controlId="formName">
//                     <Form.Label>Name</Form.Label>
//                     <Form.Control
//                         type="text"
//                         placeholder="Enter name"
//                         value={name}
//                         onChange={(e) => setName(e.target.value)}
//                         required
//                     />
//                 </Form.Group>

//                 <Form.Group controlId="formEmail">
//                     <Form.Label>Email</Form.Label>
//                     <Form.Control
//                         type="email"
//                         placeholder="Enter email"
//                         value={email}
//                         onChange={(e) => setEmail(e.target.value)}
//                         required
//                     />
//                 </Form.Group>

//                 <Form.Group controlId="formPassword">
//                     <Form.Label>Password</Form.Label>
//                     <Form.Control
//                         type="password"
//                         placeholder="Enter password"
//                         value={password}
//                         onChange={(e) => setPassword(e.target.value)}
//                         required
//                     />
//                 </Form.Group>

//                 <Form.Group controlId="formRole">
//                     <Form.Label>Role</Form.Label>
//                     <Dropdown onSelect={(eventKey) => setRole(eventKey)}>
//                         <Dropdown.Toggle variant="success" id="dropdown-basic">
//                             {role}
//                         </Dropdown.Toggle>

//                         <Dropdown.Menu>
//                             <Dropdown.Item eventKey="DRIVER">DRIVER</Dropdown.Item>
//                             <Dropdown.Item eventKey="GARBAGECOLLECTOR">GARBAGECOLLECTOR</Dropdown.Item>
//                             {/* <Dropdown.Item eventKey="Worker">Worker</Dropdown.Item> */}
//                         </Dropdown.Menu>
//                     </Dropdown>
//                 </Form.Group>

//                 <Button variant="primary" type="submit" className="mt-3">
//                     Register
//                 </Button>
//             </Form>
//         </div>
//     );
// };

// export default Register;

import React, { useState } from 'react';
import { Form, Button, Dropdown, Alert } from 'react-bootstrap';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './Header';
 
const Register = () => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('Select Role');
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();
 
    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8087/api/urbanclean/v1/auth/register/user', {
                name,
                email,
                password,
                role,
            });
            console.log(response.data);
            // alert(response.data);
            setSuccess('Registration successful!');
            setError('');
            setTimeout(() => {
                navigate('/login');
            }, 2000);
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed.');
            setSuccess('');
        }
    };
 
    return (
        <>
            <Header />
            <div className="container mt-5">
                <h2>Register</h2>
                {success && <Alert variant="success">{success}</Alert>}
                {error && <Alert variant="danger">{error}</Alert>}
                <Form onSubmit={handleRegister}>
                    <Form.Group controlId="formName">
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            type="text"
                            placeholder="Enter name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </Form.Group>
 
                    <Form.Group controlId="formEmail">
                        <Form.Label>Email</Form.Label>
                        <Form.Control
                            type="email"
                            placeholder="Enter email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </Form.Group>
 
                    <Form.Group controlId="formPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Enter password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </Form.Group>
 
                    <Form.Group controlId="formRole">
                        <Form.Label>Role</Form.Label>
                        <Dropdown onSelect={(eventKey) => setRole(eventKey)}>
                            <Dropdown.Toggle variant="success" id="dropdown-basic">
                                {role}
                            </Dropdown.Toggle>
 
                            <Dropdown.Menu>
                                <Dropdown.Item eventKey="DRIVER">DRIVER</Dropdown.Item>
                                <Dropdown.Item eventKey="GARBAGECOLLECTOR">GARBAGECOLLECTOR</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                    </Form.Group>
 
                    <Button variant="primary" type="submit" className="mt-3">
                        Register
                    </Button>
                </Form>
            </div>
        </>
    );
};
 
export default Register;
 