package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.model.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.model.PojoReference;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PojoDeleteController {

    final PojoElementRepository pojoElementRepository;
    final PojoClassRepository pojoClassRepository;
    final PojoInterfaceRepository pojoInterfaceRepository;
    final PojoReferenceRepository pojoReferenceRepository;

    public PojoDeleteController(PojoElementRepository pojoElementRepository, PojoClassRepository pojoClassRepository, PojoInterfaceRepository pojoInterfaceRepository, PojoReferenceRepository pojoReferenceRepository) {
        this.pojoElementRepository = pojoElementRepository;
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
    }

    @GetMapping("/pojoDelete/{className}/{packageName}")
    public @ResponseBody ResponseEntity<String> pojoDelete(@PathVariable String className, @PathVariable String packageName){
        packageName = packageName.replace(".", "/");
        PojoElement pojoElement = pojoElementRepository.getPojoElementByNameAndPackageName(className, packageName);
        if (pojoElement == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pojo does not exist");

        long pojoId = pojoElement.getId();

        // Todo: Not Working!!
        if (pojoElementRepository.getPojoElementsByPointingToId(pojoElement.getId()).isEmpty()){
            // No relation exists -> Delete Node
            pojoElementRepository.delete(pojoElement);
        }else{
            // Turn node to Reference
            if (!(pojoElement instanceof PojoReference)){
                if (pojoElement instanceof PojoClass){
                    pojoClassRepository.removeClassLabelAndFieldsById(pojoId);
                }else if (pojoElement instanceof PojoInterface){
                    pojoInterfaceRepository.removeInterfaceLabelAndFieldsById(pojoId);
                }
                pojoElement = pojoReferenceRepository.changeElementToReferenceById(pojoId);
                if (pojoElement == null){
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not change element type");
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
