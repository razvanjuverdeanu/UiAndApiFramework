package UI.Utils;

import API.Model.Code;
import API.Model.Geonames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.path.xml.element.Node;
import io.restassured.path.xml.element.NodeChildren;
import io.restassured.response.Response;
import org.awaitility.Awaitility;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Helpers {
    public void waitCondition(Callable<Boolean> condition) {
        Awaitility.await().timeout(10, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(condition);
    }

    public String getRandomValueFromList(List<String> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public List<String> readValuesFromFile(String filePath) throws Exception {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new Exception("file not found");
        }
    }

    public List<String> getPostalCodesFromApiUsingJackson(Response response) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        Map<String, Object> maps = xmlMapper.readValue(response.getBody().asString(), Map.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(maps, Geonames.class).getCode().stream()
                .map(Code::getPostalCode)
                .map(s -> s.replace(" ", ""))
                .map(s -> s.replace("-", ""))
                .collect(Collectors.toList());
    }

    public List<String> getPostalCodesFromApiUsingStream(Response response) {
        return response.body().xmlPath().get().children().list().stream()
                .filter(code -> code.name().equalsIgnoreCase("code"))
                .map(Node::children)
                .map(NodeChildren::list)
                .map(nodes -> nodes.stream().filter(ps -> ps.name().equalsIgnoreCase("postalcode")))
                .flatMap(nodeStream -> nodeStream.map(Node::value))
                .map(s -> s.replace(" ", ""))
                .map(s -> s.replace("-", ""))
                .collect(Collectors.toList());
    }

    public String getCountryCodeBasedOnCountryName(String countryName) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Sweden", "Se");
        map.put("Finland", "Fi");
        map.put("Denmark", "Dk");
        return map.get(countryName);
    }

    public String getPostalCodeWithSpecialCharacters(String postalCode) {
        List<String> specialCharacters = Arrays.asList("&", "*", "%", "!", "?");
        return postalCode + specialCharacters.get(new Random().nextInt(specialCharacters.size()));
    }

}
