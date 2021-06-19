package de.fh.kiel.advancedjava.pojoapplication.repository;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoReference;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * Neo4j repository for Objects of type {@link PojoReference}
 */
public interface PojoReferenceRepository extends Neo4jRepository<PojoReference, Long> {

    /**
     * Change type of existing PojoElement to PojoReference
     * @param id id of PojoElement
     * @return the changed Element of null
     */
    @Query("MATCH (n:Element) WHERE ID(n) = $id SET n:Reference RETURN n")
    PojoReference changeElementToReferenceById(@Param("id") Long id);

}
