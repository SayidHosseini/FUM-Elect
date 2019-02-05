package main.CloudCourseProject.repository;

import main.CloudCourseProject.model.Election;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, Integer> {
}
