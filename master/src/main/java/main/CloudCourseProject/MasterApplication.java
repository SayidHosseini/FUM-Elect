package main.CloudCourseProject;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MasterApplication {
    public static void main(String[] args) {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SpringApplication.run(MasterApplication.class, args);
    }
}