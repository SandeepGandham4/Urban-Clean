import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/homepage.css';
import logo from './img.png';
import loginImg from './homepageimg.png';
import { FaRecycle, FaHome, FaBuilding, FaLeaf, FaChartLine, FaArrowRight } from 'react-icons/fa';
 
function HomePage() {
  return (
    <div className="landing-page">
      <nav className="navbar">
        <div className="navbar-left">
          <img src={logo} alt="Logo" className="logo" />
          <span className="site-title">Urban Clean</span>
        </div>
        <div className="navbar-right">
          <div className="navbar-center">
            <a href="#services">Our Services</a>
            <a href="#about">About</a>
            <a href="#contact">Contact</a>
          </div>
          <Link to="/login" className="btn login">Login</Link>
          <Link to="/register" className="btn register">Sign Up</Link>
        </div>
      </nav>
 
      <section className="hero">
        <div className="hero-content">
          <div className="hero-text">
            <h1>URBAN CLEAN, FUTURE GREEN</h1>
            <p className="tagline">Smart Solutions for a Cleaner Tomorrow. Join us in building sustainable communities through efficient waste management.</p>
            <div className="hero-buttons">
              <Link to="/register" className="btn btn-primary">
                Get Started <FaArrowRight className="arrow-icon" />
              </Link>
            </div>
            <div className="statistics">
              <div className="stat-item">
                <span className="stat-number">95%</span>
                <span className="stat-label">Collection Rate</span>
              </div>
              <div className="stat-item">
                <span className="stat-number">50K+</span>
                <span className="stat-label">Happy Customers</span>
              </div>
              <div className="stat-item">
                <span className="stat-number">24/7</span>
                <span className="stat-label">Support</span>
              </div>
            </div>
          </div>
          <div className="hero-image">
            <img src={loginImg} alt="Waste Management" />
          </div>
        </div>
      </section>
 
      <section id="services" className="services">
        <h2>Our Solutions</h2>
        <div className="service-grid">
          <div className="service-card">
            <FaHome className="service-icon" />
            <h3>Residential Waste</h3>
            <p>Smart scheduling and efficient collection for households</p>
            <ul>
              <li>Regular pickups</li>
              <li>Recycling services</li>
              <li>Green waste handling</li>
            </ul>
          </div>
          <div className="service-card">
            <FaBuilding className="service-icon" />
            <h3>Commercial Services</h3>
            <p>Tailored solutions for businesses of all sizes</p>
            <ul>
              <li>Custom collection plans</li>
              <li>Waste audits</li>
              <li>Compliance support</li>
            </ul>
          </div>
          <div className="service-card">
            <FaRecycle className="service-icon" />
            <h3>Recycling Programs</h3>
            <p>Comprehensive recycling and waste reduction</p>
            <ul>
              <li>Material sorting</li>
              <li>Recycling education</li>
              <li>Sustainable practices</li>
            </ul>
          </div>
        </div>
      </section>
 
      <section className="features">
        <h2>Why Choose Us</h2>
        <div className="features-grid">
          <div className="feature">
            <FaRecycle className="feature-icon" />
            <h3>Efficient Collection</h3>
            <p>Optimized routes and timely pickups</p>
          </div>
          <div className="feature">
            <FaLeaf className="feature-icon" />
            <h3>Eco-Friendly</h3>
            <p>Committed to sustainable practices</p>
          </div>
          <div className="feature">
            <FaChartLine className="feature-icon" />
            <h3>Smart Analytics</h3>
            <p>Data-driven waste management</p>
          </div>
        </div>
      </section>
 
      <section id="contact" className="footer">
        <h2>Contact Us</h2>
        <div className="footer-content">
          <div className="contact-grid">
            <div className="footer-info">
              <h3>Get in Touch</h3>
              <p><i className="fas fa-envelope"></i> support@urbanclean.com</p>
              <p><i className="fas fa-phone"></i> +1 234 567 890</p>
              <p><i className="fas fa-map-marker-alt"></i> 123 Clean Street, Green City</p>
            </div>
            <div className="social-links">
              <h3>Follow Us</h3>
              <div className="social-buttons">
                <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer">Facebook</a>
                <a href="https://www.twitter.com" target="_blank" rel="noopener noreferrer">Twitter</a>
                <a href="https://www.instagram.com" target="_blank" rel="noopener noreferrer">Instagram</a>
              </div>
            </div>
          </div>
          <p className="copyright">&copy; {new Date().getFullYear()} Urban Clean. All rights reserved.</p>
        </div>
      </section>
    </div>
  );
}
 
export default HomePage;
 
 
 