package de.fh.kiel.advancedjava.pojomodel.rest.restmodel;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoReference;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportFormat {
    public List<PojoClass> pojoClasses;
    public List<PojoInterface> pojoInterfaces;
    public List<PojoReference> pojoReferences;
}
