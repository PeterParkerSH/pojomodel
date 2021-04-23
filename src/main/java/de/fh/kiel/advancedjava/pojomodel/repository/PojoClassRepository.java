package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoInterface;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PojoClassRepository extends Neo4jRepository<PojoClass, Long> {
    @Query("MATCH (c:Class) WHERE c.className = $className AND c.packageName = $packageName RETURN c")
    List<PojoClass> getPojoClassesByClassNameAndPackageName(@Param("className") String className, @Param("packageName") String packageName);

    @Query("MATCH (c:Class) WHERE c.className = $className AND c.packageName = $packageName RETURN c")
    PojoClass getPojoClassByClassNameAndPackageName(@Param("className") String className, @Param("packageName") String packageName);

    @Query("MATCH (c:Class) WHERE c.className = $className RETURN c")
    PojoClass getPojoClassByClassName(@Param("className") String className);

    @Query("MATCH (i:Interface) WHERE i.interfaceName = $interfaceName AND i.packageName = $packageName RETURN i")
    PojoInterface getPojoInterfaceByInterfaceNameAnAndPackageName(@Param("interfaceName") String interfaceName, @Param("packageName") String packageName);

}
