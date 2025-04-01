package API.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Component
public class Code {
    @JsonProperty("postalcode")
    String postalCode;
    String name;
    String countryCode;
    String lat;
    String lng;
    @JsonIgnore
    String adminCode1;
    String adminName1;
    String adminCode2;
    String adminName2;
    String adminCode3;
    String adminName3;
}
