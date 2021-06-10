package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.controller.FileUploadController;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.UploadContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest

class ImportExportServicerTest {

    @Autowired
    ImportExportService importExportService;

    @Autowired
    FileUploadController fileUploadController;

    @Autowired
    PojoElementRepository pojoElementRepository;


    @Autowired
    PojoClassRepository pojoClassRepository;

    @Autowired
    PojoInterfaceRepository pojoInterfaceRepository;

    @Autowired
    PojoReferenceRepository pojoReferenceRepository;

    @Autowired
    TestDataBaseController testDataBaseController;

    ExportFormat exportData;

    ExportFormat getExportData(MockMultipartFile file){
        try {
            InputStream stream = file.getInputStream();
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);
            String s = new String(buffer, StandardCharsets.UTF_8);
            return JsonUtils.jsonStringToObject(s, ExportFormat.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @BeforeEach
    void clearNodes(){
        pojoElementRepository.deleteAll();
        testDataBaseController.buildTestDataBase();
        exportData = new ExportFormat(pojoClassRepository.findAll(), pojoInterfaceRepository.findAll(), pojoReferenceRepository.findAll());
        pojoElementRepository.deleteAll();

    }





    @Test
    void jsonExportImport() throws Exception{
        testDataBaseController.buildTestDataBase();
        String exportJson = "";
        int elementCount = pojoElementRepository.findAll().size();
        assertNotEquals(0, elementCount);
        exportJson = importExportService.jsonExport();


        assertFalse(exportJson.isEmpty());

        MockMultipartFile json = new MockMultipartFile("json", FilenameUtils.getName("sampleJson.json"), MediaType.APPLICATION_JSON_VALUE, exportJson.getBytes());
        importExportService.jsonImport(getExportData(json));
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is3xxRedirection());;
        assertEquals(pojoElementRepository.findAll().size(), elementCount);
    }

    MockMultipartFile getJarMockMultipartFileFromResource(String resource) throws URISyntaxException, IOException {
        URL url = this.getClass().getClassLoader().getResource(resource);
        File file = new File(url.toURI());
        FileInputStream fis = new FileInputStream(file);
        return new MockMultipartFile("file", FilenameUtils.getName(url.getFile()), "application/octet-stream", IOUtils.toByteArray(fis));
    }

    @Test
    void ExportImportLarge() throws Exception {
        MockMultipartFile upload = getJarMockMultipartFileFromResource("ExampleJar-1.0-SNAPSHOT.jar");
        fileUploadController.uploadFile(upload);
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is3xxRedirection());

        int refCount = pojoReferenceRepository.findAll().size();
        int classCount = pojoClassRepository.findAll().size();
        int interfaceCount = pojoInterfaceRepository.findAll().size();
        assertNotEquals(0, refCount);
        assertNotEquals(0, classCount);
        assertNotEquals(0, interfaceCount);

        AtomicReference<String> exportJsonAtomic = new AtomicReference<>("");
        int elementCount = pojoElementRepository.findAll().size();
        exportJsonAtomic.set(importExportService.jsonExport());


        String exportString = exportJsonAtomic.get();
        assertFalse(exportString.isEmpty());

        PojoClass pojoClass = pojoClassRepository.getPojoClassByNameAndPackageName("TestClass123", "de/fhkiel/pojo");
        assertNull(pojoClass);
        pojoClass = PojoClass.builder().name("TestClass123").packageName("de/fhkiel/pojo").build();
        pojoClassRepository.save(pojoClass);

        MockMultipartFile json = new MockMultipartFile("json", FilenameUtils.getName("sampleJson.json"), MediaType.APPLICATION_JSON_VALUE, exportString.getBytes());

        importExportService.jsonImport(getExportData(json));
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is3xxRedirection());;

        assertEquals(refCount, pojoReferenceRepository.findAll().size());
        assertEquals(classCount, pojoClassRepository.findAll().size());
        assertEquals(interfaceCount, pojoInterfaceRepository.findAll().size());

        assertEquals(pojoElementRepository.findAll().size(), elementCount);

    }

}