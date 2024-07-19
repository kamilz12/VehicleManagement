package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.repository.VehicleClientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vehicle")
public class VehicleController {


    @GetMapping("")
    public String mainPageVehicle(){
        return "main-vehicle";
    }
    /*
    @GetMapping("/createNewVehicle")
    public String createNewVehicle(Model model){
        Vehicle vehicle;
        model.addAttribute("vehicleCategory", vehicleClientRepository();
        model.addAttribute("makes", vehicleClientRepository.getMakes());
        model.addAttribute("models", Collections.emptyList());
        model.addAttribute("engines", Collections.emptyList());
        return "new-vehicle";
    }

    @GetMapping("/models")
    @ResponseBody
    public ResponseEntity<List<String>> getModelsByMake(@RequestParam("make") String make, @RequestParam("year") String year) {
        List<String> models = vehicleClientRepository.getModelByMake(make, year);
        return new ResponseEntity<>(models, HttpStatus.OK);
    }
    @GetMapping("/engines")
    public ResponseEntity<Map<Integer, String>> getEnginesByModelAndMake(@RequestParam String make, @RequestParam String model, @RequestParam(required = false) Integer year) {
        Map<Integer, String> engines = vehicleClientRepository.getEngineByMakeAndModel(make, model, year);
        if (engines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(engines);
    }
    /*
    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getAvailableYears(@RequestParam String make, @RequestParam String model) {
        List<Integer> years = vehicleService.findAvailableYearsForMakeAndModel(make, model);
        if (years.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(years);
    }

    @PostMapping("/processVehicleForm")
    public String processVehicleForm(@ModelAttribute VehicleDTO vehicleDTO, Model model){
        String make = vehicleDTO.getMake();
        String vehicleModel = vehicleDTO.getModel();
        Integer engine = vehicleDTO.getEngineInternId();
        Integer year =Integer.parseInt(String.valueOf(vehicleDTO.getYear()));
        String engineName = vehicleDTO.getEngine_name();
        UserVehicle userVehicle = new UserVehicle();
        System.out.println(make);
        System.out.println(vehicleModel);
        System.out.println(year);

        userVehicle.setMake(make);
        userVehicle.setYear(year);
        userVehicle.setModel(vehicleModel);
        userVehicle.setInternRestId(engine);
        userVehicle.setEngineName(engineName);
        vehicleClientRepository.save(userVehicle);
        model.addAttribute("vehicle", vehicleDTO);
        return "vehicle-confirmation";
    }
*/
}
