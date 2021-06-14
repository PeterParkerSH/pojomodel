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
    public void addAttribute(long id, String type, String name, String visibility, String packageName) {
        Optional<PojoClass> optionalPojoClass = pojoClassRepository.findById(id);
        PojoClass pojoClass;
        if (optionalPojoClass.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "POJO does not exist or has incompatible type"
            );
        } else {
            pojoClass = optionalPojoClass.get();
        }
        List<AttributeRs> attributes = pojoClass.getHasAttributes();
        if (attributes.stream().anyMatch(ars -> ars.getName().equals(name))) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "POJO already has attribute " + name
            );
        }

        PojoElement typePojo;
        if ((type.equals(Boolean.class.getSimpleName())
                || type.equals(Byte.class.getSimpleName())
                || type.equals(Short.class.getSimpleName())
                || type.equals(Integer.class.getSimpleName())
                || type.equals(Long.class.getSimpleName())
                || type.equals(Float.class.getSimpleName())
                || type.equals(Double.class.getSimpleName())
                || type.equals(Character.class.getSimpleName()))
                && (packageName.isEmpty() || packageName.equalsIgnoreCase("java/lang"))) {
            typePojo = classHandlingService.getOrCreatePojoElement(type, "java/lang");
        } else if (!packageName.isEmpty()){
            typePojo = classHandlingService.getOrCreatePojoElement(type, packageName);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Invalid type: " + type);
        }
        AttributeRs newAttribute = AttributeRs.builder().visibility(visibility).name(name).pojoElement(typePojo).build();
        attributes.add(newAttribute);
        pojoClass.setHasAttributes(attributes);
        pojoClassRepository.save(pojoClass);
    }
}
