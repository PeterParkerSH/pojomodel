package de.fh.kiel.advancedjava.pojoapplication.rest.restmodel;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Model for displaying information about a package and its content
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ApiPackageOverviewElement {
    String className;
    String packageName;
    ApiPackageOverviewTypeEnum type;

    /**
     * Constructor for ApiPackageOverviewElement
     * @param pojoElement PojoElement to get package overview for
     */
    public ApiPackageOverviewElement(PojoElement pojoElement) {
        if (pojoElement == null) {
            className = "Object";
            packageName = "java.lang";
            type = ApiPackageOverviewTypeEnum.HULL;
        } else {
            className = pojoElement.getName();
            packageName = pojoElement.getPackageName().replace("/", ".");
            type = ApiPackageOverviewTypeEnum.HULL;
            if (pojoElement instanceof PojoInterface) {
                type = ApiPackageOverviewTypeEnum.INTERFACE;
            }
            if (pojoElement instanceof PojoClass) {
                type = ApiPackageOverviewTypeEnum.CLASS;
            }
        }
    }
}
