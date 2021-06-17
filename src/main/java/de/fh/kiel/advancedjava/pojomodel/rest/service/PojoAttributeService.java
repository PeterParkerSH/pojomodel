package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.*;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * PojoAttributeService manages adding and removing attributes to and from Pojos
 */
@Service
public class PojoAttributeService {
    final PojoElementRepository pojoElementRepository;
    final PojoReferenceRepository pojoReferenceRepository;
    final PojoClassRepository pojoClassRepository;
    final ClassHandlingService classHandlingService;

    public PojoAttributeService(PojoElementRepository pojoElementRepository, PojoReferenceRepository pojoReferenceRepository, PojoClassRepository pojoClassRepository, ClassHandlingService classHandlingService) {
        this.pojoElementRepository = pojoElementRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
        this.pojoClassRepository = pojoClassRepository;
        this.classHandlingService = classHandlingService;
    }

    @Transactional
    public void addAttribute(String pojoPackage,String pojoName, String type, String name, String visibility, String packageName) {
        PojoElement pojoElement = pojoElementRepository.findByPackageNameAndName(pojoPackage, pojoName);
        if (pojoElement == null || pojoElement instanceof PojoInterface){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "POJO does not exist or has incompatible type"
            );
        }
        if (pojoElement instanceof PojoReference) {
            pojoElement = pojoClassRepository.changeReferenceToClassById(pojoElement.getId());
        }
        PojoClass pojoClass;
        Optional<PojoClass> optionalPojoClass = pojoClassRepository.findById(pojoElement.getId());
        if (optionalPojoClass.isPresent()) {
            pojoClass = optionalPojoClass.get();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "POJO does not exist"
            );
        }
        List<AttributeRs> attributes = pojoClass.getHasAttributes();
        if (attributes.stream().anyMatch(ars -> ars.getName().equals(name))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "POJO already has attribute " + name
            );
        }

        PojoElement typePojo;
        typePojo = classHandlingService.getOrCreatePojoElement(type, packageName);

        AttributeRs newAttribute = AttributeRs.builder().visibility(visibility).name(name).pojoElement(typePojo).build();
        attributes.add(newAttribute);
        pojoClass.setHasAttributes(attributes);
        pojoClassRepository.save(pojoClass);
    }

    @Transactional
    public void removeAttribute(String pojoPackage,String pojoName, String name){
        PojoClass pojoClass = pojoClassRepository.findByPackageNameAndName(pojoPackage, pojoName);
        if (pojoClass == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "POJO does not exist or has incompatible type"
            );
        }
        List<AttributeRs> attributes = pojoClass.getHasAttributes();
        boolean containsAttr =  attributes.removeIf(attributeRs -> attributeRs.getName().equals(name));
        if (!containsAttr) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "POJO does not have attribute with name: " + name
            );
        }
        pojoClass.setHasAttributes(attributes);
        pojoClassRepository.save(pojoClass);
    }
}
