package at.aau.project.weatherawsbackendreporter.service;



import at.aau.project.weatherawsbackendreporter.model.MeasurementPoint;
import at.aau.project.weatherawsbackendreporter.model.TemperatureData;
import at.aau.project.weatherawsbackendreporter.model.TemperatureMeasurement;

import java.util.List;

public interface DataService {
    void ingestData(TemperatureData data);

    List<TemperatureMeasurement> readMeasurements(String from, String to, String location);

    void addMeasurementPoint(MeasurementPoint measurementPoint);

    List<MeasurementPoint> getAllMeasurementPoints();
}