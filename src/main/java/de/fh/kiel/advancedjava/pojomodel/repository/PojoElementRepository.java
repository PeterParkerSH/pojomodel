package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.AttributeRs;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface PojoElementRepository extends Neo4jRepository<PojoElement, Long> {

    PojoElement findByPackageNameAndName(String name, String packageName);

    @Query("MATCH (e:Element) WHERE e.name = $name AND e.packageName = $packageName RETURN e")
    PojoElement getPojoElementByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    @Query("match (e:Element)<--(x:Element) where ID(e) = $id  return x")
    List<PojoElement> getPojoElementsByPointingToId(@Param("id") Long id);

    @Query("match (e:Element)-[rel:HAS_ATTRIBUTE]->(x:Element) where ID(e) = $id return x")
    List<PojoElement> getPojoElementsByAttributeRS(@Param("id") Long id);

    @Query("match (e:Element)-[rel:EXTENDS]->(x:Element) where ID(e) = $id return x")
    PojoElement getPojoElementByExtendsRS(@Param("id") Long id);

    @Query("match (e:Element)-[rel:IMPLEMENTS]->(x:Element) where ID(e) = $id return x")
    List<PojoElement> getPojoElementsByImplementsRS(@Param("id") Long id);

    @Query("match (e:Element)<-[rel:EXTENDS]-(x:Element) where ID(e) = $id return x")
    List<PojoElement> getPojoElementsExtendedById(@Param("id") Long id);

    @Query("match (e:Element)<-[rel:HAS_ATTRIBUTE]-(x) where ID(e) = $id return count(rel)")
    int contAttributesOfElementById(@Param("id") Long id);

    @Query("match (e:Element {packageName: $packageName }) return count(e)")
    int countPojoElementsWithPackageName(@Param("packageName") String packageName);

    @Query("MATCH (e:Element) WHERE e.name = $name RETURN count(e)")
    int countPojoElementsWithClassName(@Param("name") String name);

}