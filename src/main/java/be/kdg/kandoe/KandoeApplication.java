package be.kdg.kandoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@Configuration
public class KandoeApplication {
	public static void main(String[] args) {
		SpringApplication.run(KandoeApplication.class, args);
	}
}
