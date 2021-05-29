package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddPojoControllerTest {

    @Autowired
    TestDataBaseController testDataBaseController;

    @Autowired
    PojoElementRepository pojoElementRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clearNodes(){
        testDataBaseController.buildTestDataBase();
    }

    private String buildPostRequest(String className, String packageName){
        packageName = packageName.replace("/", ".");
        return "/addPojo?package="+packageName+"&name="+className;
    }

    @Test
    void addPojoNotExisting() throws Exception {
        this.mockMvc.perform(get(buildPostRequest("MyBeautifulClass", "MyBeautifulPackage"))).andExpect(status().is3xxRedirection());
    }

    @Test
    void addPojoExisting() throws Exception {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        this.mockMvc.perform(get(buildPostRequest("PojoClass4", "testpackage/subpackage"))).andExpect(status().isBadRequest());
    }
}