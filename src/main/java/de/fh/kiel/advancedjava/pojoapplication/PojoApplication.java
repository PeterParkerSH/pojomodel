package de.fh.kiel.advancedjava.pojoapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the Entry point for the PojoApplication. The application provides a rest interface to store the class
 * structure of Pojos (Plain Old Java Objects). That includes inheritance and attributes.  Generics and arrays are
 * not considered. To store the data a neo4j graph database is used. The access to the database is configured in the
 * "application.yaml". The database access occurs via repositories. The repositories are located in the package
 * {@link de.fh.kiel.advancedjava.pojoapplication.repository}. The model to store the data structure for the Pojos can be
 * found in {@link de.fh.kiel.advancedjava.pojoapplication.pojomodel}.
 *
 *
 */
@SpringBootApplication
public class PojoApplication {
	public static void main(String[] args) {
		SpringApplication.run(PojoApplication.class, args);
	}

}
