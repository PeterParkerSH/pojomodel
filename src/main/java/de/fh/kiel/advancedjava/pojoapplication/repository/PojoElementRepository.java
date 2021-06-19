package de.fh.kiel.advancedjava.pojoapplication.repository;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoElement;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *  Neo4j repository for Objects of type {@link PojoElement}
 */
public interface PojoElementRepository extends Neo4jRepository<PojoElement, Long> {

    PojoElement findByPackageNameAndName(String packageName, String name);

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

    @Query("match (e:Element {packageName: $packageName }) return e")
    List<PojoElement> getPojoElementsWithPackageName(@Param("packageName") String packageName);

    @Query("MATCH (e:Element) WHERE e.packageName STARTS WITH $packageName RETURN e")
    List<PojoElement> getPojoElementStartsWithByPackageName(@Param("packageName") String packageName);



}