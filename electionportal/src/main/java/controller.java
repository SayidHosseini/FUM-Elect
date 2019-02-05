import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class controller {

    @Value("${MasterServiceIp}")
    private String masterServiceIp;

    @Value("${MasterServicePort}")
    private String masterServicePort;

    @GetMapping(value = "/getElections")
    public String getListOfElections(){
        final String uri = "http://"+masterServiceIp+":"+masterServicePort+"/election/all/";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response =restTemplate.getForEntity(uri,String.class);
        }catch (Exception e){
            return null;
        }
        return response.getBody();
    }

    @GetMapping(value = "/getElectionCandidates")
    public List<Integer> getListOfElectionCandidates(int electionId){
        final String uri = "http://"+masterServiceIp+":"+masterServicePort+"/election/candidate/all?electionId="+electionId;
        RestTemplate restTemplate = new RestTemplate();
        List<Integer> result = restTemplate.getForObject(uri,List.class);
        return result;
    }

}
