package de.fh.kiel.advancedjava.pojoapplication.rest.controller;


import de.fh.kiel.advancedjava.pojoapplication.TestDataBaseController;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoInterface;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoReference;
import de.fh.kiel.advancedjava.pojoapplication.repository.PojoElementRepository;
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

    @Autowired
    PojoDeleteController pojoDeleteController;

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
        assertNotNull(pojoDeleteController.pojoDelete("testpackage/subpackage", "PojoClass4"));
        assertNotNull(pojoDeleteController.pojoDelete("java/lang", "String"));
        assertNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
    }

    @Test
    void pojoDeleteReferenced() throws Exception {
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoClass);
        pojoDeleteController.pojoDelete("testpackage", "PojoClass2");
        //this.mockMvc.perform(get(buildDeleteRequest("PojoClass2", "testpackage"))).andExpect(status().is3xxRedirection());
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoReference);
        assertFalse(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoClass);
        //this.mockMvc.perform(get(buildDeleteRequest("PojoClass2", "testpackage"))).andExpect(status().is3xxRedirection());
        pojoDeleteController.pojoDelete("testpackage", "PojoClass2");
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoReference);
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoInterface1", "") instanceof PojoInterface);
        pojoDeleteController.pojoDelete("", "PojoInterface1");
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoInterface1", "") instanceof PojoReference);

        //this.mockMvc.perform(get("/deleteAll")).andExpect(status().is3xxRedirection());
        pojoDeleteController.deleteAll();
        assertTrue(pojoElementRepository.findAll().isEmpty());

    }
}