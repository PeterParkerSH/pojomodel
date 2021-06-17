package de.fh.kiel.advancedjava.pojomodel.pojomodel;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * AttributeRs describes a attribute relationship between a PojoClass and PojoClass, PojoReference or PojoInterface
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = AttributeRs.class)

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class AttributeRs {
    @Id
    @GeneratedValue
    private Long id;

    @JsonIdentityReference(alwaysAsId = true)
    @TargetNode
    private PojoElement pojoElement;

    private String name;

    private String visibility;
}
