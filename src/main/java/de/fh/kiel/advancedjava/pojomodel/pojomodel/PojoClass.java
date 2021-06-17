package de.fh.kiel.advancedjava.pojomodel.pojomodel;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

/**
 *PojoClass represents a standard java class
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Node("Class")
public class PojoClass extends PojoElement{

    @Builder.Default
    @Relationship(type = "EXTENDS", direction = Relationship.Direction.OUTGOING)
    private ExtendsRs extendsClass = null;

    @Builder.Default
    @Relationship(type = "IMPLEMENTS", direction = Relationship.Direction.OUTGOING)
    private List<ImplementsRs> implementsInterfaces = new ArrayList<>();

    @Builder.Default
    @Relationship(type = "HAS_ATTRIBUTE", direction = Relationship.Direction.OUTGOING)
    private List<AttributeRs> hasAttributes = new ArrayList<>();

}
