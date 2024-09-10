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
    private final VehicleClientService vehicleClientService;

    private final UserVehicleService userVehicleService;
    private final UserService userService;


    public UserVehicleController(VehicleService vehicleService, VehicleClientService vehicleClientService, UserVehicleService userVehicleService, UserService userService) {
        this.vehicleClientService = vehicleClientService;
        this.userVehicleService = userVehicleService;
        this.userService = userService;
        this.vehicleService = vehicleService;
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
        return ResponseEntity.ok(vehicleService.findMakes());
    }

    @GetMapping("/models")
    @ResponseBody
    public ResponseEntity<Set<String>> getModelsByMake(@RequestParam("make") String make) {
        return ResponseEntity.ok(vehicleService.findModelsByMake(make));
    }

    @GetMapping("/years")
    @ResponseBody
    public ResponseEntity<Set<Integer>> getAvailableYears(@RequestParam String make, @RequestParam String model) {
        Set<Integer> years = vehicleService.findYears(make, model);
        if (years.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(years);
    }

    @GetMapping("/engines")
    @ResponseBody
    public ResponseEntity<Set<String>> getEnginesByModelAndMake(@RequestParam String make, @RequestParam String model, @RequestParam Integer year) {
        Set<String> engines = vehicleService.findEngines(make, model, year);
        if (engines.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(engines);
    }

    @GetMapping("/internRestId")
    @ResponseBody
    public ResponseEntity<Integer> getInternRestID(@RequestParam String make, @RequestParam String model, @RequestParam Integer year, @RequestParam String engine) {
        Integer internRestId = vehicleService.findInternRestId(make, model, year, engine);
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
    public String showUserVehicles(Model model) {
        User user = userService.findUserById(userService.findLoggedUserIdByUsername());
        List<UserVehicle> userVehicles = userVehicleService.findAllByUserId(user.getId());
        model.addAttribute("userVehicleList", userVehicles);
        return "vehicle/user-vehicles-list";
    }


    @GetMapping("/showFormForUpdate")
    String updateForm(@RequestParam("id") long id, Model model) {
        UserVehicle userVehicle = userVehicleService.findById(id);
        model.addAttribute("userVehicle", userVehicle);

        // Extract vehicle details
        Vehicle vehicle = userVehicle.getVehicle();

        System.out.println(vehicle.getMake());
        System.out.println(vehicle.getModel());
        System.out.println(vehicle.getYear());
        System.out.println(vehicle.getEngineName());
        // Initialize the attributes for dropdowns
        Set<String> makes = getMakes().getBody();
        model.addAttribute("makes", makes);

        Set<String> models = getModelsByMake(vehicle.getMake()).getBody();
        model.addAttribute("models", models);
        Set<Integer> years = getAvailableYears(vehicle.getMake(), vehicle.getModel()).getBody();
        model.addAttribute("years", years);


        Set<String> engines = getEnginesByModelAndMake(vehicle.getMake(), vehicle.getModel(), vehicle.getYear()).getBody();
        model.addAttribute("engines", engines);

        return "vehicle/new-vehicle";
    }


    @GetMapping("/deleteUserVehicle")
    String deleteUserVehicle(@RequestParam("id") long id, Model model) {
        UserVehicle userVehicle = userVehicleService.findById(id);
        if (userVehicle != null) {
            model.addAttribute("userVehicle", userVehicle);
            userVehicleService.deleteById(id);
        } else {
            return "errors/error";
        }
        return "vehicle/vehicle-deleted";
    }

    @GetMapping("/showVehicleDetails")
    public String showDetails(@RequestParam("id") Long id, Model model) {
        UserVehicle userVehicle = userVehicleService.findById(id);
        User user = userService.findUserById(userService.findLoggedUserIdByUsername());
        if (userVehicle != null && userVehicle.getUser() == user) {
            VehicleDTO vehicleDTO = vehicleClientService.fetchFuelConsumptionData(userVehicle.getVehicle().getInternRestId());
            model.addAttribute("vehicleData", vehicleDTO);
            return "vehicle/vehicle-detail";
        } else {
            return "errors/error";
        }

    }
}