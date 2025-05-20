package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.VehicleClient;
import com.kamilz12.vehiclemanagementsystem.webclient.fueleconomy.service.VehicleClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@RequestMapping("/vehicleData")
@Controller
public class VehicleController {
    private final VehicleClientService vehicleClientService;
    private final VehicleClient vehicleClient;

    // Zmienne do śledzenia procesu
    private final AtomicBoolean fetchInProgress = new AtomicBoolean(false);
    private final AtomicBoolean fetchCompleted = new AtomicBoolean(false);
    private final AtomicInteger fetchProgress = new AtomicInteger(0);
    private String statusMessage = "Inicjalizacja...";

    public VehicleController(VehicleClientService vehicleClientService, VehicleClient vehicleClient) {
        this.vehicleClientService = vehicleClientService;
        this.vehicleClient = vehicleClient;
    }

    @GetMapping("/showFetchPage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showFetchPage() {
        return "vehicle/vehicle-fetch-page";
    }

    @GetMapping("/loading")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showLoadingPage() {
        return "vehicle/vehicle-loading";
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("inProgress", fetchInProgress.get());
        status.put("completed", fetchCompleted.get());
        status.put("progress", fetchProgress.get());
        status.put("message", statusMessage);
        status.put("fetch5Mode", vehicleClient.isFetch5Mode());
        return ResponseEntity.ok(status);
    }

    @PostMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String fetchVehicle() {
        // Przekieruj na stronę ładowania
        CompletableFuture.runAsync(() -> {
            try {
                fetchInProgress.set(true);
                fetchCompleted.set(false);
                fetchProgress.set(0);
                statusMessage = "Rozpoczynanie pobierania danych pojazdów...";

                Thread.sleep(1000);
                fetchProgress.set(20);
                statusMessage = "Pobieranie dostępnych roczników...";

                Thread.sleep(1000);
                fetchProgress.set(40);
                statusMessage = "Pobieranie marek pojazdów...";

                Thread.sleep(1000);
                fetchProgress.set(60);
                statusMessage = "Pobieranie i zapisywanie danych...";

                // Faktyczne pobieranie pojazdów
                var vehicleDTOs = vehicleClient.fetchVehicles();
                var vehicleEntities = vehicleDTOs.stream()
                        .map(vehicleClient::vehicleDTOtoVehicleDAO)
                        .toList();

                vehicleClientService.fetchAndSaveALlVehiclesDataFromAPI();

                fetchProgress.set(100);
                statusMessage = "Zakończono pobieranie i zapisywanie danych.";
                fetchCompleted.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                statusMessage = "Proces został przerwany.";
                fetchCompleted.set(true);
            } catch (Exception e) {
                statusMessage = "Wystąpił błąd: " + e.getMessage();
                fetchCompleted.set(true);
            } finally {
                fetchInProgress.set(false);
            }
        });

        return "redirect:/vehicleData/loading";
    }
}