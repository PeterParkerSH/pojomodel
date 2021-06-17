package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoInterface;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoReference;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * PojoDeleteService manages the deletion of all or single Pojos
 */
@Service
public class PojoDeleteService {

    final PojoElementRepository pojoElementRepository;
    final PojoClassRepository pojoClassRepository;
    final PojoInterfaceRepository pojoInterfaceRepository;
    final PojoReferenceRepository pojoReferenceRepository;

    public PojoDeleteService(PojoElementRepository pojoElementRepository, PojoClassRepository pojoClassRepository, PojoInterfaceRepository pojoInterfaceRepository, PojoReferenceRepository pojoReferenceRepository) {
        this.pojoElementRepository = pojoElementRepository;
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
    }

    @Transactional
    public void deleteAll() {
        pojoElementRepository.deleteAll();
    }

    @Transactional
    public void pojoDelete(String packageName, String className){
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
    }

}
