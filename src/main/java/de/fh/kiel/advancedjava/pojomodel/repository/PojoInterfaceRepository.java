package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoInterface;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PojoInterfaceRepository extends Neo4jRepository<PojoInterface, Long> {
    @Query("MATCH (i:Interface) WHERE i.interfaceName = $interfaceName AND i.packageName = $packageName RETURN i")
    PojoInterface getPojoInterfaceByInterfaceNameAndPackageName(@Param("interfaceName") String interfaceName, @Param("packageName") String packageName);

}
