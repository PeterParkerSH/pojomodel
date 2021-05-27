package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ImportExportControllerTest {
    @Autowired
    private MockMvc mockMvc;

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

    @BeforeEach
    void clearNodes(){
        pojoElementRepository.deleteAll();
    }

    private MockMultipartFile getJsonMockMultipartFileFromResource(String resource) throws URISyntaxException, IOException {
        URL url = this.getClass().getClassLoader().getResource(resource);
        File file = new File(url.toURI());
        FileInputStream fis = new FileInputStream(file);
        return new MockMultipartFile("json", FilenameUtils.getName(url.getFile()), MediaType.APPLICATION_JSON_VALUE, IOUtils.toByteArray(fis));
    }

    @Test
    void jsonImport() throws Exception{
        MockMultipartFile json = getJsonMockMultipartFileFromResource("sampleJson.json");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is3xxRedirection());
        assertFalse(pojoElementRepository.findAll().isEmpty());
        json = getJsonMockMultipartFileFromResource("ExampleJar-1.0-SNAPSHOT.jar");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is4xxClientError());
    }

    @Test
    void jsonExportImport() throws Exception{
        testDataBaseController.buildTestDataBase();
        AtomicReference<String> exportJsonAtomic = new AtomicReference<>("");
        int elementCount = pojoElementRepository.findAll().size();
        this.mockMvc.perform(get("/jsonExport")).andDo(result -> {
            exportJsonAtomic.set(result.getResponse().getContentAsString());
        }).andExpect(status().isOk());

        String exportString = exportJsonAtomic.get();
        assertFalse(exportString.isEmpty());
        pojoElementRepository.deleteAll();

        MockMultipartFile json = new MockMultipartFile("json", FilenameUtils.getName("sampleJson.json"), MediaType.APPLICATION_JSON_VALUE, exportString.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is3xxRedirection());;
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
        MockMultipartFile upload = getJarMockMultipartFileFromResource("rita.jar");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is3xxRedirection());

        int refCount = pojoReferenceRepository.findAll().size();
        int classCount = pojoClassRepository.findAll().size();
        int interfaceCount = pojoInterfaceRepository.findAll().size();

        AtomicReference<String> exportJsonAtomic = new AtomicReference<>("");
        int elementCount = pojoElementRepository.findAll().size();
        this.mockMvc.perform(get("/jsonExport")).andDo(result -> {
            exportJsonAtomic.set(result.getResponse().getContentAsString());
        }).andExpect(status().isOk());

        String exportString = exportJsonAtomic.get();
        assertFalse(exportString.isEmpty());
        pojoElementRepository.deleteAll();

        MockMultipartFile json = new MockMultipartFile("json", FilenameUtils.getName("sampleJson.json"), MediaType.APPLICATION_JSON_VALUE, exportString.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is3xxRedirection());;

        assertEquals(refCount, pojoReferenceRepository.findAll().size());
        assertEquals(classCount, pojoClassRepository.findAll().size());
        assertEquals(interfaceCount, pojoInterfaceRepository.findAll().size());

        assertEquals(pojoElementRepository.findAll().size(), elementCount);

    }

}