package de.fh.kiel.advancedjava.pojoapplication.repository;

import de.fh.kiel.advancedjava.pojoapplication.TestDataBaseController;
import de.fh.kiel.advancedjava.pojoapplication.pojomodel.PojoElement;
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
        assertEquals(8, attributeRS.size());
    }

    @Test
    void getElementCountByName() {
        assertEquals(1, pojoElementRepository.countPojoElementsWithClassName("PojoClass1"));
    }

    @Test
    void getElementCountByNameWildcard() {
        assertEquals(6, pojoElementRepository.getPojoElementStartsWithByPackageName("test").size());
    }

    @Test
    void getPojoElementByPackageName() {
        assertEquals(5, pojoElementRepository.countPojoElementsWithPackageName("testpackage"));
        assertEquals(1, pojoElementRepository.countPojoElementsWithPackageName("testpackage/subpackage"));
    }
}