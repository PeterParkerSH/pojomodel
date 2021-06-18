package de.fh.kiel.advancedjava.pojoapplication.pojomodel;

import lombok.AllArgsConstructor;

import lombok.experimental.SuperBuilder;

import org.springframework.data.neo4j.core.schema.Node;

/**
 * PojoInterface represents a java interface. It cannot have any attribute relationships
 */
@AllArgsConstructor
@SuperBuilder
@Node("Interface")
public class PojoInterface extends PojoElement {

}
