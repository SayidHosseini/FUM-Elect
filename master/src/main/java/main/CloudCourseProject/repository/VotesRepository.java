package main.CloudCourseProject.repository;

import main.CloudCourseProject.model.Election;
import main.CloudCourseProject.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotesRepository extends JpaRepository<Vote, Integer> {
//    Vote findByElectionAndUserId(Election e, int userId);
    Vote findByElectionAndUserEmail(Election e, String userEmail);
}
