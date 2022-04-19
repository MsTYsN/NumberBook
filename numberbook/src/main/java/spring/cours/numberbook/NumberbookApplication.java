package spring.cours.numberbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;


@SpringBootApplication
@OpenAPIDefinition
public class NumberbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(NumberbookApplication.class, args);
	}

}
