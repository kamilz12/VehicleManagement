package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.service.user.UserService;
import com.kamilz12.vehiclemanagementsystem.service.uservehicle.UserVehicleService;
import com.kamilz12.vehiclemanagementsystem.service.vehicle.VehicleService;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.service.VehicleClientService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/vehicle")
@Slf4j
public class UserVehicleController {

    private final VehicleService vehicleService;
    List<Vehicle> vehicles;
    private final VehicleClientService vehicleClientService;

    private final UserVehicleService userVehicleService;
    private final UserService userService;


    public UserVehicleController(VehicleService vehicleService, VehicleClientService vehicleClientService, UserVehicleService userVehicleService, UserService userService) {
        this.vehicleClientService = vehicleClientService;
        this.userVehicleService = userVehicleService;
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.vehicles = vehicleService.findAll();

    }

    @GetMapping("**")
    public String mainPageVehicle() {
        return "vehicle/main-vehicle";
    }


    @GetMapping("/createNewVehicle")
    public String createNewVehicle(Model model) {
        UserVehicle userVehicle = new UserVehicle();
        model.addAttribute("userVehicle", userVehicle);
        return "vehicle/new-vehicle";
    }

    @GetMapping("/makes")
    public ResponseEntity<Set<String>> getMakes() {
        Set<String> makes = vehicles.stream()
                .distinct()
                .map(Vehicle::getMake).
                collect(Collectors.toCollection(TreeSet::new));
        return ResponseEntity.ok(makes);
    }

    @GetMapping("/models")
    @ResponseBody
    public ResponseEntity<Set<String>> getModelsByMake(@RequestParam("make") String make) {
        Set<String> modelsSet = vehicles.stream()
                .filter(vehicle -> vehicle.getMake().equals(make))
                .map(Vehicle::getModel)
                .collect(Collectors.toCollection(TreeSet::new));
        return ResponseEntity.ok(modelsSet);
    }

    @GetMapping("/years")
    @ResponseBody
    public ResponseEntity<Set<Integer>> getAvailableYears(@RequestParam String make,
                                                          @RequestParam String model) {
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
    @ResponseBody
    public ResponseEntity<Set<String>> getEnginesByModelAndMake(@RequestParam String make,
                                                                @RequestParam String model,
                                                                @RequestParam Integer year) {
        Set<String> engines = vehicles.stream()
                .filter(vehicle -> vehicle.getMake().equals(make))
                .filter(vehicle -> vehicle.getModel().equals(model))
                .filter(vehicle -> vehicle.getYear().equals(year))
                .map(Vehicle::getEngineName)
                .collect(Collectors.toCollection(TreeSet::new));
        if (engines.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(engines);
    }

    @GetMapping("/internRestId")
    @ResponseBody
    public ResponseEntity<Integer> getInternRestID(@RequestParam String make,
                                                   @RequestParam String model,
                                                   @RequestParam Integer year,
                                                   @RequestParam String engine) {
        Integer internRestId = vehicles.stream()
                .filter(vehicle -> vehicle.getMake().equals(make))
                .filter(vehicle -> vehicle.getModel().equals(model))
                .filter(vehicle -> vehicle.getYear().equals(year))
                .filter(vehicle -> vehicle.getEngineName().equals(engine))
                .map(Vehicle::getInternRestId)
                .findFirst().orElse(null);
        if (internRestId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(internRestId);
    }


    @PostMapping("/processVehicleForm")
    public String processVehicleForm(@Valid @ModelAttribute("userVehicle") UserVehicle userVehicle, BindingResult bindingResult) {
        Vehicle vehicle = userVehicle.getVehicle();
        if (vehicle.getMake() == null || vehicle.getMake().isEmpty()) {
            bindingResult.rejectValue("vehicle.make", "error.vehicle.make", "Make is required");
        }
        if (vehicle.getModel() == null || vehicle.getModel().isEmpty()) {
            bindingResult.rejectValue("vehicle.model", "error.vehicle.model", "Model is required");
        }
        if (vehicle.getYear() == null) {
            bindingResult.rejectValue("vehicle.year", "error.vehicle.year", "Year is required");
        }
        if (vehicle.getEngineName() == null || vehicle.getEngineName().isEmpty()) {
            bindingResult.rejectValue("vehicle.engineName", "error.vehicle.engineName", "Engine name is required");
        }

        if (bindingResult.hasErrors()) {
            return "vehicle/new-vehicle";
        }
        Vehicle vehicleIntern = vehicleService.findByInternRestId(userVehicle.getVehicle().getInternRestId());
        User user = userService.findUserById(userService.findLoggedUserIdByUsername());
        userVehicle.setUser(user);
        userVehicle.setVehicle(vehicleIntern);
        userVehicle.setOwned(true);
        userVehicleService.save(userVehicle);
        return "vehicle/vehicle-confirmation";
    }

    @GetMapping("/showUserVehicles")
    public String showUserVehicles(Model model){
        User user = userService.findUserById(userService.findLoggedUserIdByUsername());
        List <UserVehicle> userVehicles = userVehicleService.findAllByUserId(user.getId());
        model.addAttribute("userVehicleList",userVehicles);
        return "vehicle/user-vehicles-list";
    }


    @GetMapping("/showFormForUpdate")
    String updateForm(@RequestParam("id") long id, Model model){
        UserVehicle userVehicle = userVehicleService.findById(id);
        model.addAttribute("userVehicle", userVehicle);

        // Extract vehicle details
        Vehicle vehicle = userVehicle.getVehicle();

        // Initialize the attributes for dropdowns
        Set<String> makes = vehicles.stream().map(Vehicle::getMake).collect(Collectors.toSet());
        model.addAttribute("makes", makes);

        Set<String> models = vehicles.stream()
                .filter(v -> v.getMake().equals(vehicle.getMake()))
                .map(Vehicle::getModel).collect(Collectors.toSet());
        model.addAttribute("models", models);

        Set<Integer> years = vehicles.stream()
                .filter(v -> v.getMake().equals(vehicle.getMake()))
                .filter(v -> v.getModel().equals(vehicle.getModel()))
                .map(Vehicle::getYear).collect(Collectors.toSet());
        model.addAttribute("years", years);

        Set<String> engines = vehicles.stream()
                .filter(v -> v.getMake().equals(vehicle.getMake()))
                .filter(v -> v.getModel().equals(vehicle.getModel()))
                .filter(v -> v.getYear().equals(vehicle.getYear()))
                .map(Vehicle::getEngineName).collect(Collectors.toSet());
        model.addAttribute("engines", engines);

        return "vehicle/new-vehicle";
    }


    @GetMapping("/deleteUserVehicle")
    String deleteUserVehicle(@RequestParam("id") long id, Model model){
        UserVehicle userVehicle = userVehicleService.findById(id);
        if(userVehicle!=null) {
            model.addAttribute("userVehicle", userVehicle);
            userVehicleService.deleteById(id);
        }
        else{
            return "errors/error";
        }
        return "vehicle/vehicle-deleted";
    }

    @GetMapping("/showVehicleDetails")
    public String showDetails(@RequestParam("id") Long id, Model model) {
        UserVehicle userVehicle = userVehicleService.findById(id);
        User user = userService.findUserById(userService.findLoggedUserIdByUsername());
        if (userVehicle!=null && userVehicle.getUser() ==user) {
            VehicleDTO vehicleDTO = vehicleClientService.fetchFuelConsumptionData(userVehicle.getVehicle().getInternRestId());
            model.addAttribute("vehicleData", vehicleDTO);
            return "vehicle/vehicle-detail";
        }
        else{
            return "errors/error";
        }

    }
}