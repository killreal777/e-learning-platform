package itmo.blps.elearningplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@EnableConfigurationProperties({ELearningPlatformConfig.class})
public class ELearningPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ELearningPlatformApplication.class, args);
    }

}
