package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.controller.FileUploadController;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploadControllerTest {

    @Autowired
    TestDataBaseController testDataBaseController;

    @Autowired
    FileUploadController fileUploadController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PojoElementRepository pojoElementRepository;

    @BeforeEach
    void clearNodes(){
        pojoElementRepository.deleteAll();
    }

    MockMultipartFile getMockMultipartFileFromResource(String resource) throws URISyntaxException, IOException {
        URL url = this.getClass().getClassLoader().getResource(resource);
        File file = new File(url.toURI());
        FileInputStream fis = new FileInputStream(file);
        return new MockMultipartFile("file", FilenameUtils.getName(url.getFile()), "application/octet-stream", IOUtils.toByteArray(fis));
    }

    @Test
    void uploadFile() throws Exception {
        MockMultipartFile upload = getMockMultipartFileFromResource("ExampleJar-1.0-SNAPSHOT.jar");
        assertNotNull(fileUploadController.uploadFile(upload));
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is3xxRedirection());
        assertNotEquals(0, pojoElementRepository.count());
        upload = getMockMultipartFileFromResource("testpackage/PojoClass3.class");
        try{
            fileUploadController.uploadFile(upload);
            fail();
        }catch (ResponseStatusException e){
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is4xxClientError());
        upload = getMockMultipartFileFromResource("testpackage/subpackage/PojoClass5.class");
        assertNotNull(fileUploadController.uploadFile(upload));
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is3xxRedirection());
        assertNotNull( pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass5", "testpackage/subpackage"));
    }

    //@Test
    void uploadLargeJarFile() throws Exception {
        MockMultipartFile upload = getMockMultipartFileFromResource("TheGeneticPoemGenerator.jar");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is3xxRedirection());

    }
}