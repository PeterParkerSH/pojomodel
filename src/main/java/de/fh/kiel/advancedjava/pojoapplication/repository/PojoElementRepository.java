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

    /**
     * Get PojoElement by package name and class name
     * @param packageName package name
     * @param name class name
     * @return PojoElement
     */
    PojoElement findByPackageNameAndName(String packageName, String name);

    /**
     * Get PojoElement by name and package name
     * @param name class name
     * @param packageName package name
     * @return PojoElement
     */
    @Query("MATCH (e:Element) WHERE e.name = $name AND e.packageName = $packageName RETURN e")
    PojoElement getPojoElementByNameAndPackageName(@Param("name") String name, @Param("packageName") String packageName);

    /**
     * Get List of PojoElements that are pointing to another PojoElement with given id
     * @param id id of target PojoElemnt
     * @return {@code List<PojoElement>}
     */
    @Query("match (e:Element)<--(x:Element) where ID(e) = $id  return x")
    List<PojoElement> getPojoElementsByPointingToId(@Param("id") Long id);

    /**
     * Get List of PojoElements that own a given PojoElement as as attribute
     * @param id id of PojoElement
     * @return {@code List<PojoElement>}
     */
    @Query("match (e:Element)-[rel:HAS_ATTRIBUTE]->(x:Element) where ID(e) = $id return x")
    List<PojoElement> getPojoElementsByAttributeRS(@Param("id") Long id);

    /**
     * Get PojoElement that is extended by a given PojoElement
     * @param id id of PojoElement
     * @return PojoElement
     */
    @Query("match (e:Element)-[rel:EXTENDS]->(x:Element) where ID(e) = $id return x")
    PojoElement getPojoElementByExtendsRS(@Param("id") Long id);

    /**
     * Get PojoElements that are implemented by a given PojoElement
     * @param id id of PojoElement
     * @return {@code List<PojoElement>}
     */
    @Query("match (e:Element)-[rel:IMPLEMENTS]->(x:Element) where ID(e) = $id return x")
    List<PojoElement> getPojoElementsByImplementsRS(@Param("id") Long id);

    /**
     * Get List of PojoElements that extend a given PojoElement
     * @param id id of PojoElement
     * @return {@code List<PojoElement>}
     */
    @Query("match (e:Element)<-[rel:EXTENDS]-(x:Element) where ID(e) = $id return x")
    List<PojoElement> getPojoElementsExtendedById(@Param("id") Long id);

    /**
     * Get number of attributes of a given PojoElement
     * @param id id of PojoElement
     * @return count of attributes
     */
    @Query("match (e:Element)<-[rel:HAS_ATTRIBUTE]-(x) where ID(e) = $id return count(rel)")
    int countAttributesOfElementById(@Param("id") Long id);

    /**
     * Get number of PojoElements in a given package
     * @param packageName name of package
     * @return count of PojoElements in package
     */
    @Query("match (e:Element {packageName: $packageName }) return count(e)")
    int countPojoElementsWithPackageName(@Param("packageName") String packageName);

    /**
     * counts all PojoElements with a given name
     * @param name of the PojoElement
     * @return the element count
     */
    @Query("MATCH (e:Element) WHERE e.name = $name RETURN count(e)")
    int countPojoElementsWithClassName(@Param("name") String name);

    /**
     * Lists all PojoElements which have the exact given package name
     * @param packageName the package search path
     * @return all Elements where the package name is packageName
     */
    @Query("match (e:Element {packageName: $packageName }) return e")
    List<PojoElement> getPojoElementsWithPackageName(@Param("packageName") String packageName);

    /**
     * Returns all Pojo Elements by package name starting with the given string
     * @param packageName the package search path
     * @return all Elements where the package name starts with packageName
     */
    @Query("MATCH (e:Element) WHERE e.packageName STARTS WITH $packageName RETURN e")
    List<PojoElement> getPojoElementStartsWithByPackageName(@Param("packageName") String packageName);



}