package main.CloudCourseProject.controller;

import main.CloudCourseProject.model.Candidate;
import main.CloudCourseProject.model.Election;
import main.CloudCourseProject.model.Vote;
import main.CloudCourseProject.repository.CandidateRepository;
import main.CloudCourseProject.repository.ElectionRepository;
import main.CloudCourseProject.repository.VotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
public class masterController {


    @Autowired
    ElectionRepository electionRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    VotesRepository votesRepository;

    @Value("${voteSystemIp}")
    private String voteSystemIp;

    @Value("${voteSystemPort}")
    private String voteSystemPort;


    private boolean isValid(Election e,Candidate c,String voterEmail){
        System.out.println("isValid function");
        final String uri = "http://"+voteSystemIp+":"+voteSystemPort+"/validate";
        System.out.println(uri);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> uriParam = new HashMap<>();
        uriParam.put("electionId", ""+e.getId());
        uriParam.put("candidateID", ""+c.getId());
        uriParam.put("voterEmail", voterEmail);
        uriParam.put("submitDateString", new Date().toString());
        String result = restTemplate.getForObject(uri,String.class,uriParam);
        if("true".equals(result)) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/election/get")
    public Election getElectionById(int electionId){
        Optional<Election> e =  electionRepository.findById(electionId);
        if(e.isPresent()){
            return e.get();
        }
        return null;
    }

    @RequestMapping(value = "/candidate/get")
    public Candidate getCandidateById(int candidateId){
        Optional<Candidate> e =  candidateRepository.findById(candidateId);
        if(e.isPresent()){
            return e.get();
        }
        return null;
    }


    @RequestMapping(value = "/election/all")
    public List<Election> getListOfElections(){
        return electionRepository.findAll();
    }

    @RequestMapping(value = "/election/candidate/all")
    public List<Integer> getElectionCandidateList(int electionId){
        Optional<Election> e = electionRepository.findById(electionId);
        List<Candidate> l = candidateRepository.findByElectionsIn(e.get());
        List<Integer> result = new ArrayList<Integer>();
        for(int i=0;i<l.size();i++){
            result.add(l.get(i).getId());
        }
        return result;
    }

    @RequestMapping(value = "/election/save")
    public void saveElection(String name,String startTimeDate,String endTimeDate) throws ParseException {
        System.out.println(name);
        System.out.println(startTimeDate);
        System.out.println(endTimeDate);
        String format = "dd-MM-yyyy HH:mm:ss";
        Date d1 = new SimpleDateFormat(format).parse(startTimeDate);
        Date d2 = new SimpleDateFormat(format).parse(endTimeDate);
        Election e = new Election(name,d1,d2);
        electionRepository.save(e);
    }

    @RequestMapping(value = "/candidate/save")
    public void saveCandidate(String name,int candidateElectionNumber){
        Candidate c = new Candidate(name,candidateElectionNumber);
        candidateRepository.save(c);
    }

    @RequestMapping(value = "/election/candidate/add")
    public String addCandidateToElection(int candidateId,int electionId){
        Optional<Candidate> c = candidateRepository.findById(candidateId);
        Optional<Election> e = electionRepository.findById(electionId);

        if(c.isPresent()){
            if(e.isPresent()) {
                if(c.get().getElections().contains(e.get())){
                 return "candidate already participated";
                }
                c.get().getElections().add(e.get());
                candidateRepository.save(c.get());
                return "ok";
            }
            else {
                return "election not found";
            }
        }
        return "candidate not found";

    }

    @RequestMapping(value = "/election/candidate/delete")
    public String removeCandidateFromElection(int candidateId,int electionId){
        Optional<Candidate> c = candidateRepository.findById(candidateId);
        Optional<Election> e = electionRepository.findById(electionId);

        if(c.isPresent()){
            if(e.isPresent()) {
                if(!c.get().getElections().contains(e.get())){
                    return "candidate has not participated";
                }
                c.get().getElections().remove(e.get());
                candidateRepository.save(c.get());
                return "ok";
            }
            else {
                return "election not found";
            }
        }
        return "candidate not found";
    }


//    @RequestMapping(value = "/election/delete")
//    public void deleteElection(int electionId){
//        electionRepository.deleteById(electionId);
//    }

    @RequestMapping(value = "/candidate/delete")
    public void deleteCandidate(int candidateId){
        candidateRepository.deleteById(candidateId);
    }

    @RequestMapping(value = "/vote/save")//todo
    public String saveVote(int electionId,int candidateId,String voterEmail){
        Optional<Election> election = electionRepository.findById(electionId);
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if(!election.isPresent()){
            return "election not foundt";
        }
        if(!candidate.isPresent()){
            return "candidate not found";
        }
        if(isValid(election.get(),candidate.get(),voterEmail)){

            Date d = new Date();
            Vote v = new Vote(candidate.get(),election.get(), d,voterEmail);
            votesRepository.save(v);
            return "vote successful";
        }
        return "invalid vote";
    }

    @RequestMapping(value = "/vote/exists")
    public String hasVoted(int electionId,String voterEmail){
        System.out.println(electionId);
        System.out.println(voterEmail);
        Optional<Election> e = electionRepository.findById(electionId);
        if(!e.isPresent()){
            return "election not found";
        }
        //should check if user exists
        //todo
        Vote v = votesRepository.findByElectionAndUserEmail(e.get(),voterEmail);
        if(v == null){
            return "no";
        }
        return "yes";
    }


    @RequestMapping(value = "/test")
    public String test() {
        Date date = new Date();

        return "fuck";
    }
//        System.out.println(date);
//        Date d = new Date();
//        return d.toString();

//        // sample dateString
//        //31-Dec-1998 23:37:50
//        String format = "dd-MMM-yyyy HH:mm:ss";
//        try {
//            Date d = new SimpleDateFormat(format).parse(dateString);
//            return d.toString();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
