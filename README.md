# Vehicle Data Application

This application allows you to download data from government sources via the [Fuel Economy API](https://www.fueleconomy.gov/feg/ws/index.shtml). Using this RestAPI, you can retrieve information such as the make, model, year of manufacture, and engine specifications of all available cars. It is possible to update car database (it will be remanufactured to scheluded update a vehicle data from rest api)
It's possible to create an account with hashed password. After loging to application we can create, update, delete and read a list of cars.

## Features

- Retrieve vehicle data including make, model, year, and engine specifications from the API.
- Select, update delete and add vehicles to your user profile.
- Store vehicle data in a database.
- User-friendly interface to manage your vehicle data.

## TODO
- maintenance CRUD
- calculating fuel consumption based on user data and compare to data from fuel economy api
- interface for receiveing this data
- scheluded vehicle database update and upgrading database based on role of user
## Usage

1. Access the application in your web browser at `http://localhost:8000`.
2. Use the interface to retrieve vehicle data from the Fuel Economy API, key isn't required
3. Select and add vehicles to your profile.
4. Manage your vehicles and maintenance records through the user interface.

## Contributing

We welcome contributions to improve the application. If you have suggestions or find issues, please open an issue or submit a pull request.

## License

This project is licensed under the MIT License.
