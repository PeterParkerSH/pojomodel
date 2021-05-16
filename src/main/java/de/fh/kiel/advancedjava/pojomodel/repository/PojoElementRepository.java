package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoElement;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PojoElementRepository extends Neo4jRepository<PojoElement, Long> {
    @Query("MATCH (e:Element) WHERE e.name = $name AND e.packageName = $packageName RETURN e")
    PojoElement getPojoElementByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    @Query("match (e:Element)<--(x:Element) where ID(e) = $id  return x")
    List<PojoElement> getPojoElementsByPointingToId(@Param("id") Long id);


}