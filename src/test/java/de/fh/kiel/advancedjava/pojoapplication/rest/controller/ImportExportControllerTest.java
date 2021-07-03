package de.fh.kiel.advancedjava.pojoapplication.rest.controller;

import de.fh.kiel.advancedjava.pojoapplication.TestDataBaseController;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.*;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojoapplication.rest.restmodel.ExportFormat;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
    ImportExportController importExportController;

    @Autowired
    FileUploadController fileUploadController;

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
        testDataBaseController.buildTestDataBase();
        MockMultipartFile json = getJsonMockMultipartFileFromResource("sampleJson.json");
        assertNotNull(importExportController.jsonImport(json));
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is3xxRedirection());
        assertFalse(pojoElementRepository.findAll().isEmpty());
        json = getJsonMockMultipartFileFromResource("ExampleJar-1.0-SNAPSHOT.jar");
        try {
            importExportController.jsonImport(json);
            fail();
        }catch (ResponseStatusException e){
            assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getStatus());
        }
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is4xxClientError());
    }

    @Test
    void jsonExportImport() throws Exception{
        testDataBaseController.buildTestDataBase();
        ResponseEntity<ExportFormat> responseEntity = null;
        int elementCount = pojoElementRepository.findAll().size();
        assertNotEquals(0, elementCount);
        responseEntity = importExportController.jsonExport();
        assertEquals(200, responseEntity.getStatusCode().value());

        var resultObject = new Object() {
            String resultStr = "";
        };
        this.mockMvc.perform(get("/jsonExport")).andDo(result -> {
            resultObject.resultStr = result.getResponse().getContentAsString();
        }).andExpect(status().isOk());

        String exportString = resultObject.resultStr;
        assertFalse(exportString.isEmpty());

        MockMultipartFile json = new MockMultipartFile("json", FilenameUtils.getName("sampleJson.json"), MediaType.APPLICATION_JSON_VALUE, exportString.getBytes());
        assertNotNull(importExportController.jsonImport(json));
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
        assertNotNull(fileUploadController.uploadFile(upload));
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is3xxRedirection());

        int refCount = pojoReferenceRepository.findAll().size();
        int classCount = pojoClassRepository.findAll().size();
        int interfaceCount = pojoInterfaceRepository.findAll().size();
        assertNotEquals(0, refCount);
        assertNotEquals(0, classCount);
        assertNotEquals(0, interfaceCount);

        ResponseEntity<ExportFormat> responseEntity = null;
        int elementCount = pojoElementRepository.findAll().size();
        responseEntity = importExportController.jsonExport();
        assertEquals(200, responseEntity.getStatusCode().value());

        var resultObj = new Object() {
            String resultStr = "";
        };
        this.mockMvc.perform(get("/jsonExport")).andDo(result -> {
            resultObj.resultStr = result.getResponse().getContentAsString();
        }).andExpect(status().isOk());

        String exportString = resultObj.resultStr;
        assertFalse(exportString.isEmpty());

        PojoClass pojoClass = pojoClassRepository.getPojoClassByNameAndPackageName("TestClass123", "de/fhkiel/pojo");
        assertNull(pojoClass);
        pojoClass = PojoClass.builder().name("TestClass123").packageName("de/fhkiel/pojo").build();
        pojoClassRepository.save(pojoClass);

        MockMultipartFile json = new MockMultipartFile("json", FilenameUtils.getName("sampleJson.json"), MediaType.APPLICATION_JSON_VALUE, exportString.getBytes());

        assertNotNull(importExportController.jsonImport(json));
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/jsonImport").file(json)).andExpect(status().is3xxRedirection());;

        assertEquals(refCount, pojoReferenceRepository.findAll().size());
        assertEquals(classCount, pojoClassRepository.findAll().size());
        assertEquals(interfaceCount, pojoInterfaceRepository.findAll().size());
        assertEquals(pojoElementRepository.findAll().size(), elementCount);
    }

    void importExportFormant(ExportFormat exportFormat){
        pojoElementRepository.deleteAll();
        List<PojoElement> allElements = new ArrayList<>();
        allElements.addAll(exportFormat.getPojoReferences());
        allElements.addAll(exportFormat.getPojoInterfaces());
        allElements.addAll(exportFormat.getPojoClasses());

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
            pojoElementRepository.save(element);
        }

        for (PojoClass pojoclass: exportFormat.getPojoClasses()){
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

    @Test
    void imexman2(){
        testDataBaseController.buildTestDataBase();

        ExportFormat exportFormat = new ExportFormat(pojoClassRepository.findAll(),pojoInterfaceRepository.findAll(),pojoReferenceRepository.findAll());


        importExportFormant(exportFormat);
        /*
        List<PojoElement> allElements = pojoElementRepository.findAll();
        List<PojoReference> datar1 = pojoReferenceRepository.findAll();
        List<PojoInterface> datai1 = pojoInterfaceRepository.findAll();
        List<PojoClass>     datac1 = pojoClassRepository.findAll();

        pojoElementRepository.deleteAll();


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
            pojoElementRepository.save(element);
        }

        for (PojoClass pojoclass: datac1){
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

*/

        List<PojoElement> data2 = pojoElementRepository.findAll();

        assertEquals(12, data2.size());
    }

    /**
     * Gets Pojo by class name and package name if it already exists, if not, creates new {@link PojoReference}
     * @param className name of the class
     * @param packageName package name of the class
     * @return PojoElement
     */
    public PojoElement getOrCreatePojoElement(String className, String packageName){
        PojoElement pojoElement = pojoElementRepository.findByPackageNameAndName(packageName, className);
        if (pojoElement == null) {
            PojoReference pojoReference = PojoReference.builder().name(className).packageName(packageName).build();
            pojoReferenceRepository.save(pojoReference);
            pojoElement = pojoReference;
        }
        return pojoElement;
    }
    //@Test
    void imexman(){
        testDataBaseController.buildTestDataBase();
        List<PojoElement> data = pojoElementRepository.findAll();
        pojoElementRepository.deleteAll();
        pojoElementRepository.saveAll(data);
        List<PojoElement> data2 = pojoElementRepository.findAll();
        assertEquals(data.size(), data2.size());
    }


}