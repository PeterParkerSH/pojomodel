package de.fh.kiel.advancedjava.pojomodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Node("Class")
public class PojoClass {
    @Id
    @GeneratedValue
    private Long id;
    private String className;
    private String packageName;

    @Relationship(type = "EXTENDS", direction = Relationship.Direction.OUTGOING)
    private ExtendsRs extendsClass = null;

    @Relationship(type = "IMPLEMENTS", direction = Relationship.Direction.OUTGOING)
    private Set<ImplementsRs> implementsInterfaces = new HashSet<>();

}
