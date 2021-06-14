package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PojoClassRepository extends Neo4jRepository<PojoClass, Long> {
    PojoClass findByPackageNameAndName(String packageName, String name);
    
    @Query("MATCH (c:Class) WHERE c.name = $name AND c.packageName = $packageName RETURN c")
    PojoClass getPojoClassByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    @Query("MATCH (c:Class) WHERE c.name = $name DELETE c")
    void deleteAllByName(@Param("name") String name);

    @Query("MATCH (n:Element) WHERE ID(n) = $id REMOVE n:Reference SET n:Class RETURN n")
    PojoClass changeReferenceToClassById(@Param("id") Long id);

    @Query("MATCH (n:Class)-[rel]->(x) WHERE ID(n) = $id REMOVE n:Class DELETE rel")
    void removeClassLabelAndFieldsById(@Param("id") Long id);
}
