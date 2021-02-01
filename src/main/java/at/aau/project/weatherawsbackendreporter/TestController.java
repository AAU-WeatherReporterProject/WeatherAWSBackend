package at.aau.project.weatherawsbackendreporter;

import at.aau.project.weatherawsbackendreporter.entity.Measurement;
import at.aau.project.weatherawsbackendreporter.entity.TemperatureMeasurementPoint;
import at.aau.project.weatherawsbackendreporter.repository.MeasurementRepository;
import at.aau.project.weatherawsbackendreporter.repository.TemperatureMeasurementPointRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/testdata")
@RequiredArgsConstructor
public class TestController {

    private final MeasurementRepository measurementRepository;
    private final TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;

    @GetMapping
    public ResponseEntity create() {

        TemperatureMeasurementPoint tmp = new TemperatureMeasurementPoint();
        // Do something with tmp
        TemperatureMeasurementPoint upload = temperatureMeasurementPointRepository.save(tmp);

        Measurement mp = new Measurement();
        // Do something with mp
        Measurement uploadM = measurementRepository.save(mp);

        return ResponseEntity.ok().build();
    }
}
