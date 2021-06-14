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
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass2", "testpackage"));
        assertNotNull(pojoAttributeController.addAttribute("testpackage.subpackage", "PojoClass4","PojoClass2", "attrPojoClass2", "private", "testpackage"));
        assertTrue(pojoClassRepository.findByPackageNameAndName("testpackage/subpackage", "PojoClass4")
                .getHasAttributes().stream().anyMatch(attrs -> attrs.getName().equals("attrPojoClass2"))
        );
    }

    @Test
    void addReferenceAttributeWithEmptyPackageToExistingPojo() {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass1", ""));
        assertNotNull(pojoAttributeController.addAttribute("testpackage.subpackage", "PojoClass4","PojoClass1", "attrPojoClass1", "private", ""));
        assertTrue(pojoClassRepository.findByPackageNameAndName("testpackage/subpackage", "PojoClass4")
                .getHasAttributes().stream().anyMatch(attrs -> attrs.getName().equals("attrPojoClass1"))
        );
    }

    @Test
    void addAttributeToNonexistentPojo() {
        assertNull(pojoElementRepository.findByPackageNameAndName("hello/world", "HelloClass"));
        try {
            pojoAttributeController.addAttribute("hello.world", "HelloClass", "PojoClass2", "attrPojoClass2", "private", "testpackage");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }
    }

    @Test
    void addIntegerAndLongAttributeToExistingPojo() {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        assertNotNull(pojoAttributeController.addAttribute("testpackage.subpackage", "PojoClass4", "Integer", "intAttr", "private", ""));
        assertTrue(pojoClassRepository.findByPackageNameAndName("testpackage/subpackage", "PojoClass4")
                .getHasAttributes().stream().anyMatch(attrs -> attrs.getName().equals("intAttr"))
        );

        assertNotNull(pojoAttributeController.addAttribute("testpackage.subpackage", "PojoClass4", "Long", "longAttr", "private", "java/lang"));
        assertTrue(pojoClassRepository.findByPackageNameAndName("testpackage/subpackage", "PojoClass4")
                .getHasAttributes().stream().anyMatch(attrs -> attrs.getName().equals("longAttr"))
        );
    }

    @Test
    void addAttributeWithInvalidName() {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoClass4", "testpackage/subpackage"));
        try {
            pojoAttributeController.addAttribute("testpackage.subpackage", "PojoClass4", "Integer", "int", "private", "");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }
    }

    @Test
    void addAttributeToPojoReference() {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("String", "java/lang"));
        assertNotNull(pojoAttributeController.addAttribute("java.lang", "String", "Integer", "intAttr", "private", "java.lang"));
        assertTrue(pojoClassRepository.findByPackageNameAndName("java/lang", "String")
                .getHasAttributes().stream().anyMatch(attrs -> attrs.getName().equals("intAttr"))
        );
    }

    @Test
    void addAttributeToInterface() {
        assertNotNull(pojoElementRepository.getPojoElementByNameAndPackageName("PojoInterface2", "testpackage"));
        try {
            pojoAttributeController.addAttribute("testpackage", "PojoInterface2", "Integer", "int", "private", "java.lang");
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }
    }
}