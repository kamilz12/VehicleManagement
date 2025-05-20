
# Vehicle Management System

## Formal Information

### Project Name  
**Vehicle Management System**

### Technologies Used

- **Backend**:
  - Java 17  
  - Spring Boot 3.3.1  
  - Spring Security – authentication and authorization  
  - Spring Data JPA – data access layer  
  - Lombok – boilerplate code reduction

- **Frontend**:
  - Thymeleaf – HTML templating engine  
  - Bootstrap (CSS/JS) – user interface styling

- **Database**:
  - MySQL 8.0

- **Infrastructure**:
  - Docker – application containerization  
  - Maven – dependency management and project build

- **Integration**:
  - REST API – integration with external systems  
  - Jackson – JSON data processing

## Project Description

The Vehicle Management System addresses the integration challenge of retrieving a large database of vehicles (around 50,000 records) on demand, ensuring the data is as up-to-date as possible. The application pulls data from the U.S. government's FuelEconomy service, which includes relevant information on vehicle makes, models, and other characteristics. The system retrieves data in a structure that does not match the application's own, leading to conflicts that require systematic integration and proper data transformation.

### Sample Questions Answered by the Application:
1. What is the city fuel consumption for a specific car model?
2. What vehicles are available on the market?
3. What engine (type, capacity, fuel) does a given model have depending on the year of production?
4. What models are available for a given car brand in specific years? (This data is not obvious.)
5. What type of fuel does a specific vehicle use?

## Environment Setup

### System Requirements
- Java 17 JDK  
- Maven 3.6+  
- Docker and Docker Compose (optional)  
- MySQL 8.0 (if not running in a container)  
- IDE with Maven support (recommended: IntelliJ IDEA, Eclipse, VSCode)

### Running with Docker

1. Fill in the database credentials in the `docker-compose.yaml` file:  
   `docker-compose.yaml`
```yaml
mysql:
  container_name: mysql
  image: mysql:8.0
  environment:
    - MYSQL_USER=MYSQLUSERNAME
    - MYSQL_PASSWORD=MYSQLPASSWORD
    - MYSQL_DATABASE=vehicle
    - MYSQL_ROOT_PASSWORD=MYSQLROOTPASSWORD

vehicle-app:
  build: .
  container_name: vehicle-app
  ports:
    - "8080:8080"
  environment:
    - MYSQL_HOST=mysql
    - MYSQL_USERNAME=MYSQLUSERNAME
    - MYSQL_PASSWORD=MYSQLPASSWORD
    - MYSQL_DATABASE=vehicle
    - MYSQL_PORT=3306
```

Step-by-step commands to run the application:
- ```mvn clean install```
- ```docker-compose up --build```
- Restart the container to ensure the database is properly initialized:
- ```docker-compose down```
- ```docker-compose up --build```

## Data Sources

The system integrates data from the following sources:

1. **Internal MySQL database** – stores user, vehicle, and relational data  
2. **Fuel Economy API** – external API providing fuel consumption and vehicle information  
3. **User-provided data** – information on vehicles, their specifications, and history

The application manages data from multiple sources and presents it in a unified and consistent format for easy access.

## Additional Information

### Service Access
- Administrator account credentials seeded in the application:  
  - **Login**: `adminek`  
  - **Password**: `admin`

### System Features
- User registration and login  
- Adding, editing, and deleting vehicles  
- Searching for vehicle information  
- Fetching additional vehicle data from external APIs  
- Exporting vehicle data to various formats

### Security
The system uses Spring Security to protect data access. Each user only has access to their own vehicles. Users with the administrator role can also run system integration. The application uses Spring Security's built-in mechanisms for authentication and authorization.  
The session mechanism is cookie-based with HTTP-only cookies.

### API Integration
If external APIs are unavailable, the system uses a fallback mechanism based on the `fallback-makes.json` file.

### Running Without Docker
1. Run: ```mvn clean install```
2. In the `target` folder, run the app with: ```java -jar application.jar```
