package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface PojoClassRepository extends Neo4jRepository<PojoClass, Long> {
}
