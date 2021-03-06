package de.fh.kiel.advancedjava.pojoapplication.pojomodel;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

/**
 * PojoElement is the base class for all POJO classes
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PojoClass.class, name = "PojoClass"),

        @JsonSubTypes.Type(value = PojoInterface.class, name = "PojoInterface"),

        @JsonSubTypes.Type(value = PojoReference.class, name = "PojoReference")
}
)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Node("Element")
public abstract class PojoElement {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String packageName;
}
