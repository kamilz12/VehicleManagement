package com.kamilz12.vehiclemanagementsystem.service.vehicle;

import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import com.kamilz12.vehiclemanagementsystem.model.vehicle.Vehicle;
import com.kamilz12.vehiclemanagementsystem.repository.vehicle.VehicleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Set<String> findMakes() {
        return vehicleRepository.findAllDistinctMakesASC();
    }

    public Set<String> findModelsByMake(String make) {
        return (make != null) ?
                vehicleRepository.findAllDistinctModelsByMake(make) :
                Collections.emptySet();
    }

    public Set<Integer> findYears(String make, String model) {
        if (make == null || model == null) {
            throw new IllegalArgumentException("Make and model must not be null");
        }
        return vehicleRepository.findAllDistinctYearsByModelAndYear(make, model);
    }

    public Set<String> findEngines(String make, String model, Integer year) {
        if (make == null || model == null || year == null) {
            throw new IllegalArgumentException("Make, model, and year must not be null");
        }
        return vehicleRepository.findAllDistinctEnginesByMakeModelYear(make, model, year);
    }

    public Integer findInternRestId(String make, String model, Integer year, String engine) {
        if (make == null || model == null || year == null || engine == null) {
            throw new IllegalArgumentException("Make, model, year, and engine must not be null");
        }
        return vehicleRepository.findDistinctInternRestIdByMakeModelYearEngineName(make, model, year, engine);
    }

    public Vehicle findByInternRestId(Integer internRestId) {
        return vehicleRepository.findByInternRestId(internRestId);
    }

    public void save(Vehicle vehicle) {
        this.vehicleRepository.save(vehicle);
    }

    /**
     * Pobiera ograniczoną liczbę pojazdów i przekształca je na obiekty VehicleDTO
     *
     * @param limit maksymalna liczba pojazdów do pobrania
     * @return lista obiektów VehicleDTO
     */
    public List<VehicleDTO> fetchLimitedVehicles(int limit) {
        List<Vehicle> vehicles = vehicleRepository.findAll(PageRequest.of(0, limit)).getContent();

        return vehicles.stream()
                .map(vehicle -> VehicleDTO.builder()
                        .year(vehicle.getYear())
                        .make(vehicle.getMake())
                        .model(vehicle.getModel())
                        .engineInternId(vehicle.getInternRestId())
                        .engineName(vehicle.getEngineName())
                        .build())
                .collect(Collectors.toList());
    }
}

