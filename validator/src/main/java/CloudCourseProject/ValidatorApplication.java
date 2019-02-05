package CloudCourseProject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
public class ValidatorApplication {
    static String module_jwt =null;
    private static void loginToAuth(){

        final String uri = "http://auth:2000/users/login";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = "{\"email\": \"admin@fumelect.com\",\"password\": \"admin\"}";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(uri, HttpMethod.POST,entity,String.class);
        }
        catch (HttpClientErrorException e){
            e.printStackTrace();
            System.out.println("module login failed");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode jwt = root.path("JWT");
            module_jwt = jwt.asText();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("module login failed");
            return;
        }
        System.out.println("module login successful");
    }

    public static void main(String[] args) {
        SpringApplication.run(ValidatorApplication.class, args);
        loginToAuth();

    }
}
