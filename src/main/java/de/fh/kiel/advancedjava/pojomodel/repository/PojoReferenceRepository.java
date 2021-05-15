package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.model.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.model.PojoReference;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PojoReferenceRepository extends Neo4jRepository<PojoReference, Long> {
    @Query("MATCH (r:Reference) WHERE r.name = $name AND r.packageName = $packageName RETURN r")
    PojoReference getPojoReferenceByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    @Query("MATCH (n:Element) WHERE ID(n) = $id SET n:Reference RETURN n")
    PojoInterface changeElementToReferenceById(@Param("id") Long id);

}
