import React, { useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import { FaBars, FaTimes } from 'react-icons/fa';
import '../styles/Header.css';
import logo from './img.png';
 
const Header = () => {
    const location = useLocation();
    const [isMenuOpen, setIsMenuOpen] = useState(false);
 
    const getHeaderContent = () => {
        switch (location.pathname) {
            case '/login':
                return {
                    buttons: [
                        { to: '/', text: 'Home', className: 'home' },
                        { to: '/register', text: 'Register', className: 'register' }
                    ]
                };
            case '/register':
                return {
                    buttons: [
                        { to: '/', text: 'Home', className: 'home' },
                        { to: '/login', text: 'Login', className: 'login' }
                    ]
                };
            case '/':
                return {
                    buttons: [
                        { to: '/login', text: 'Login', className: 'login' },
                        { to: '/register', text: 'Register', className: 'register' }
                    ]
                };
            default:
                return {
                    buttons: [
                        { to: '/', text: 'Home', className: 'home' }
                    ]
                };
        }
    };
 
    const { buttons } = getHeaderContent();
 
    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    };
 
    return (
        <header className="glass-header">
            <div className="header-content">
                <div className="header-left">
                    <img src={logo} alt="Logo" className="logo" />
                    <h1 className="site-title">Urban Clean</h1>
                </div>
 
                <div className="header-right">
                    <button className="mobile-menu" onClick={toggleMenu}>
                        {isMenuOpen ? <FaTimes /> : <FaBars />}
                    </button>
 
                    <div className={`nav-links ${isMenuOpen ? 'show-mobile-menu' : ''}`}>
                        {buttons.map((button, index) => (
                            <Link
                                key={index}
                                to={button.to}
                                className={`btn ${button.className}`}
                                onClick={() => setIsMenuOpen(false)}
                            >
                                {button.text}
                            </Link>
                        ))}
                    </div>
                </div>
            </div>
        </header>
    );
};
 
export default Header;
 
 