package de.fh.kiel.advancedjava.pojoapplication.rest.restmodel;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Model for displaying a Pojo on the index page
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ApiPojoElement {
    String className;
    String packageName;

    /**
     * Constructor for ApiPojoElement
     * @param pojoElement PojoElement to convert to ApiPojoElement
     */
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
