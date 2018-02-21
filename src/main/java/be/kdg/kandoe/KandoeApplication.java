package be.kdg.kandoe;

import be.kdg.kandoe.configuration.security.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class KandoeApplication {

//	    @Value("${jwt.expiration_time}")
    private long EXPIRATION_TIME;

//    @Value("${jwt.secret}")
    private String SECRET;

	public KandoeApplication(@Value("${jwt.expiration_time}") long time, @Value("${jwt.secret}") String secret) {
        this.EXPIRATION_TIME = time;
        this.SECRET = secret;
    }

	@Bean
	public FilterRegistrationBean jwtFilter(){
		final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new JwtFilter(SECRET, EXPIRATION_TIME));
		registrationBean.addUrlPatterns("/api/private/*");
		registrationBean.addUrlPatterns("/api/admin/*");
		return registrationBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(KandoeApplication.class, args);
	}
}
