package CloudCourseProject;
;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class Controller {

    @Value("${AuthServiceIp}")
    private String authServiceIp;

    @Value("${AuthServicePort}")
    private String authServicePort;

    @Value("${MasterServiceIp}")
    private String masterServiceIp;

    @Value("${MasterServicePort}")
    private String masterServicePort;


    @GetMapping(value = "/")
    private String getElectionJson(int electionId){
        final String uri = "http://"+masterServiceIp+":"+masterServicePort+"/election/get?electionId="+electionId;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response =restTemplate.getForEntity(uri,String.class);
        }catch (Exception e){
            return null;
        }
        return response.getBody();

    }


    private String getUserRole(String voterEmail){
        System.out.println("entered function");
        final String uri = "http://"+authServiceIp+":"+authServicePort+"/users/getRole";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestJson = "{\"JWT\":\""+ValidatorApplication.module_jwt+"\",\"email\": \""+voterEmail+"\"}";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        ResponseEntity<String> response;
        try {
            response =  restTemplate.exchange(uri, HttpMethod.POST,entity,String.class);
        }catch (Exception e){
            return e.toString();
        }
        ObjectMapper mapper = new ObjectMapper();
        String userrole = null;
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode role = root.path("role");
            userrole = role.asText();
        } catch (IOException e) {
            return e.toString();
        }
        return userrole;
    }


    private boolean hasVoted(int electionId, String voterEmail) {
        final String uri = "http://"+masterServiceIp+":"+masterServicePort+"/vote/exists?electionId="+electionId + "&voterEmail=" + voterEmail;
        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        String s =restTemplate.getForObject(uri,String.class);
        if ("no".equals(s)){
            return false;
        }
        return true;
    }


    private List<Integer> getListOfElectionCandidates(int electionId){
        final String uri = "http://"+masterServiceIp+":"+masterServicePort+"/election/candidate/all?electionId="+electionId;
        RestTemplate restTemplate = new RestTemplate();
        List<Integer> result = restTemplate.getForObject(uri,List.class);
        return result;
    }


    @GetMapping(value = "/validate")
    public String validate(int electionId,int candidateID,String voterEmail,String submitDateString ){
        String jsonString = getElectionJson(electionId);
        if(jsonString == null){
            return "false";
        }
        ObjectMapper mapper = new ObjectMapper();
        Date startDate;
        Date endDate;
        try {
            JsonNode root = mapper.readTree(jsonString);
            JsonNode startDateJson = root.path("startTime");
            JsonNode endDateJson = root.path("endTime");
            String format = "yyyy-MM-dd HH:mm:ss.SZ";
            startDate = new SimpleDateFormat(format).parse(startDateJson.asText().replace("T"," "));
            endDate = new SimpleDateFormat(format).parse(endDateJson.asText().replace("T"," "));
        } catch (Exception e) {
            return e.toString();
        }
        Date currentTime;
        try {
            currentTime= new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(submitDateString);
        } catch (ParseException e) {
            return e.toString();
        }
//        checking if time is ok
        if(currentTime.compareTo(startDate) < 0 || currentTime.compareTo(endDate) > 0 ){
            return "false";
        }

        //check if voterId is user
        if(!"user".equals(getUserRole(voterEmail))){
            return "false";
        }
        System.out.println(3);
        List<Integer> electionCandidates = getListOfElectionCandidates(electionId);
        if(!electionCandidates.contains(candidateID) || electionCandidates.contains(voterEmail)){
            return "false";
        }
        System.out.println(4);
        //check if voter has not voted yet
        if(hasVoted(electionId,voterEmail)){
            return "false";
        }

        return "true";
    }

}
