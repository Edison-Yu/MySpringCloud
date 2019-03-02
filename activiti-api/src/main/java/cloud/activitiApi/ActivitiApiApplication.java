package cloud.activitiApi;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApiApplication.class, args);
    }

}
