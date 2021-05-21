package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.pojomodel.PojoElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PojoElementRepositoryTest {

    @Autowired
    PojoElementRepository pojoElementRepository;

    @Autowired
    TestDataBaseController testDataBaseController;

    @BeforeEach
    void clearNodes(){
        pojoElementRepository.deleteAll();
        testDataBaseController.buildTestDataBase();
    }

    @Test
    void getPojoElementsByAttributeRS() {
        PojoElement pojoElement = pojoElementRepository.findByPackageNameAndName("", "PojoClass1");
        assertNotNull(pojoElement);
        List<PojoElement> attributeRS = pojoElementRepository.getPojoElementsByAttributeRS(pojoElement.getId());
        assertEquals(6, attributeRS.size());
    }

    @Test
    void getElementCountByName() {
        assertEquals(1, pojoElementRepository.countPojoElementsWithClassName("PojoClass1"));
    }

    @Test
    void getPojoElementByPackageName() {
        assertEquals(5, pojoElementRepository.countPojoElementsWithPackageName("testpackage"));
        assertEquals(1, pojoElementRepository.countPojoElementsWithPackageName("testpackage/subpackage"));

    }
}