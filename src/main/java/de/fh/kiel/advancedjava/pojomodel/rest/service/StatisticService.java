package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiPojoElement;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.PojoStatistic;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

/**
 * StatisticService retrieves statistical information about a given Pojo from the database
 */
@Service
public class StatisticService {

    final PojoElementRepository pojoElementRepository;

    public StatisticService(PojoElementRepository pojoElementRepository) {
        this.pojoElementRepository = pojoElementRepository;
    }

    @Transactional
    public PojoStatistic pojoStatistic( String packageName, String className) {
        packageName = packageName.replace(".", "/");
        PojoElement pojoElement = pojoElementRepository.getPojoElementByNameAndPackageName(className, packageName);

        if (pojoElement == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Elemet not fund in database");
        }

        return PojoStatistic.builder()
                .className(className)
                .packageName(packageName)
                .attributeCount(pojoElementRepository.getPojoElementsByAttributeRS(pojoElement.getId()).size())
                .extendsClass(new ApiPojoElement(pojoElementRepository.getPojoElementByExtendsRS(pojoElement.getId())))
                .implementsList(pojoElementRepository.getPojoElementsByImplementsRS(pojoElement.getId())
                        .stream()
                        .map(ApiPojoElement::new)
                        .collect(Collectors.toList()))
                .subClassCount(pojoElementRepository.getPojoElementsExtendedById(pojoElement.getId()).size())
                .usedAsAttributeCount(pojoElementRepository.contAttributesOfElementById(pojoElement.getId()))
                .numberOfClassesWithName(pojoElementRepository.countPojoElementsWithClassName(className))
                .numberOfClassesInPackage(pojoElementRepository.countPojoElementsWithPackageName(packageName))
                .build();
    }
}
