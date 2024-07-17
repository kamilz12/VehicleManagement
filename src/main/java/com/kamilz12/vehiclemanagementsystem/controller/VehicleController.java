package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.dto.VehicleCategoryDTO;
import com.kamilz12.vehiclemanagementsystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/createNewVehicle")
    public String createNewVehicle(Model model){
        VehicleCategoryDTO vehicleCategoryDTO = new VehicleCategoryDTO();
        model.addAttribute("vehicleCategory",vehicleCategoryDTO);
        model.addAttribute("makes", vehicleService.getMakes());
        model.addAttribute("models", Collections.emptyList());
        model.addAttribute("engines", Collections.emptyList());
        return "new-vehicle";
    }
    @GetMapping("/models")
    @ResponseBody
    public ResponseEntity<List<String>> getModelsByMake(@RequestParam("make") String make) {
        List<String> models = vehicleService.getModelByMake(make);
        return new ResponseEntity<>(models, HttpStatus.OK);
    }
    @GetMapping("/engines")
    public ResponseEntity<Map<Integer, String>> getEnginesByModelAndMake(@RequestParam String make, @RequestParam String model, @RequestParam(required = false) String year) {
        Map<Integer, String> engines = vehicleService.getEngineByMakeAndModel(make, model, year);
        if (engines.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(engines);
    }
    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getAvailableYears(@RequestParam String make, @RequestParam String model) {
        List<Integer> years = vehicleService.findAvailableYearsForMakeAndModel(make, model);
        if (years.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(years);
    }
}
