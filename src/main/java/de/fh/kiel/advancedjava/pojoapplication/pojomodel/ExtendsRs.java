package de.fh.kiel.advancedjava.pojoapplication.pojomodel;

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
 * ExtendsRs describes an inheritance relationship between a PojoClass and another PojoClass or PojoReference
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = ExtendsRs.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RelationshipProperties
public class ExtendsRs {
    @Id
    @GeneratedValue
    private Long id;

    @JsonIdentityReference(alwaysAsId = true)
    @TargetNode
    private PojoElement pojoClass;
}
