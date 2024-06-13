
# LidarApi

LidarApi is a Spring Boot application for processing LiDAR data. This project provides RESTful APIs to handle various LiDAR data operations, including reading, processing, and storing LiDAR data files.

## Table of Contents
- [Getting Started](#getting-started)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Frontend Features](#frontend-features)

## Getting Started

These instructions will help you set up and run the LidarApi project on your local machine for development and testing purposes.

## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java 21
- Maven 3.6.0 
- Spring Boot 3.2.5
- Git
- Node.js and npm (for the frontend)



## Installation

1. Clone the repository:
```
git clone https://github.com/cdilsiz5/LidarApi.git
cd LidarApi
```
2. Build the project using Maven:
 ```
mvn clean install
```
3. Run the application:
```
mvn spring-boot:run
```

## Usage

Once the application is running, you can access the APIs through
``` 
http://localhost:8080
``` 
## API Endpoints

### Version 1 Endpoints (V1)
This section details the API endpoints for Version 1 of the LidarApi, which provides basic functionalities for LiDAR data management.

- **GET `/api/v1/luxoft/metadata`**
    - **Description:** Returns metadata information of LiDAR data.
    - **Responses:**
        - `200`: Successfully retrieved metadata.
        - `400`: Invalid request parameters.

- **GET `/api/v1/luxoft/data`**
    - **Description:** Returns binary data based on group IDs.
    - **Parameters:**
        - `startGroupId`: Start index of the group.
        - `endGroupId`: End index of the group.
    - **Responses:**
        - `200`: Successfully retrieved binary data.

### Version 2 Endpoints (V2)
This section details the API endpoints for Version 2 of the LidarApi, offering advanced features and functionalities for enhanced LiDAR data management.

- **GET `/api/v2/luxoft/sessions`**
    - **Description:** Returns a list of all LiDAR sessions.
    - **Responses:**
        - `200`: Successfully retrieved sessions list.

- **GET `/api/v2/luxoft/metadata/{sessionId}`**
    - **Description:** Returns metadata information for a specific LiDAR session based on the session ID.
    - **Parameters:**
        - `sessionId`: The session identifier.
    - **Responses:**
        - `200`: Successfully retrieved metadata.
        - `400`: Invalid request parameters.

- **GET `/api/v2/luxoft/data/{sessionId}`**
    - **Description:** Returns binary data for a specified session ID based on group IDs.
    - **Parameters:**
        - `sessionId`: The session identifier.
        - `startGroupId`: Start index of the group.
        - `endGroupId`: End index of the group.
    - **Responses:**
        - `200`: Successfully retrieved binary data.

## Frontend Features

### Overview
The frontend of our Angular project provides a user interface to interact with the Lidar data via a single-page application. It lists session IDs in a table and allows asynchronous data fetching using Web Workers. After the data fetching, the application stores the results in IndexedDB with relevant identifiers and updates the UI accordingly.

### Key Features
- **Session ID Listing:** Displays a table of session IDs fetched from the backend.
- **Asynchronous Data Fetching:** Utilizes Web Workers to perform asynchronous API requests when the "Get Data" button is clicked for any session ID.
- **Data Management:** Data fetched for each session, defined by startGroupId and endGroupId, is stored in IndexedDB.
- **UI Updates:** Once the data is stored, the "Get Data" button for the respective session ID updates to show "Imported", indicating the data is fetched and stored locally.

## Installation and Running the Project

### Prerequisites
Ensure you have the following installed:
- Node.js (preferably the latest LTS version)
- Angular CLI

### Setup
To set up the project, follow these steps:

1. Navigate to the project's frontend directory:
   ```bash
   cd /src/frontend
   ```
2. Install the necessary dependencies:
   ```bash
   npm install
    ```
3. Serve the application locally:
    ```
    ng serve
    ```
### Accessing the Application

Once the application is running, you can access it by opening a web browser and visiting:

[http://localhost:4200](http://localhost:4200)

This setup will present you with the main page where you can interact with the LiDAR data through the features described. The application's flow ensures a smooth user experience by efficiently managing data fetch and display operations asynchronously.
