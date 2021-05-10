package de.fh.kiel.advancedjava.pojomodel.repository;

import de.fh.kiel.advancedjava.pojomodel.model.PojoClass;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PojoClassRepositoryTest {

    @Autowired
    PojoClassRepository pojoClassRepository;

    @Test
    void getPojoClassByNameAndPackageName() {
        pojoClassRepository.deleteAllByName("TestClass123");
        PojoClass pojoClass = pojoClassRepository.getPojoClassByNameAndPackageName("TestClass123", "de/fhkiel/pojo");
        assertNull(pojoClass);
        pojoClass = PojoClass.builder().name("TestClass123").packageName("de/fhkiel/pojo").build();
        pojoClassRepository.save(pojoClass);
        pojoClass = pojoClassRepository.getPojoClassByNameAndPackageName("TestClass123", "de/fhkiel/pojo");
        assertNotNull(pojoClass);
        assertEquals(pojoClass.getName(), "TestClass123");
        pojoClassRepository.deleteAllByName("TestClass123");
        pojoClass = pojoClassRepository.getPojoClassByNameAndPackageName("TestClass123", "de/fhkiel/pojo");
        assertNull(pojoClass);
    }


}