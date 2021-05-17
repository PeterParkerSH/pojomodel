package de.fh.kiel.advancedjava.pojomodel.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.neo4j.core.schema.Node;

@AllArgsConstructor
@SuperBuilder
@Node("Reference")
public class PojoReference extends PojoElement{
}
