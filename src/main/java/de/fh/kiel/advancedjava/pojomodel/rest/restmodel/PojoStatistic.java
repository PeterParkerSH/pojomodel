package de.fh.kiel.advancedjava.pojomodel.rest.restmodel;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PojoStatistic {
    String className;
    String packageName;
    int attributeCount;
    ApiPojoElement extendsClass;
    List<ApiPojoElement> implementsList = new ArrayList<>();
    int subClassCount;
    int usedAsAttributeCount;
    int numberOfClassesInPackage;
    int numberOfClassesWithName;
}
