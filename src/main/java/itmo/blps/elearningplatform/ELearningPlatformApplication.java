package itmo.blps.elearningplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ELearningPlatformConfig.class})
public class ELearningPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);
    }

}
