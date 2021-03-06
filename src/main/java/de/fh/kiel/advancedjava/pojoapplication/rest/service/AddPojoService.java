package de.fh.kiel.advancedjava.pojoapplication.rest.service;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoReference;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoReferenceRepository;
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

    /**
     * Constructor for AddPojoService
     * @param pojoElementRepository see {@link PojoElementRepository}
     * @param pojoReferenceRepository see {@link PojoReferenceRepository}
     */
    public AddPojoService(PojoElementRepository pojoElementRepository, PojoReferenceRepository pojoReferenceRepository) {
        this.pojoElementRepository = pojoElementRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
    }

    /**
     * Add a pojo with class name and package name
     * @param pojoName name of the class
     * @param packageName package of the class
     * @throws ResponseStatusException org.springframework.web.server.ResponseStatusException
     */
    @Transactional
    public void addPojo(String pojoName, String packageName) throws ResponseStatusException{
        if(pojoElementRepository.getPojoElementByNameAndPackageName(pojoName, packageName) != null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pojo exists already");
        }
        PojoReference pojo = PojoReference.builder().name(pojoName).packageName(packageName).build();
        pojoReferenceRepository.save(pojo);
    }

}
