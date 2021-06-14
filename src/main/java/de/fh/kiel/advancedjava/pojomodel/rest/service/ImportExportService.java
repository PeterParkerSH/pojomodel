package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public String jsonExport() {
        ExportFormat export = new ExportFormat(pojoClassRepository.findAll(), pojoInterfaceRepository.findAll(), pojoReferenceRepository.findAll());
        return JsonUtils.objectToJsonString(export);
    }

    @Transactional
    public void jsonImport(ExportFormat imported){
        pojoElementRepository.deleteAll();

        pojoInterfaceRepository.saveAll(imported.getPojoInterfaces());
        pojoReferenceRepository.saveAll(imported.getPojoReferences());
        pojoClassRepository.saveAll(imported.getPojoClasses());
    }
}
