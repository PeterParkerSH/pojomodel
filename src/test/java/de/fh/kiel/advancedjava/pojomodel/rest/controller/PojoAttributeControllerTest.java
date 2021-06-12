package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PojoAttributeControllerTest {
    @Autowired
    TestDataBaseController testDataBaseController;

    @Autowired
    PojoElementRepository pojoElementRepository;

    @Autowired
    PojoClassRepository pojoClassRepository;

    @Autowired
    PojoAttributeController pojoAttributeController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clearNodes(){
        testDataBaseController.buildTestDataBase();
    }

    @Test
    void addReferenceAttributeToExistingPojo() {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        long id = pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage").getId();
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage"));
        assertNotNull(pojoAttributeController.addAttribute(id, "PojoClass2", "attrPojoClass2", "private", "testpackage"));
        assertTrue(pojoClassRepository.findByPackageNameAndName("testpackage/subpackage", "PojoClass4")
                .getHasAttributes().stream().anyMatch(attrs -> attrs.getName().equals("attrPojoClass2"))
        );
    }

    @Test
    void addAttributeToNonexistentPojo() {
        assertTrue(pojoElementRepository.findById(12345L).isEmpty());
        try {
            pojoAttributeController.addAttribute(12345L, "PojoClass2", "attrPojoClass2", "private", "testpackage");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    void addIntegerAndLongAttributeToExistingPojo() {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        long id = pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage").getId();
        assertNotNull(pojoAttributeController.addAttribute(id, "Integer", "intAttr", "private", ""));
        assertTrue(pojoClassRepository.findByPackageNameAndName("testpackage/subpackage", "PojoClass4")
                .getHasAttributes().stream().anyMatch(attrs -> attrs.getName().equals("intAttr"))
        );

        assertNotNull(pojoAttributeController.addAttribute(id, "Long", "longAttr", "private", "java/lang"));
        assertTrue(pojoClassRepository.findByPackageNameAndName("testpackage/subpackage", "PojoClass4")
                .getHasAttributes().stream().anyMatch(attrs -> attrs.getName().equals("longAttr"))
        );
    }

    @Test
    void addAttributeWithInvalidName() {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        long id = pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage").getId();
        try {
            pojoAttributeController.addAttribute(id, "Integer", "int", "private", "");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }
    }
}