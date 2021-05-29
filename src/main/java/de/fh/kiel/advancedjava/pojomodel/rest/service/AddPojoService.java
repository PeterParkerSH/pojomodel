package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoReference;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddPojoService {
    final PojoElementRepository pojoElementRepository;
    final PojoClassRepository pojoClassRepository;
    final PojoInterfaceRepository pojoInterfaceRepository;
    final PojoReferenceRepository pojoReferenceRepository;

    public AddPojoService(PojoElementRepository pojoElementRepository, PojoClassRepository pojoClassRepository, PojoInterfaceRepository pojoInterfaceRepository, PojoReferenceRepository pojoReferenceRepository) {
        this.pojoElementRepository = pojoElementRepository;
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
    }

    @Transactional
    public void addPojo(String pojoName, String packageName){
        PojoReference pojo = PojoReference.builder().name(pojoName).packageName(packageName).build();
        pojoReferenceRepository.save(pojo);
    }

    @Transactional
    public boolean checkPojoAlreadyExists(String pojoName, String packageName){
        packageName = packageName.replace(".", "/");
        return pojoElementRepository.getPojoElementByNameAndPackageName(pojoName, packageName) != null;
    }
}
