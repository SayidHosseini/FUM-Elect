package main.CloudCourseProject.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name= "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Candidate candidate;

    @ManyToOne
    private Election election;

    @Column
    private Date submitDate;

//    @Column
//    private int userId;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Column
    private String userEmail;

    public Vote(Candidate candidate, Election election, Date submitDate, String userEmail) {
        this.candidate = candidate;
        this.election = election;
        this.submitDate = submitDate;
//        userId = userId;
        this.userEmail = userEmail;
    }

    public Vote() {
    }

    public int getId() {
        return id;
    }


    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Election getElection() {
        return election;
    }

    public void setElection(Election election) {
        this.election = election;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }


}
