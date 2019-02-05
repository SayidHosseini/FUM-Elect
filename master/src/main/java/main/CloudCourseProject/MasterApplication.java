package main.CloudCourseProject;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MasterApplication {
    public static void main(String[] args) {
        System.out.println("goiing to sleep!!!!!!!!");
//		for(int i=0;i<9000000;i++){}
		try{
			Thread.sleep(120000);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("waking up!!!!!!!!!!!!!!");
        SpringApplication.run(MasterApplication.class, args);
    }
}
