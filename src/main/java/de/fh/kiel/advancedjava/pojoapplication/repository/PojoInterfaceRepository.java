package de.fh.kiel.advancedjava.pojoapplication.repository;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoInterface;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * Neo4j repository for Objects of type {@link PojoInterface}
 */
public interface PojoInterfaceRepository extends Neo4jRepository<PojoInterface, Long> {

    /**
     * Change type of given {@link de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoReference} to {@link PojoInterface}
     * @param id id of PojoReference
     * @return PojoInterface
     */
    @Query("MATCH (n:Element) WHERE ID(n) = $id REMOVE n:Reference SET n:Interface RETURN n")
    PojoInterface changeReferenceToInterfaceById(@Param("id") Long id);

    /**
     * Remove the label and fields from a given PojoInterface
     * @param id id of PojoInterface
     */
    @Query("MATCH (n:Interface) WHERE ID(n) = $id REMOVE n:Interface")
    void removeInterfaceLabelAndFieldsById(@Param("id") Long id);

}
