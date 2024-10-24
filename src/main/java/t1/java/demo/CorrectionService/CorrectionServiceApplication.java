package t1.java.demo.CorrectionService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CorrectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorrectionServiceApplication.class, args);
	}

}
