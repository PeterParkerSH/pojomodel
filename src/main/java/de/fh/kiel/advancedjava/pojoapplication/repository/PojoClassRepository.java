package de.fh.kiel.advancedjava.pojoapplication.repository;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoClass;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * Neo4j repository for Objects of type {@link PojoClass}
 */
public interface PojoClassRepository extends Neo4jRepository<PojoClass, Long> {

    /**
     * Finds a PojoClass by package and class name
     * @param packageName of the Pojo
     * @param name of the Pojo
     * @return PojoClass or null
     */
    PojoClass findByPackageNameAndName(String packageName, String name);

    /**
     * Finds a PojoClass by package and class name without attributes etc.
     * @param packageName of the Pojo
     * @param name of the Pojo
     * @return PojoClass or null
     */
    @Query("MATCH (c:Class) WHERE c.name = $name AND c.packageName = $packageName RETURN c")
    PojoClass getPojoClassByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    /**
     * Changes an existing PojoReference to a PojoClass
     * @param id of the PojoElement
     * @return The PojoElement or null
     */
    @Query("MATCH (n:Element) WHERE ID(n) = $id REMOVE n:Reference SET n:Class RETURN n")
    PojoClass changeReferenceToClassById(@Param("id") Long id);

    /**
     * Turns a PojoClass to a PojoElement by ID
     * @param id of the pojo class
     */
    @Query("MATCH (n:Class)-[rel]->(x) WHERE ID(n) = $id REMOVE n:Class DELETE rel")
    void removeClassLabelAndFieldsById(@Param("id") Long id);
}
