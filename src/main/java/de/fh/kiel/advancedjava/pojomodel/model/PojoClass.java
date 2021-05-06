package de.fh.kiel.advancedjava.pojomodel.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@Node("Class")
public class PojoClass extends PojoElement{

    @Builder.Default
    @Relationship(type = "EXTENDS", direction = Relationship.Direction.OUTGOING)
    private ExtendsRs extendsClass = null;

    @Builder.Default
    @Relationship(type = "IMPLEMENTS", direction = Relationship.Direction.OUTGOING)
    private Set<ImplementsRs> implementsInterfaces = new HashSet<>();

}
