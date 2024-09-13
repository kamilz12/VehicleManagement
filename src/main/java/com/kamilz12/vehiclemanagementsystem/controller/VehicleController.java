package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.service.VehicleClientService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/vehicleData")
@Controller
public class VehicleController {
    private final VehicleClientService vehicleClientService;

    public VehicleController(VehicleClientService vehicleClientService) {
        this.vehicleClientService = vehicleClientService;
    }

    @GetMapping("/showFetchPage")
    public String showFetchPage() {
        return "vehicle/vehicle-fetch-page";
    }

    @PostMapping("/fetch")
    private String fetchVehicle(){
        vehicleClientService.fetchAndSaveALlVehiclesDataFromAPI();
        return "redirect:/vehicle/vehicle-fetch-success";
    }
}
