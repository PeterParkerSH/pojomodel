package de.fh.kiel.advancedjava.pojomodel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PojoApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(PojoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PojoApplication.class, args);
	}

}
