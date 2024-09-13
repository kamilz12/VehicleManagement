# Vehicle Data Application

This application allows you to fetch data from government sources via the [Fuel Economy API](https://www.fueleconomy.gov/feg/ws/index.shtml). Using this API, you can retrieve information such as the make, model, year of manufacture, and engine specifications of all available cars.

Application uses SpringMVC, Thymeleaf, JavaScript, SpringSecurity, Hibernate, Spring Data, Jackson,  MySQL

## Instalation
Firstable you need to set database parametrs
`dockercompose.yaml`
```
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

You need to be in application catalog!
 - mvn clean install 
 - docker-compose up --build
 Now you need to restart containters
 - docker-compose down 
 - docker-compose up --build
 Your application is ready for use!

## Features

- Retrieve vehicle data including make, model, year, and engine specifications from the Fuel Economy API and information about fuel consumption of this vehicles.
- Make an CRUD operations on your vehicles
- Store vehicle data in a database.
- User-friendly interface to manage your vehicle data.

## TODO

- [ ] Update vehicle table only for admin with button on main page | Button is added but it's needed to make a permissions for admin
- [ ] Maintenance interface
- [ ] Privileges based on user role
- [ ] Loading bar for updating vehicles

## Usage

1. Access the application in your web browser at `http://localhost:8000`.
2. Create account by clicking 'Register'
3. Use UpdateVehicleData button in welcome page to fetch newest list of vehicles from external API
4. Keep an instruction in fetch vehicles page, you need to wait for fetching all vehicles. You can watch progress in console. It's recommended to make an vehicle update one/two times per year.
5. Now you can use all 



<details>
<summary>Screenshots</summary>

![Screenshot 1](https://github.com/user-attachments/assets/63ad2cf3-a6bb-46ad-8845-e61ea5699487)
![Screenshot 2](https://github.com/user-attachments/assets/b12e538e-d45a-4778-a07a-d2c7e3c48256)
![Screenshot 3](https://github.com/user-attachments/assets/8f9ec453-aae2-49d3-8023-97f38fc9f30f)
![Screenshot 4](https://github.com/user-attachments/assets/5ffbb0ef-cc07-4a43-b6a5-e1a4d2685d94)
![Screenshot 5](https://github.com/user-attachments/assets/06d080cb-ba70-4c1c-9507-5acdc800987f)
![Screenshot 6](https://github.com/user-attachments/assets/62321622-4bfd-4e49-9044-d86d27cceff8)
![Screenshot 7](https://github.com/user-attachments/assets/a0a61741-eaec-4d69-83b6-00539f8facfa)
![Screenshot 8](https://github.com/user-attachments/assets/d99ac31c-7896-492a-937d-953301a0c699)
![Screenshot 9](https://github.com/user-attachments/assets/2fcce398-e621-4bc7-a68d-049eb8d07a27)
![Screenshot 10](https://github.com/user-attachments/assets/537acbc5-3078-42ec-a10e-4afebcaefa74)

</details>

## License

This project is licensed under the MIT License.
