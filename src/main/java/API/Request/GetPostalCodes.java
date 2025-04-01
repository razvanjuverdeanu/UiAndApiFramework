package API.Request;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class GetPostalCodes {

    public Response getAllPostCodes(String countryName, String countryCode) {
        RestAssured.useRelaxedHTTPSValidation();
        return given()
                .contentType(ContentType.JSON)
                .log()
                .all()
                .when()
                .queryParam("username", "razvanj")
                .queryParam("placename", countryName)
                .queryParam("country", countryCode)
                .get("http://api.geonames.org/postalCodeSearch")
                .thenReturn();
    }

}
