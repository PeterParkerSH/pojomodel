package de.fh.kiel.advancedjava.pojomodel.export;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.model.PojoReference;
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
