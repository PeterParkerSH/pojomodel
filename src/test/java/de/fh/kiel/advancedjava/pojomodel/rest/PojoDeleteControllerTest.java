package de.fh.kiel.advancedjava.pojomodel.rest;


import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.model.PojoReference;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PojoDeleteControllerTest {

    @Autowired
    TestDataBaseController testDataBaseController;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PojoElementRepository pojoElementRepository;

    @BeforeEach
    void clearNodes(){
        testDataBaseController.buildTestDataBase();
    }

    @Test
    void pojoDeleteUnknown() throws Exception {
        this.mockMvc.perform(get("/pojoDelete/sdfsdf/sdfdsf")).andExpect(status().is4xxClientError());
    }

    private String buildDeleteRequest(String className, String packageName){
        packageName = packageName.replace("/", ".");
        return "/pojoDelete?package="+packageName+"&name="+className;
    }

    @Test
    void pojoDeleteExisting() throws Exception {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        this.mockMvc.perform(get(buildDeleteRequest("PojoClass4", "testpackage/subpackage"))).andExpect(status().is3xxRedirection());
        this.mockMvc.perform(get(buildDeleteRequest("String", "java/lang"))).andExpect(status().is3xxRedirection());
        assertNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
    }

    @Test
    void pojoDeleteReferenced() throws Exception {
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoClass);
        this.mockMvc.perform(get(buildDeleteRequest("PojoClass2", "testpackage"))).andExpect(status().is3xxRedirection());
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoReference);

    }
}