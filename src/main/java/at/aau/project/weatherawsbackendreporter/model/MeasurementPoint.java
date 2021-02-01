package at.aau.project.weatherawsbackendreporter.model;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import org.jetbrains.annotations.Nullable;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class MeasurementPoint {

    @Nullable
    private final String location;

    public MeasurementPoint() {
        this(null);
    }

}
