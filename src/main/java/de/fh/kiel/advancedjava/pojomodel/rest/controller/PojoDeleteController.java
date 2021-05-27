package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoReference;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/deleteAll")
    public String deleteAll() {
        pojoElementRepository.deleteAll();
        return "redirect:/index";
    }

    @GetMapping("/pojoDelete")
    public String pojoDelete(@RequestParam("package") String packageName, @RequestParam("name") String className){
        packageName = packageName.replace(".", "/");
        PojoElement pojoElement = pojoElementRepository.getPojoElementByNameAndPackageName(className, packageName);
        if (pojoElement == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pojo does not exist");

        long pojoId = pojoElement.getId();

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
        return "redirect:/index";
    }
}
