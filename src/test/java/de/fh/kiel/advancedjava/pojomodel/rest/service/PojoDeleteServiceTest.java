package de.fh.kiel.advancedjava.pojomodel.rest.service;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoClass;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoReference;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PojoDeleteServiceTest {
    @Autowired
    TestDataBaseController testDataBaseController;
    @Autowired
    PojoDeleteService pojoDeleteService;

    @Autowired
    PojoElementRepository pojoElementRepository;

    @BeforeEach
    void clearNodes(){
        testDataBaseController.buildTestDataBase();
    }



    @Test
    void pojoDeleteExisting() throws Exception {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        pojoDeleteService.pojoDelete( "testpackage/subpackage", "PojoClass4");
        //this.mockMvc.perform(get(buildDeleteRequest("PojoClass4", "testpackage/subpackage"))).andExpect(status().is3xxRedirection());
        pojoDeleteService.pojoDelete("java/lang", "String");
        //this.mockMvc.perform(get(buildDeleteRequest("String", "java/lang"))).andExpect(status().is3xxRedirection());
        assertNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
    }

    @Test
    void pojoDeleteReferenced() throws Exception {
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoClass);
        pojoDeleteService.pojoDelete("testpackage", "PojoClass2");
        //this.mockMvc.perform(get(buildDeleteRequest("PojoClass2", "testpackage"))).andExpect(status().is3xxRedirection());
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoReference);
        assertFalse(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoClass);
        pojoDeleteService.pojoDelete("testpackage", "PojoClass2");
        //this.mockMvc.perform(get(buildDeleteRequest("PojoClass2", "testpackage"))).andExpect(status().is3xxRedirection());
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoReference);
        pojoDeleteService.deleteAll();
        //this.mockMvc.perform(get("/deleteAll")).andExpect(status().is3xxRedirection());
        assertTrue(pojoElementRepository.findAll().isEmpty());

    }
}