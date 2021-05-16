package de.fh.kiel.advancedjava.pojomodel.rest;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileUploadControllerTest {

    @Autowired
    TestDataBaseController testDataBaseController;

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
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is2xxSuccessful());
        upload = getMockMultipartFileFromResource("testpackage/PojoClass3.class");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is4xxClientError());
        upload = getMockMultipartFileFromResource("testpackage/subpackage/PojoClass5.class");
        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload").file(upload)).andExpect(status().is2xxSuccessful());
        assertNotNull( pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass5", "testpackage/subpackage"));
    }
}