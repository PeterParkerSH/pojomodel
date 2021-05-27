package de.fh.kiel.advancedjava.pojomodel.rest.restmodel;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ApiPojoElement {
    String className;
    String packageName;
    public ApiPojoElement(PojoElement pojoElement){
        if (pojoElement == null){
            className = "Object";
            packageName = "java.lang";
        }else {
            className = pojoElement.getName();
            packageName = pojoElement.getPackageName().replace("/", ".");
        }
    }
}
