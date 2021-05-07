package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoInterface;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PojoInterfaceRepository extends Neo4jRepository<PojoInterface, Long> {
    @Query("MATCH (i:Interface) WHERE i.name = $name AND i.packageName = $packageName RETURN i")
    PojoInterface getPojoInterfaceByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    @Query("MATCH (n:Element) WHERE ID(n) = $id REMOVE n:Reference SET n:Interface RETURN n")
    PojoInterface changeReferenceToInterfaceById(@Param("id") Long id);

}
