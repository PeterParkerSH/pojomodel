package de.fh.kiel.advancedjava.pojoapplication.repository;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoInterface;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PojoInterfaceRepository extends Neo4jRepository<PojoInterface, Long> {
    @Query("MATCH (i:Interface) WHERE i.name = $name AND i.packageName = $packageName RETURN i")
    PojoInterface getPojoInterfaceByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    @Query("MATCH (n:Element) WHERE ID(n) = $id REMOVE n:Reference SET n:Interface RETURN n")
    PojoInterface changeReferenceToInterfaceById(@Param("id") Long id);

    @Query("MATCH (n:Interface) WHERE ID(n) = $id REMOVE n:Interface")
    void removeInterfaceLabelAndFieldsById(@Param("id") Long id);

}
