package at.aau.project.weatherawsbackendreporter.repository;

import at.aau.project.weatherawsbackendreporter.entity.Measurement;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.jpa.repository.JpaRepository;


import java.sql.Timestamp;
import java.util.List;

@EnableScan
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findAllByTemperatureMeasurementPointLocationAndTimestampBetween(String location, Timestamp from, Timestamp to);

    List<Measurement> findAllByTemperatureMeasurementPointLocationAndTimestampBefore(String location, Timestamp to);

    List<Measurement> findAllByTemperatureMeasurementPointLocationAndTimestampAfter(String location, Timestamp from);

    List<Measurement> findAllByTemperatureMeasurementPointLocation(String location);
}