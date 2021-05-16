package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.export.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Controller
public class ImportExportController {
    final
    PojoElementRepository pojoElementRepository;
    final
    PojoClassRepository pojoClassRepository;

    final
    PojoInterfaceRepository pojoInterfaceRepository;

    final
    PojoReferenceRepository pojoReferenceRepository;

    public ImportExportController(PojoClassRepository pojoClassRepository, PojoInterfaceRepository pojoInterfaceRepository, PojoReferenceRepository pojoReferenceRepository, PojoElementRepository pojoElementRepository) {
        this.pojoClassRepository = pojoClassRepository;
        this.pojoInterfaceRepository = pojoInterfaceRepository;
        this.pojoReferenceRepository = pojoReferenceRepository;
        this.pojoElementRepository = pojoElementRepository;
    }

    @GetMapping("/jsonExport")
    public @ResponseBody
    String jsonExport() {
        ExportFormat export = new ExportFormat(pojoClassRepository.findAll(), pojoInterfaceRepository.findAll(), pojoReferenceRepository.findAll());

        return JsonUtils.objectToJsonString(export);
    }

    @PostMapping("/jsonImport")
    public @ResponseBody
    ResponseEntity<String> jsonImport(@RequestParam("json") MultipartFile json){
        // todo: ungetestet
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
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
