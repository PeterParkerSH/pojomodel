package de.fh.kiel.advancedjava.pojoapplication.rest.service;

import de.fh.kiel.advancedjava.pojoapplication.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojoapplication.rest.restmodel.ExportFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ImportExportService adds Pojos from JSON file to the database and exports Pojos from the database
 */
@Service
public class ImportExportService {
    final PojoElementRepository pojoElementRepository;
    final PojoClassRepository pojoClassRepository;

    final PojoInterfaceRepository pojoInterfaceRepository;

    final PojoReferenceRepository pojoReferenceRepository;

    public ImportExportService(PojoClassRepository pojoClassRepository, PojoInterfaceRepository pojoInterfaceRepository, PojoReferenceRepository pojoReferenceRepository, PojoElementRepository pojoElementRepository) {
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
        this.pojoElementRepository = pojoElementRepository;
    }

    @Transactional
    public ExportFormat jsonExport() {
        return new ExportFormat(pojoClassRepository.findAll(), pojoInterfaceRepository.findAll(), pojoReferenceRepository.findAll());
    }

    @Transactional
    public void jsonImport(ExportFormat imported){
        pojoElementRepository.deleteAll();

        pojoInterfaceRepository.saveAll(imported.getPojoInterfaces());
        pojoReferenceRepository.saveAll(imported.getPojoReferences());
        pojoClassRepository.saveAll(imported.getPojoClasses());
    }
}
