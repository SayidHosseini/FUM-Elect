package main.CloudCourseProject.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name= "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private List<Election> elections;

    @Column
    private int candidateElectionNumber;

    public Candidate(String name, int candidateElectionNumber) {
        this.name = name;
        this.candidateElectionNumber = candidateElectionNumber;
    }

    public Candidate() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Election> getElections() {
        return elections;
    }

    public void setElections(List<Election> elections) {
        this.elections = elections;
    }

    public int getCandidateElectionNumber() {
        return candidateElectionNumber;
    }

    public void setCandidateElectionNumber(int candidateElectionNumber) {
        this.candidateElectionNumber = candidateElectionNumber;
    }

    public int getId() {
        return id;
    }
}
