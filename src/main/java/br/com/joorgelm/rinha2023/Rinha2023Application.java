package br.com.joorgelm.rinha2023;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Rinha2023Application {

	public static void main(String[] args) {
		SpringApplication.run(Rinha2023Application.class, args);
	}

}
