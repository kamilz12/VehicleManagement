package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.service.UserService;
import com.kamilz12.vehiclemanagementsystem.service.UserVehicleService;
import com.kamilz12.vehiclemanagementsystem.service.VehicleService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vehicle")
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;
    List <Vehicle> vehicles;

    private final UserVehicleService userVehicleService;
    private final UserService userService;
    public VehicleController(VehicleService vehicleService, UserVehicleService userVehicleService, UserService userService) {
        this.vehicleService = vehicleService;
        this.userVehicleService = userVehicleService;
        this.userService = userService;
    }
    @PostConstruct
    public void readAllVehicles(){
        vehicles = vehicleService.findAll();
    }

    @GetMapping("/**")
    public String mainPageVehicle(){
        return "main-vehicle";
    }

    @GetMapping("/createNewVehicle")
    public String createNewVehicle(Model model){
        Vehicle vehicle = new Vehicle();
        Set<String> makes = vehicles.stream()
                .distinct()
                .map(Vehicle::getMake).
                collect(Collectors.toCollection(TreeSet::new));
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("makes", makes);
        return "new-vehicle";
    }

    @GetMapping("/models")
    @ResponseBody
    public ResponseEntity<Set<String>> getModelsByMake(@RequestParam("make") String make) {
        Set <String> modelsSet = vehicles.stream()
                .filter(vehicle -> vehicle.getMake().equals(make))
                .map(Vehicle::getModel)
                .collect(Collectors.toCollection(TreeSet::new));
        return ResponseEntity.ok(modelsSet);
    }
    @GetMapping("/years")
    public ResponseEntity<Set<Integer>> getAvailableYears(@RequestParam String make, @RequestParam String model) {
        Set<Integer> years = vehicles.stream()
                .filter(vehicle -> vehicle.getMake().equals(make))
                .filter(vehicle -> vehicle.getModel().equals(model))
                .map(Vehicle::getYear)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        if (years.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(years);
    }

    @GetMapping("/engines")
    public ResponseEntity<Set<String>> getEnginesByModelAndMake(@RequestParam String make,
                                                                @RequestParam String model,
                                                                @RequestParam(required = false) Integer year) {
        Set <String> engines = vehicles.stream()
                .filter(vehicle -> vehicle.getMake().equals(make))
                .filter(vehicle -> vehicle.getModel().equals(model))
                .filter(vehicle ->  vehicle.getYear().equals(year))
                .map(Vehicle::getEngineName)
                .collect(Collectors.toCollection(TreeSet::new));
        if(engines.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(engines);
    }

    @GetMapping("/internRestId")
    public ResponseEntity<Integer> getInternRestID(@RequestParam String make,
                                                   @RequestParam String model,
                                                   @RequestParam Integer year,
                                                   @RequestParam String engine){
    Integer internRestId = vehicles.stream()
            .filter(vehicle -> vehicle.getMake().equals(make))
            .filter(vehicle -> vehicle.getModel().equals(model))
            .filter(vehicle -> vehicle.getYear().equals(year))
            .filter(vehicle -> vehicle.getEngineName().equals(engine))
            .map(Vehicle::getInternRestId)
            .findFirst().orElse(null);
    if(internRestId==null){
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(internRestId);
    }

    @PostMapping("/processVehicleForm")
    public String processVehicleForm(@ModelAttribute Vehicle vehicle, Model model){
        User user = userService.findUserById(userService.findLoggedUserIdByUsername());
        vehicle.addUser(user);
        vehicle.setInternRestId(55555);
        vehicle.getUserVehicles().forEach(userVehicle -> userVehicle.setMileage(0));
        vehicle.getUserVehicles().forEach(userVehicle -> userVehicle.setVin("12345678901234567"));
        vehicleService.save(vehicle);
        return "vehicle-confirmation";
    }

}
