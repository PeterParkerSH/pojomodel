package de.fh.kiel.advancedjava.pojoapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the SpringBoot project
 */
@SpringBootApplication
public class PojoApplication {
	/**
	 * Application entry routine
	 * @param args command line parameters
	 */
	public static void main(String[] args) {
		SpringApplication.run(PojoApplication.class, args);
	}

}
