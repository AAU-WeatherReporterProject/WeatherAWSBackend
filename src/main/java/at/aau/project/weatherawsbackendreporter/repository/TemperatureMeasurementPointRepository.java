package at.aau.project.weatherawsbackendreporter.repository;

import at.aau.project.weatherawsbackendreporter.entity.TemperatureMeasurementPoint;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.jpa.repository.JpaRepository;

@EnableScan
public interface TemperatureMeasurementPointRepository extends JpaRepository<TemperatureMeasurementPoint, String> {

}

