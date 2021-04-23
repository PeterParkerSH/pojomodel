package de.fh.kiel.advancedjava.pojomodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class ImplementsRs {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private PojoInterface pojoInterface;
}