package com.kamilz12.vehiclemanagementsystem.service.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.kamilz12.vehiclemanagementsystem.dto.VehicleDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
@Slf4j
public class VehicleExportService {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();
    private final YAMLMapper yamlMapper = new YAMLMapper();

    /**
     * Eksportuje listę pojazdów do formatu JSON
     */
    public void exportToJson(List<VehicleDTO> vehicles, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=vehicles.json");

        try (OutputStream outputStream = response.getOutputStream()) {
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, vehicles);
        }
        log.info("Wyeksportowano {} pojazdów do formatu JSON", vehicles.size());
    }

    /**
     * Eksportuje listę pojazdów do formatu XML
     */
    public void exportToXml(List<VehicleDTO> vehicles, HttpServletResponse response) throws IOException {
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=vehicles.xml");

        try (OutputStream outputStream = response.getOutputStream()) {
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, vehicles);
        }
        log.info("Wyeksportowano {} pojazdów do formatu XML", vehicles.size());
    }

    /**
     * Eksportuje listę pojazdów do formatu YAML
     */
    public void exportToYaml(List<VehicleDTO> vehicles, HttpServletResponse response) throws IOException {
        response.setContentType("application/yaml");
        response.setHeader("Content-Disposition", "attachment; filename=vehicles.yaml");

        try (OutputStream outputStream = response.getOutputStream()) {
            yamlMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, vehicles);
        }
        log.info("Wyeksportowano {} pojazdów do formatu YAML", vehicles.size());
    }
}
