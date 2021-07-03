package de.fh.kiel.advancedjava.pojoapplication.rest.service;

import de.fh.kiel.advancedjava.pojoapplication.pojomodel.*;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojoapplication.rest.restmodel.ExportFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ImportExportService adds Pojos from JSON file to the database and exports Pojos from the database
 */
@Service
public class ImportExportService {
    final PojoElementRepository pojoElementRepository;
    final PojoClassRepository pojoClassRepository;

    final PojoInterfaceRepository pojoInterfaceRepository;

    final PojoReferenceRepository pojoReferenceRepository;

    /**
     * Constructor for ImportExportService
     * @param pojoClassRepository see {@link PojoClassRepository}
     * @param pojoInterfaceRepository see {@link PojoInterfaceRepository}
     * @param pojoReferenceRepository see {@link PojoReferenceRepository}
     * @param pojoElementRepository see {@link PojoElementRepository}
     */
    public ImportExportService(PojoClassRepository pojoClassRepository, PojoInterfaceRepository pojoInterfaceRepository, PojoReferenceRepository pojoReferenceRepository, PojoElementRepository pojoElementRepository) {
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
        this.pojoElementRepository = pojoElementRepository;
    }

    /**
     * Prepares all Pojos from database for JSON export
     * @return All Pojos in database as {@link ExportFormat}
     */
    @Transactional
    public ExportFormat jsonExport() {
        return new ExportFormat(pojoClassRepository.findAll(), pojoInterfaceRepository.findAll(), pojoReferenceRepository.findAll());
    }

    /**
     * Saves Pojos imported from JSON in the database
     * @param imported Imported Pojos in {@link ExportFormat}
     */
    @Transactional
    public void jsonImport(ExportFormat imported){
        pojoElementRepository.deleteAll();
        List<PojoElement> allElements = new ArrayList<>();
        allElements.addAll(imported.getPojoReferences());
        allElements.addAll(imported.getPojoInterfaces());
        allElements.addAll(imported.getPojoClasses());

        for (PojoElement data: allElements){
            PojoElement element = null;
            if (data instanceof PojoReference) {
                element = PojoReference.builder().name(data.getName()).packageName(data.getPackageName()).build();
            }
            if (data instanceof PojoInterface) {
                element = PojoInterface.builder().name(data.getName()).packageName(data.getPackageName()).build();
            }
            if (data instanceof PojoClass) {
                element = PojoClass.builder().name(data.getName()).packageName(data.getPackageName()).build();
            }
            if (element != null)
                pojoElementRepository.save(element);
        }

        for (PojoClass pojoclass: imported.getPojoClasses()){
            PojoClass realClass = (PojoClass)pojoElementRepository.findByPackageNameAndName(pojoclass.getPackageName(), pojoclass.getName());
            List <AttributeRs> attributeRsList = new ArrayList<>();
            for (AttributeRs attributeRs: pojoclass.getHasAttributes()){
                PojoElement targetNode = pojoElementRepository.findByPackageNameAndName(attributeRs.getPojoElement().getPackageName(), attributeRs.getPojoElement().getName());
                attributeRsList.add(AttributeRs.builder().pojoElement(targetNode).name(attributeRs.getName()).visibility(attributeRs.getVisibility()).build());
            }
            realClass.setHasAttributes(attributeRsList);

            List <ImplementsRs> implementsRsList = new ArrayList<>();
            for (ImplementsRs implementsRs: pojoclass.getImplementsInterfaces()){
                PojoElement targetNode = pojoElementRepository.findByPackageNameAndName(implementsRs.getPojoInterface().getPackageName(), implementsRs.getPojoInterface().getName());
                implementsRsList.add(ImplementsRs.builder().pojoInterface(targetNode).build());
            }
            realClass.setImplementsInterfaces(implementsRsList);

            if (pojoclass.getExtendsClass() != null){
                PojoElement targetNode = pojoElementRepository.findByPackageNameAndName(pojoclass.getExtendsClass().getPojoClass().getPackageName(), pojoclass.getExtendsClass().getPojoClass().getName());
                realClass.setExtendsClass(ExtendsRs.builder().pojoClass(targetNode).build());
            }
            pojoClassRepository.save(realClass);
        }
    }
}
