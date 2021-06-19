package de.fh.kiel.advancedjava.pojoapplication.repository;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoInterface;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoReference;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * Neo4j repository for Objects of type {@link PojoReference}
 */
public interface PojoReferenceRepository extends Neo4jRepository<PojoReference, Long> {
    @Query("MATCH (r:Reference) WHERE r.name = $name AND r.packageName = $packageName RETURN r")
    PojoReference getPojoReferenceByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    @Query("MATCH (n:Element) WHERE ID(n) = $id SET n:Reference RETURN n")
    PojoInterface changeElementToReferenceById(@Param("id") Long id);

}
