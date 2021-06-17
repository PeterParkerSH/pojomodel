package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoReference;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * AddPojoService handles adding a PojoReference to the database with only the name and package name of the pojo
 */
@Service
public class AddPojoService {
    final PojoElementRepository pojoElementRepository;
    final PojoReferenceRepository pojoReferenceRepository;

    public AddPojoService(PojoElementRepository pojoElementRepository, PojoReferenceRepository pojoReferenceRepository) {
        this.pojoElementRepository = pojoElementRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
    }

    @Transactional
    public void addPojo(String pojoName, String packageName) throws ResponseStatusException{
        if(pojoElementRepository.getPojoElementByNameAndPackageName(pojoName, packageName) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pojo exists already");
        }
        PojoReference pojo = PojoReference.builder().name(pojoName).packageName(packageName).build();
        pojoReferenceRepository.save(pojo);
    }

}
