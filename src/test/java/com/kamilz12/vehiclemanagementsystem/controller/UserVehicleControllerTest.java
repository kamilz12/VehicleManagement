package com.kamilz12.vehiclemanagementsystem.controller;

import com.kamilz12.vehiclemanagementsystem.model.vehicle.User;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.UserVehicle;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.service.user.UserService;
import com.kamilz12.vehiclemanagementsystem.service.uservehicle.UserVehicleService;
import com.kamilz12.vehiclemanagementsystem.service.vehicle.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserVehicleControllerTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private UserVehicleService userVehicleService;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private UserVehicleController userVehicleController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mainPageVehicle_ReturnsMainVehiclePage() {
        String viewName = userVehicleController.mainPageVehicle();
        assertEquals("vehicle/main-vehicle", viewName);
    }

    @Test
    void createNewVehicle_ReturnsNewVehicleForm() {
        String viewName = userVehicleController.createNewVehicle(model);
        assertEquals("vehicle/new-vehicle", viewName);
        verify(model).addAttribute(eq("userVehicle"), any(UserVehicle.class));
    }

    @Test
    void getMakes_ReturnsMakes() {
        when(vehicleService.findMakes()).thenReturn(Set.of("Toyota", "Honda"));
        ResponseEntity<Set<String>> response = userVehicleController.getMakes();
        assertEquals(Set.of("Toyota", "Honda"), response.getBody());
    }

    @Test
    void getModelsByMake_ReturnsModels() {
        when(vehicleService.findModelsByMake("Toyota")).thenReturn(Set.of("Camry", "Corolla"));
        ResponseEntity<Set<String>> response = userVehicleController.getModelsByMake("Toyota");
        assertEquals(Set.of("Camry", "Corolla"), response.getBody());
    }

    @Test
    void getAvailableYears_ReturnsYears() {
        when(vehicleService.findYears("Toyota", "Camry")).thenReturn(Set.of(2020, 2021));
        ResponseEntity<Set<Integer>> response = userVehicleController.getAvailableYears("Toyota", "Camry");
        assertEquals(Set.of(2020, 2021), response.getBody());
    }

    @Test
    void getEnginesByModelAndMake_ReturnsEngines() {
        when(vehicleService.findEngines("Toyota", "Camry", 2020)).thenReturn(Set.of("V6", "I4"));
        ResponseEntity<Set<String>> response = userVehicleController.getEnginesByModelAndMake("Toyota", "Camry", 2020);
        assertEquals(Set.of("V6", "I4"), response.getBody());
    }

    @Test
    void getInternRestID_ReturnsInternRestId() {
        when(vehicleService.findInternRestId("Toyota", "Camry", 2020, "V6")).thenReturn(12345);
        ResponseEntity<Integer> response = userVehicleController.getInternRestID("Toyota", "Camry", 2020, "V6");
        assertEquals(12345, response.getBody());
    }

    @Test
    void processVehicleForm_SuccessfulSubmission_ReturnsConfirmation() {
        when(bindingResult.hasErrors()).thenReturn(false);

        Vehicle vehicle = new Vehicle();
        vehicle.setInternRestId(0);
        vehicle.setMake("make");
        vehicle.setModel("model");
        vehicle.setYear(2002);
        vehicle.setEngineName("v8 5.0");
        UserVehicle userVehicle = new UserVehicle();
        userVehicle.setVehicle(vehicle);

        when(vehicleService.findByInternRestId(anyInt())).thenReturn(vehicle);
        when(userService.findUserById(anyLong())).thenReturn(new User());

        String viewName = userVehicleController.processVehicleForm(userVehicle, bindingResult);
        assertEquals("vehicle/vehicle-confirmation", viewName);
        verify(userVehicleService).save(any(UserVehicle.class));
    }

    @Test
    void showUserVehicles_ReturnsUserVehiclesList() {
        when(userService.findUserById(anyLong())).thenReturn(new User());
        when(userVehicleService.findAllByUserId(anyLong())).thenReturn(List.of(new UserVehicle()));
        String viewName = userVehicleController.showUserVehicles(model);
        assertEquals("vehicle/user-vehicles-list", viewName);
        verify(model).addAttribute(eq("userVehicleList"), anyList());
    }

    @Test
    void updateForm_ReturnsNewVehicleForm() {
        UserVehicle userVehicle = new UserVehicle();
        Vehicle vehicle = new Vehicle();
        vehicle.setMake("Toyota");
        vehicle.setModel("Camry");
        vehicle.setYear(2020);
        vehicle.setEngineName("V6");
        userVehicle.setVehicle(vehicle);

        when(userVehicleService.findById(anyLong())).thenReturn(userVehicle);
        when(vehicleService.findMakes()).thenReturn(Set.of("Toyota"));
        when(vehicleService.findModelsByMake("Toyota")).thenReturn(Set.of("Camry"));
        when(vehicleService.findYears("Toyota", "Camry")).thenReturn(Set.of(2020));
        when(vehicleService.findEngines("Toyota", "Camry", 2020)).thenReturn(Set.of("V6"));

        String viewName = userVehicleController.updateForm(1L, model);

        assertEquals("vehicle/new-vehicle", viewName);
        verify(model).addAttribute(eq("userVehicle"), any(UserVehicle.class));
        verify(model).addAttribute(eq("makes"), anySet());
        verify(model).addAttribute(eq("models"), anySet());
        verify(model).addAttribute(eq("years"), anySet());
        verify(model).addAttribute(eq("engines"), anySet());
    }

    @Test
    void deleteUserVehicle_ValidId_ReturnsVehicleDeleted() {
        when(userVehicleService.findById(anyLong())).thenReturn(new UserVehicle());
        String viewName = userVehicleController.deleteUserVehicle(1L, model);
        assertEquals("vehicle/vehicle-deleted", viewName);
        verify(userVehicleService).deleteById(anyLong());
    }

    @Test
    void deleteUserVehicle_InvalidId_ReturnsError() {
        when(userVehicleService.findById(anyLong())).thenReturn(null);
        String viewName = userVehicleController.deleteUserVehicle(1L, model);
        assertEquals("errors/error", viewName);
    }

    @Test
    void showDetails_UserVehicleNotOwnedByUser_ReturnsError() {
        UserVehicle userVehicle = new UserVehicle();
        User user = new User();
        user.setId(1L);
        User differentUser = new User();
        differentUser.setId(2L);

        when(userVehicleService.findById(anyLong())).thenReturn(userVehicle);
        when(userService.findUserById(anyLong())).thenReturn(differentUser);

        String viewName = userVehicleController.showDetails(1L, model);

        assertEquals("errors/error", viewName);
    }

    @Test
    void processVehicleForm_MissingMake_ReturnsNewVehicleFormWithError() {
        when(bindingResult.hasErrors()).thenReturn(false);

        Vehicle vehicle = new Vehicle();
        vehicle.setModel("model");
        vehicle.setYear(2002);
        vehicle.setEngineName("v8 5.0");
        UserVehicle userVehicle = new UserVehicle();
        userVehicle.setVehicle(vehicle);

        String viewName = userVehicleController.processVehicleForm(userVehicle, bindingResult);

        assertEquals("vehicle/new-vehicle", viewName);
        verify(bindingResult).rejectValue(eq("vehicle.make"), anyString(), anyString());
    }

    @Test
    void processVehicleForm_MissingModel_ReturnsNewVehicleFormWithError() {
        when(bindingResult.hasErrors()).thenReturn(false);

        Vehicle vehicle = new Vehicle();
        vehicle.setMake("make");
        vehicle.setYear(2002);
        vehicle.setEngineName("v8 5.0");
        UserVehicle userVehicle = new UserVehicle();
        userVehicle.setVehicle(vehicle);

        String viewName = userVehicleController.processVehicleForm(userVehicle, bindingResult);

        assertEquals("vehicle/new-vehicle", viewName);
        verify(bindingResult).rejectValue(eq("vehicle.model"), anyString(), anyString());
    }

    @Test
    void processVehicleForm_MissingYear_ReturnsNewVehicleFormWithError() {
        when(bindingResult.hasErrors()).thenReturn(false);

        Vehicle vehicle = new Vehicle();
        vehicle.setMake("make");
        vehicle.setModel("model");
        vehicle.setEngineName("v8 5.0");
        UserVehicle userVehicle = new UserVehicle();
        userVehicle.setVehicle(vehicle);

        String viewName = userVehicleController.processVehicleForm(userVehicle, bindingResult);

        assertEquals("vehicle/new-vehicle", viewName);
        verify(bindingResult).rejectValue(eq("vehicle.year"), anyString(), anyString());
    }

    @Test
    void processVehicleForm_MissingEngineName_ReturnsNewVehicleFormWithError() {
        when(bindingResult.hasErrors()).thenReturn(false);

        Vehicle vehicle = new Vehicle();
        vehicle.setMake("make");
        vehicle.setModel("model");
        vehicle.setYear(2002);
        UserVehicle userVehicle = new UserVehicle();
        userVehicle.setVehicle(vehicle);

        String viewName = userVehicleController.processVehicleForm(userVehicle, bindingResult);

        assertEquals("vehicle/new-vehicle", viewName);
        verify(bindingResult).rejectValue(eq("vehicle.engineName"), anyString(), anyString());
    }



}