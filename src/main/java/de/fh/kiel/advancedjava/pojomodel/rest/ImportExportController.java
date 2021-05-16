package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.export.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Controller
public class ImportExportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportController.class);


    final PojoElementRepository pojoElementRepository;
    final PojoClassRepository pojoClassRepository;

    final PojoInterfaceRepository pojoInterfaceRepository;

    final PojoReferenceRepository pojoReferenceRepository;

    public ImportExportController(PojoClassRepository pojoClassRepository, PojoInterfaceRepository pojoInterfaceRepository, PojoReferenceRepository pojoReferenceRepository, PojoElementRepository pojoElementRepository) {
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
        this.pojoElementRepository = pojoElementRepository;
    }

    @GetMapping(value = "/jsonExport", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String jsonExport() {
        ExportFormat export = new ExportFormat(pojoClassRepository.findAll(), pojoInterfaceRepository.findAll(), pojoReferenceRepository.findAll());

        return JsonUtils.objectToJsonString(export);
    }

    @GetMapping("/deleteAll")
    public String deleteAll() {
        pojoElementRepository.deleteAll();
        return "redirect:/upload";
    }

    @PostMapping("/jsonImport")
    public String jsonImport(@RequestParam("json") MultipartFile json){
        try {
            InputStream stream = json.getInputStream();
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            jsonString = jsonString.replaceAll("(\r\n)", "" );
            ExportFormat imported = JsonUtils.jsonStringToObject(jsonString, ExportFormat.class);

            pojoElementRepository.deleteAll();

            pojoInterfaceRepository.saveAll(imported.pojoInterfaces);
            pojoReferenceRepository.saveAll(imported.pojoReferences);
            pojoClassRepository.saveAll(imported.pojoClasses);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return "redirect:/upload";
    }
}
