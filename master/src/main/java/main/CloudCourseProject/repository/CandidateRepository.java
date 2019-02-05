package main.CloudCourseProject.repository;

import main.CloudCourseProject.model.Candidate;
import main.CloudCourseProject.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    public List<Candidate> findByElectionsIn(Election election);
}
