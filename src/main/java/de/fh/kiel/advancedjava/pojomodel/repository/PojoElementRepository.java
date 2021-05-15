package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoElement;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;

import java.util.List;

public interface PojoElementRepository extends Neo4jRepository<PojoElement, Long> {
    @Query("MATCH (e:Element) WHERE e.name = $name AND e.packageName = $packageName RETURN e")
    PojoElement getPojoElementByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    @Query("MATCH (c:Class, i:Interface) WHERE e.name = $name AND e.packageName = $packageName RETURN e")
    List<PojoElement> findAllByIdContainingRelation();

    @Query("match (e:Element) where ID(e) = $id optional match (e)<--(x) return e")
    PojoElement getPojoElementByIdIfRelationExists(@Param("id") Long id);


}