package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiPojoElement;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.PojoStatistic;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@Controller
public class StatisticController {

    final PojoElementRepository pojoElementRepository;

    public StatisticController(PojoElementRepository pojoElementRepository) {
        this.pojoElementRepository = pojoElementRepository;
    }

    @GetMapping(value = "/pojoStatistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String pojoStatistic(@RequestParam("package") String packageName, @RequestParam("name") String className) {
        packageName = packageName.replace(".", "/");
        PojoElement pojoElement = pojoElementRepository.getPojoElementByNameAndPackageName(className, packageName);

        if (pojoElement == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Elemet not fund in database");
        }

        String result = JsonUtils.objectToJsonString(PojoStatistic.builder()
                .className(className)
                .packageName(packageName)
                .attributeCount(pojoElementRepository.getPojoElementsByAttributeRS(pojoElement.getId()).size())
                .extendsClass(new ApiPojoElement(pojoElementRepository.getPojoElementByExtendsRS(pojoElement.getId())))
                .implementsList(pojoElementRepository.getPojoElementsByImplementsRS(pojoElement.getId())
                        .stream()
                        .map(pojoStreamEl -> new ApiPojoElement(pojoStreamEl))
                        .collect(Collectors.toList()))
                .subClassCount(pojoElementRepository.getPojoElementsExtendedById(pojoElement.getId()).size())
                .usedAsAttributeCount(pojoElementRepository.contAttributesOfElementById(pojoElement.getId()))
                .numberOfClassesWithName(pojoElementRepository.countPojoElementsWithClassName(className))
                .numberOfClassesInPackage(pojoElementRepository.countPojoElementsWithPackageName(packageName))
                .build()
        );
        if (result.isEmpty()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating response");
        }
        return result;
    }

}
