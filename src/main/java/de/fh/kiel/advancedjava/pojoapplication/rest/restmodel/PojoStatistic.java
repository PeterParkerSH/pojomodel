package de.fh.kiel.advancedjava.pojoapplication.rest.restmodel;


import lombok.*;

import java.util.List;

/**
 * Model for statistical information about a Pojo
 */
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
    List<ApiPojoElement> implementsList;
    int subClassCount;
    int usedAsAttributeCount;
    int numberOfClassesInPackage;
    int numberOfClassesWithName;
}
