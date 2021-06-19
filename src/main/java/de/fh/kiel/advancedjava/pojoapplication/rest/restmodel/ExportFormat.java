package de.fh.kiel.advancedjava.pojoapplication.rest.restmodel;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoInterface;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoReference;
import lombok.*;

import java.util.List;

/**
 * Model for JSON export of all Pojos in the database
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportFormat {
    private List<PojoClass> pojoClasses;
    private List<PojoInterface> pojoInterfaces;
    private List<PojoReference> pojoReferences;
}
