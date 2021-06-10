package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.rest.service.ImportExportService;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Api
@Controller
public class ImportExportController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExportController.class);

    @Autowired
    ImportExportService importExportService;

    public ImportExportController(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }

    @ApiOperation(value = "Export POJOs as JSON",
            notes = "Export all POJOs in the database as JSON",
            response = ExportFormat.class
    )
    @GetMapping(value = "/jsonExport", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String jsonExport() {
        return importExportService.jsonExport();
    }

    @ApiOperation(value = "Import POJOs from JSON",
            notes = "Clear database and import all POJOs from JSON, if JSON format is valid",
            response = RedirectView.class
    )
    @PostMapping("/jsonImport")
    public RedirectView jsonImport(@ApiParam(value = "JSON file to be imported", required = true) @RequestParam("json") MultipartFile json){
        try {

            if (!Objects.requireNonNull(json.getOriginalFilename()).endsWith(".json")){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "File extension has to be \".json\"");
            }

            InputStream stream = json.getInputStream();
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer)
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
        return new RedirectView("/index");
    }
}
