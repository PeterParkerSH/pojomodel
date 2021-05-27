package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.service.ImportExportService;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Controller
public class ImportExportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportController.class);

    @Autowired
    ImportExportService importExportService;

    public ImportExportController(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    @GetMapping(value = "/jsonExport", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String jsonExport() {
        return importExportService.jsonExport();
    }

    @PostMapping("/jsonImport")
    public String jsonImport(@RequestParam("json") MultipartFile json){
        try {

            if (!Objects.requireNonNull(json.getOriginalFilename()).endsWith(".json")){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "File extension has to be \".json\"");
            }

            InputStream stream = json.getInputStream();
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            jsonString = jsonString.replaceAll("(\r\n)", "" );
            ExportFormat imported = JsonUtils.jsonStringToObject(jsonString, ExportFormat.class);

            if (imported == null){
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Could not convert json file");
            }

            importExportService.jsonImport(imported);

        } catch (IOException|NullPointerException e) {
            LOGGER.error(e.getMessage());

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Could not convert json file");
        }
        return "redirect:/index";
    }
}
