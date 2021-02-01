package at.aau.project.weatherawsbackendreporter.controller;

import at.aau.project.weatherawsbackendreporter.model.MeasurementPoint;
import at.aau.project.weatherawsbackendreporter.model.TemperatureData;
import at.aau.project.weatherawsbackendreporter.model.TemperatureMeasurement;
import at.aau.project.weatherawsbackendreporter.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "v1")
@RequiredArgsConstructor
public class DynamoDBController {

    private DataService dataService;

    public DynamoDBController(@Autowired DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping(value = "/ingest")
    public void ingest(@RequestBody @Valid TemperatureData data) {
        dataService.ingestData(data);
    }

    @PostMapping(value = "/measurementPoint")
    public ResponseEntity<String> addMeasurementPoint(@RequestBody @Nonnull MeasurementPoint measurementPoint) {
        dataService.addMeasurementPoint(measurementPoint);
        return ResponseEntity.ok("Measurement Point was successfully created!");
    }

    @GetMapping(value = "/measurementPoints")
    public List<MeasurementPoint> getMeasurementPoints() {
        return dataService.getAllMeasurementPoints();
    }

    @GetMapping(value = "/dataPoints")
    public List<TemperatureMeasurement> getDataPoints(
            @Param("from") String from, @Param("to") String to, @Param("key") String key) {
        return dataService.readMeasurements(from, to, key);
    }
}
