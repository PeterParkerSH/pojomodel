package de.fh.kiel.advancedjava.pojomodel.rest.restmodel;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoReference;
import lombok.*;

import java.util.List;

/**
 * Data format for JSON export of all Pojos in the database
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportFormat {
    private List<PojoClass> pojoClasses;
    private List<PojoInterface> pojoInterfaces;
    private List<PojoReference> pojoReferences;
}
