package CloudCourseMicroservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class controller {

    @Value("${MasterServiceIp}")
    private String masterServiceIp;

    @Value("${MasterServicePort}")
    private String masterServicePort;

    @GetMapping(value = "/getElections")
    public String getListOfElections(){
        final String uri = "http://"+masterServiceIp+":"+masterServicePort+"/election/all/";
        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response =restTemplate.getForEntity(uri,String.class);
        }catch (Exception e){
            return e.toString();
        }
        System.out.println("finish");
        return response.getBody();
    }

    @GetMapping(value = "/getElectionCandidates")
    public List<Integer> getListOfElectionCandidates(int electionId){
        final String uri = "http://"+masterServiceIp+":"+masterServicePort+"/election/candidate/all?electionId="+electionId;
        RestTemplate restTemplate = new RestTemplate();
        List<Integer> result;
        try {
             result = restTemplate.getForObject(uri,List.class);
        }catch (Exception e){
            return new ArrayList<>();
        }
        return result;
    }

    @GetMapping(value = "/saveVote")
    public String vote(int electionId,int candidateId,String voterEmail){
        final String uri = "http://"+masterServiceIp+":"+masterServicePort+"/vote/save";
        System.out.println(uri);
        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("electionId", ""+electionId);
        uriParam.put("candidateID", ""+candidateId);
        uriParam.put("voterEmail", voterEmail);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri,String.class,uriParam);
        return result;
    }

}
