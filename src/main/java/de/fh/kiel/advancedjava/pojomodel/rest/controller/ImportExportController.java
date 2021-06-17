package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.rest.service.ImportExportService;
import de.fh.kiel.advancedjava.pojomodel.rest.service.RedirectPageContentService;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.everit.json.schema.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Api(tags = {"Pojo Interface"})
@Controller
public class ImportExportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportController.class);
    final ImportExportService importExportService;
    final RedirectPageContentService redirectPageContentService;

    public ImportExportController(ImportExportService importExportService,
                                  RedirectPageContentService redirectPageContentService) {
        this.importExportService = importExportService;
        this.redirectPageContentService = redirectPageContentService;
    }


    @ApiOperation(value = "Export POJOs as JSON",
            notes = "Export all POJOs in the database as JSON"
    )
    @GetMapping(value = "/jsonExport", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExportFormat> jsonExport() {
        return ResponseEntity.ok(importExportService.jsonExport());
    }

    @ApiOperation(value = "Import POJOs from JSON",
            notes = "Clear database and import all POJOs from JSON, if JSON format is valid"
    )
    @PostMapping(value = "/jsonImport", produces = MediaType.TEXT_HTML_VALUE,
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<String> jsonImport(@ApiParam(value = "JSON file to be imported", required = true) @RequestParam("json") MultipartFile json){
        if (!Objects.requireNonNull(json.getOriginalFilename()).endsWith(".json")){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "File extension has to be \".json\"");
        }

        // convert JSON file to string
        String jsonString;
        try (InputStream stream = json.getInputStream()){
            byte[] buffer = new byte[stream.available()];
            jsonString = "";
            while (stream.read(buffer) > 0) {
                jsonString = new String(buffer, StandardCharsets.UTF_8);

            }
        } catch (IOException|NullPointerException e) {
            LOGGER.error(e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Could not read json file");
        }

        try {
            JsonUtils.validateJSON(jsonString);
        } catch (ValidationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid JSON file: " + e.getMessage());
        }

        ExportFormat imported = JsonUtils.jsonStringToObject(jsonString, ExportFormat.class);
        if (imported == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Could not convert json file to pojo import format");
        }

        importExportService.jsonImport(imported);

        return ResponseEntity.ok(redirectPageContentService.getRedirectPage());
    }
}
