package de.fh.kiel.advancedjava.pojomodel.pojomodel;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * PojoReference represents a standard java class that was added to the database without its bytecode, either manually or through referencing in another Pojo
 */
@AllArgsConstructor
@SuperBuilder
@Node("Reference")
public class PojoReference extends PojoElement{
}
