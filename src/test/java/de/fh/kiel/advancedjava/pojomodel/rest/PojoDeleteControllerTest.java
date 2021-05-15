package de.fh.kiel.advancedjava.pojomodel.rest;


import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.model.PojoElement;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

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
    @Test
    void pojoDeleteExisting() throws Exception {
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage") != null);
        this.mockMvc.perform(get("/pojoDelete/PojoClass4/testpackage.subpackage")).andExpect(status().isOk());
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage") == null);
    }

    @Test
    void pojoDeleteReferenced() throws Exception {
        this.mockMvc.perform(get("/pojoDelete/PojoClass2/testpackage")).andExpect(status().isOk());
        assertTrue(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage") instanceof PojoClassRepository);

    }
}