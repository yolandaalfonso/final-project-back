# Triply - Travel Social Network

Triply is a social network platform for travelers where users can document and share their journeys. Users can create travel entries with titles, descriptions, countries visited, dates, and photos, building a personal travel diary while connecting with other travelers.

Key features include user authentication via Firebase, trip management with photo storage, and a social feed to explore other travelers' adventures. The platform is built with Spring Boot (backend) and React (frontend), ensuring a smooth and responsive experience.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Project Structure](#project-structure)
- [Running Tests](#running-tests)
- [Tools](#tools)
- [Known Issues](#known-issues)
- [Future Improvements](#future-improvements)
- [Contributors](#contributors)
- [Disclaimer](#disclaimer)

## Prerequisites

Before running this project, ensure you have the following installed on your machine:

- Java 21 or later
- Maven
- PostgreSQL
- Docker (optional, for containerized databases)
- Firebase account
- Node.js and npm (for the frontend)

## Installation

Instructions on how to install and set up the project.

```bash
git clone https://github.com/yolandaalfonso/final-project-back.git
cd final-project-back
```

### Environment Variables (.env)

Create a `.env` file in the root directory with the following variables:

```env
DATABASE_URL=jdbc:postgresql://localhost:5432/mydatabase
DATABASE_USERNAME=your_database_username
DATABASE_PASSWORD=your_database_password
SPRING_PROFILES_ACTIVE=dev
API_ENDPOINT=/api
```

### Database Setup

You need to set up the following environment variables to connect to the database:

- **DATABASE_URL**: The JDBC connection URL for your PostgreSQL database. Example: `jdbc:postgresql://localhost:5432/mydatabase`

- **DATABASE_USERNAME**: The username used to access the database. Replace with the username that has permission to access and manage the database. Example: `admin_user`

- **DATABASE_PASSWORD**: The password for the database username. Replace with the actual password that corresponds to the username. Example: `supersecretpassword`

Make sure to set these environment variables correctly before running the application so that it can connect to the database successfully.

### Firebase Setup

To use Firebase in this project, follow these steps:

1. **Create a Firebase Account**: If you don't have a Firebase account, go to [Firebase](https://firebase.google.com/) and sign up.

2. **Create a Firebase Project**: Once you have an account, create a new project in Firebase. This is where your app will connect to Firebase services.

3. **Enable Firebase Authentication**: In your Firebase project, go to the Authentication section and enable the authentication methods you want to use (Email/Password, Google, etc.).

4. **Enable Firebase Storage**: Go to the Storage section in your Firebase project and enable it. This will be used to store trip photos.

5. **Get Your Firebase Key**: After setting up your project, Firebase will give you a service account key in the form of a JSON file. This file contains important information your app needs to connect to Firebase.

6. **Save the JSON File**: Download this JSON file and place it in the `src/main/resources` folder of your project with the name `firebase-service-account.json`. Without this file, the app won't be able to connect to Firebase.

7. **Configure Firebase in Your Application**: The application is already configured to read the Firebase credentials from the JSON file. Make sure the file is in the correct location.

That's it! Now your app will be able to use Firebase Authentication and Firebase Storage.

## Usage

To run the application:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## Endpoints

API endpoint base: `/api`

### Swagger Documentation

We use Swagger (OpenAPI) to provide clear, interactive documentation for our API. Swagger automatically generates up-to-date documentation based on the API code, allowing developers to explore and test endpoints directly in their browser.

**Key Benefits:**
- Comprehensive Documentation: All API routes, parameters, and responses are clearly displayed.
- Interactive Testing: Test API calls directly through the Swagger UI without needing external tools.
- Seamless Collaboration: Provides a common reference for developers and integrators to easily understand the API structure.

**How to Access:**
1. Run the application
2. Open your browser and go to: `http://localhost:8080/swagger-ui.html`

### Main Endpoints

#### Authentication
- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Login with Firebase token

#### Trips
- `GET /api/trips` - Get all trips
- `GET /api/trips/{id}` - Get trip by ID
- `POST /api/trips` - Create a new trip (requires authentication)
- `PUT /api/trips/{id}` - Update a trip (requires authentication)
- `DELETE /api/trips/{id}` - Delete a trip (requires authentication)

#### Profile
- `GET /api/profile/{userId}` - Get user profile
- `PUT /api/profile/{userId}` - Update user profile (requires authentication)

## Project Structure

Below is an overview of the main directories and files in the Triply project:

```
final-project-back/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/
│   │   │       └── yol/
│   │   │           └── final_project_back/
│   │   │               ├── auth/              # Authentication logic
│   │   │               ├── config/            # Application configuration
│   │   │               ├── firebase/          # Firebase integration
│   │   │               ├── globals/           # Global utilities
│   │   │               ├── images/            # Image management
│   │   │               ├── implementations/   # Service implementations
│   │   │               ├── profile/           # User profile management
│   │   │               ├── register/          # User registration
│   │   │               ├── security/          # Security configuration
│   │   │               └── trip/              # Trip management
│   │   │                   ├── dtos/          # Data Transfer Objects
│   │   │                   ├── TripEntity.java
│   │   │                   ├── TripController.java
│   │   │                   ├── ITripService.java
│   │   │                   ├── TripMapper.java
│   │   │                   └── TripRepository.java
│   │   └── resources/
│   │       ├── application.properties         # Application configuration
│   │       ├── data.sql                       # Initial database setup
│   │       └── firebase-service-account.json  # Firebase credentials
│   │
│   └── test/                                  # Test cases
│
├── target/                                    # Compiled output files
├── .env                                       # Environment variables (not in repo)
├── .gitignore                                # Git ignore file
├── pom.xml                                   # Maven dependencies
└── README.md                                 # Project documentation
```

## Running Tests

To ensure everything is working as expected, you can run the unit and integration tests included in the project. Use the following command to execute all tests:

```bash
mvn test
```

This will automatically run all the tests defined in the `src/test/` directory, validating the functionality of the different components, including services, controllers, and data layers.

**Important Notes:**
- Make sure your database is properly set up and running if your tests depend on database interactions.
- If you have specific test profiles or configurations (such as test databases), ensure they are correctly set up in your `application-test.properties` or other test-related configuration files.
- The results of the tests will be displayed in the terminal, and you can check detailed reports in the `target/surefire-reports` folder after execution.

Running the tests regularly helps ensure that new changes do not break existing functionality and keeps the codebase reliable.

##Coverage Test

<img width="279" height="663" alt="Captura de pantalla 2025-11-03 a las 8 24 36" src="https://github.com/user-attachments/assets/6fd5ad14-d380-415a-8f8f-083effc10219" />

## Tools

- Visual Studio Code
- Postman
- Docker
- PostgreSQL
- Firebase Console

## Known Issues

- **Image Upload Limitations**: Current implementation has size limits for photo uploads. Large images may need compression before upload.
- **Search Functionality**: Search feature is basic and could be improved with full-text search capabilities.

Feel free to open an issue if you encounter something not listed here.

## Future Improvements

Here are some planned features or improvements for future versions of the project:

- **Enhanced Search**: Implement full-text search with filters for countries, dates, and trip types.
- **Social Features**: Add likes, comments, and the ability to follow other travelers.
- **Map Integration**: Display trips on an interactive map showing visited locations.
- **Trip Planning**: Add features for planning future trips with itineraries and budgets.
- **Mobile App**: Develop native mobile applications for iOS and Android.
- **Notifications**: Real-time notifications for new followers, likes, and comments.
- **Export Feature**: Allow users to export their trips as PDF or other formats.

## Contributors

**Backend Developer:**
- Yolanda Alfonso: [@yolandaalfonso](https://github.com/yolandaalfonso)

**Frontend:**
- Frontend Repository: [Triply Frontend](https://github.com/yolandaalfonso/final-project-front)

## Disclaimer

This project is developed as part of a bootcamp learning experience and is intended for educational purposes only. The creators and contributors are not responsible for any issues, damages, or losses that may occur from using this code.

This project is not meant for commercial use. All trademarks and references to third-party services (such as Firebase and Google) belong to their respective owners. By using this code, you acknowledge that it is a work in progress, created by learners, and comes without warranties or guarantees of any kind.

Use at your own discretion and risk.
