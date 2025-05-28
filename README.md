# Vehicle Management System

## Formal Information

### Project Name
Vehicle Management System – Vehicle management system

### Project Team and Responsibilities
1. **Kamil Ziółkowski** – backend  
2. **Szymon Zych** – frontend  
3. **Joint task:** data‑retrieval module

### Technologies Used
- **Backend**
  - Java 17
  - Spring Boot 3.3.1
  - Spring Security – authentication and authorization
  - Spring Data JPA – data‑access layer
  - Lombok – reduces boilerplate code

- **Frontend**
  - Thymeleaf – HTML template engine
  - Bootstrap (CSS/JS) – UI styling

- **Database**
  - MySQL 8.0

- **Infrastructure**
  - Docker – containerisation
  - Maven – dependency management and project build

- **Integration**
  - REST API – integration with external systems
  - Jackson – JSON processing

## Project Description
The Vehicle Management System solves the integration challenge of retrieving a large vehicle dataset (≈ 50 000 records) at any time while ensuring the data are as up‑to‑date as possible.  
The application pulls data from the US government **FuelEconomy** service, which contains makes, models and other vehicle details. Because the external data structure differs from our own, integration and transformation are required before the information can be used.

### Data‑extraction Process from the FuelEconomy Service

**Stage 1**

1. Retrieve the list of years (1984 – current year).  
2. Retrieve vehicle makes.  
3. Retrieve models for each make.  
4. Retrieve engines for each model.  

This produces mapped vehicles with their models and engines for the corresponding production years.

**Stage 2**

Using the engine ID, we extract the remaining vehicle details, map them to DTOs, store them in the database and expose them through the REST service.  
Because the application follows the MVC pattern, the same data are presented to the UI views.

If data are needed only for specific years, a year filter can be configured in the application.

### Sample Questions Answered by the Application
1. What is the city fuel consumption for a specific vehicle model?  
2. Which vehicles are available on the market?  
3. What engine (type, displacement, fuel) does a given model have in a particular production year?  
4. Which models of a given make are available in selected years (information not directly obvious from the source)?  
5. What fuel does a specific vehicle use?

## Environment Setup

### System Requirements
- Java 17 JDK  
- Maven 3.6+  
- Docker and Docker Compose (optional)  
- MySQL 8.0 (if not run inside a container)  
- An IDE with Maven support (recommended: IntelliJ IDEA, Eclipse, VS Code)

### Running with Docker
Run the application in containers with:

```bash
docker compose up --build
# then, for subsequent starts
docker compose up
```

## Data Sources

The system integrates data from:

1. **Internal MySQL database** – stores users, vehicles and their relationships.  
2. **Fuel Economy API** – external API providing fuel‑consumption and vehicle specifications.  
3. **User‑entered data** – vehicle information, parameters and history.

The application unifies these sources, ensuring coherent and easily accessible information.

## Additional Information

### Service Access
Seeded administrator account:  
- **Username:** `adminek`  
- **Password:** `admin`

### System Features
- User registration and login  
- Adding, editing and deleting vehicles  
- Vehicle information search  
- Fetching additional vehicle data from external APIs  
- Exporting vehicle data in various formats

### Security
Spring Security restricts data access. Each user sees only their own vehicles, while an administrator can also trigger system integration.  
Session management uses HTTP‑only cookies.

### API Integration
If the external API is unavailable, a fallback mechanism loads data from `fallback-makes.json`.

### Running Without Docker
1. Build the project:

   ```bash
   mvn clean install
   ```

   (Requires local Maven and Java.)

2. In the `target` folder, start the application:

   ```bash
   java -jar application.jar
   ```
