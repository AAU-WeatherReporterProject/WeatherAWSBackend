package at.aau.project.weatherawsbackendreporter.service;

import at.aau.project.weatherawsbackendreporter.entity.Measurement;
import at.aau.project.weatherawsbackendreporter.entity.TemperatureMeasurementPoint;
import at.aau.project.weatherawsbackendreporter.model.MeasurementPoint;
import at.aau.project.weatherawsbackendreporter.model.TemperatureData;
import at.aau.project.weatherawsbackendreporter.model.TemperatureMeasurement;
import at.aau.project.weatherawsbackendreporter.repository.MeasurementRepository;
import at.aau.project.weatherawsbackendreporter.repository.TemperatureMeasurementPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("DataService")
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final MeasurementRepository measurementRepository;
    private final TemperatureMeasurementPointRepository temperatureMeasurementPointRepository;

    @Override
    public void ingestData(TemperatureData data) {
        List<Measurement> measurementList = new ArrayList<>();
        addMeasurementPointIfNotExists(data.getMetadata().getKey());
        for (TemperatureMeasurement inputMeasurement : data.getMeasurements()) {
            if (inputMeasurement != null) {
                measurementList.add(fillMeasurementFromTemperatureMeasurement(inputMeasurement, data.getMetadata().getKey()));
            }
        }
        measurementRepository.saveAll(measurementList);
    }

    /**
     * if there exists no measurement point for the given location
     * add one.
     *
     * @param location primary key for measurement point
     */
    private void addMeasurementPointIfNotExists(String location) {
        if (!temperatureMeasurementPointRepository.existsById(location)) {
            addMeasurementPoint(new MeasurementPoint(location));
        }
    }

    private Measurement fillMeasurementFromTemperatureMeasurement(TemperatureMeasurement temperatureMeasurement, String location) {
        Measurement measurement = new Measurement();
        measurement.setTemperatureMeasurementPoint(new TemperatureMeasurementPoint(location));
        measurement.setTemperature(temperatureMeasurement.getTemperature());
        measurement.setSky(temperatureMeasurement.getSkyState());
        measurement.setHumidity(temperatureMeasurement.getHumidity());
        measurement.setPressure(temperatureMeasurement.getPressure());
        measurement.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return measurement;
    }


    @Override
    public List<TemperatureMeasurement> readMeasurements(String from, String to, String location) {
        List<TemperatureMeasurement> temperatureMeasurements = new ArrayList<>();
        List<Measurement> measurements;
        if (location == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "No Location given");
        }
        Timestamp timestampFrom = from != null ? Timestamp.valueOf(from) : null;
        Timestamp timestampTo = to != null ? Timestamp.valueOf(to) : null;
        if (timestampFrom != null && timestampTo != null) {
            measurements = measurementRepository.findAllByTemperatureMeasurementPointLocationAndTimestampBetween(location, timestampFrom, timestampTo);
        } else if (timestampFrom != null) {
            measurements = measurementRepository.findAllByTemperatureMeasurementPointLocationAndTimestampAfter(location, timestampFrom);
        } else if (timestampTo != null) {
            measurements = measurementRepository.findAllByTemperatureMeasurementPointLocationAndTimestampBefore(location, timestampTo);
        } else {
            measurements = measurementRepository.findAllByTemperatureMeasurementPointLocation(location);
        }

        measurements.sort(Comparator.comparing(Measurement::getTimestamp).reversed());
        for (Measurement measurement : measurements) {
            temperatureMeasurements.add(new TemperatureMeasurement(measurement.getTemperature(), measurement.getHumidity(),
                    measurement.getPressure(), measurement.getSky(), measurement.getTimestamp().toString()));
        }
        return temperatureMeasurements;
    }

    @Override
    public void addMeasurementPoint(MeasurementPoint measurementPoint) {
        if (measurementPoint == null || measurementPoint.getLocation() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "No Measurement Point Location given");
        }

        if (temperatureMeasurementPointRepository.existsById(measurementPoint.getLocation())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Measurement Point already exists");
        }
        TemperatureMeasurementPoint point = new TemperatureMeasurementPoint();
        point.setLocation(measurementPoint.getLocation());
        temperatureMeasurementPointRepository.saveAndFlush(point);
    }

    @Override
    public List<MeasurementPoint> getAllMeasurementPoints() {
        List<TemperatureMeasurementPoint> temperatureMeasurementPoints = temperatureMeasurementPointRepository.findAll();
        return temperatureMeasurementPoints.stream()
                .map(temp -> new MeasurementPoint(temp.getLocation()))
                .collect(Collectors.toList());
    }
}
