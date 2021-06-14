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
public class ApiOverviewElement {
    String className;
    String packageName;
    ApiOverviewTypeEnum type;
    public ApiOverviewElement(PojoElement pojoElement) {
        if (pojoElement == null) {
            className = "Object";
            packageName = "java.lang";
            type = ApiOverviewTypeEnum.HULL;
        } else {
            className = pojoElement.getName();
            packageName = pojoElement.getPackageName().replace("/", ".");
            type = ApiOverviewTypeEnum.HULL;
            if (pojoElement instanceof PojoInterface) {
                type = ApiOverviewTypeEnum.INTERFACE;
            }
            if (pojoElement instanceof PojoClass) {
                type = ApiOverviewTypeEnum.CLASS;
            }
        }
    }
    public int getPackageDepth(){
        return packageName.split("\\.").length;
    }
}
