# Urban Clean Waste Management System

## Overview
The Urban Clean Waste Management System is a comprehensive solution designed to streamline waste management processes. This project consists of a frontend built with React and a backend powered by Node.js and Express. The system allows for efficient management of zones, routes, pickups, vehicles, and workers.

## Frontend
The frontend of the application is developed using React and styled with Bootstrap. It includes the following features:

- **Homepage**: A welcoming page with options to register and log in.
- **Role Selection**: Dropdowns for selecting user roles such as Admin, Supervisor, and Worker.
- **Dashboard Components**: Separate dashboards for different user roles, providing tailored functionalities.

### Installation
To set up the frontend, follow these steps:

1. Navigate to the `frontend` directory:
   ```
   cd urban-clean-waste-management/frontend
   ```

2. Install the required dependencies:
   ```
   npm install
   ```

3. Start the development server:
   ```
   npm start
   ```

## Backend
The backend is built using Node.js and Express, providing RESTful APIs for managing various aspects of the waste management system. Key components include:

- **Controllers**: Handle the business logic for zones, routes, pickups, vehicles, and workers.
- **Models**: Define the data structure for each entity in the database.
- **Routes**: Set up the API endpoints for client-server communication.

### Installation
To set up the backend, follow these steps:

1. Navigate to the `backend` directory:
   ```
   cd urban-clean-waste-management/backend
   ```

2. Install the required dependencies:
   ```
   npm install
   ```

3. Start the server:
   ```
   node src/app.js
   ```

## Features
- User registration and authentication.
- Role-based access control.
- Management of waste collection zones and routes.
- Scheduling of pickups and vehicle assignments.
- Worker management functionalities.

## Technologies Used
- React
- Bootstrap
- Node.js
- Express
- MongoDB (or any other database of choice)

## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue for any enhancements or bug fixes.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.