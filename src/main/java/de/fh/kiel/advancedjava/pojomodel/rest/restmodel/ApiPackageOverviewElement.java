package de.fh.kiel.advancedjava.pojomodel.rest.restmodel;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoInterface;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class ApiPackageOverviewElement {
    String className;
    String packageName;
    ApiPackageOverviewTypeEnum type;
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
