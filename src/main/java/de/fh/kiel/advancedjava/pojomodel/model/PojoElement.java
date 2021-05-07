package de.fh.kiel.advancedjava.pojomodel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

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
