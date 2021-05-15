package de.fh.kiel.advancedjava.pojomodel.model;

import lombok.AllArgsConstructor;

import lombok.experimental.SuperBuilder;

import org.springframework.data.neo4j.core.schema.Node;

@AllArgsConstructor
@SuperBuilder
@Node("Interface")
public class PojoInterface extends PojoElement {

}
