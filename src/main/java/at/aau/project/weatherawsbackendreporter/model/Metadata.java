package at.aau.project.weatherawsbackendreporter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
public class Metadata {

    @NotNull(message = "no location given")
    private final String key;

    public Metadata() {
        this(null);
    }
}
