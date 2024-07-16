package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.service.VehicleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService fuelEconomyService) {
        this.vehicleService = fuelEconomyService;
    }

    @GetMapping(value = "/{make}/{model}", produces = "application/json")
    public String getMakeModel(@PathVariable String make, @PathVariable String model){
        return vehicleService.getVehiclesByMakeAndModel(make, model);
    }
}
