package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ImportExportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportService.class);


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
    public String jsonImport(ExportFormat imported){
        pojoElementRepository.deleteAll();

        pojoInterfaceRepository.saveAll(imported.pojoInterfaces);
        pojoReferenceRepository.saveAll(imported.pojoReferences);
        pojoClassRepository.saveAll(imported.pojoClasses);

        return "redirect:/index";
    }
}
