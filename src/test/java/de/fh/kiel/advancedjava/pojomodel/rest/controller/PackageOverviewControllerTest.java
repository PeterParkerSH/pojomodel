package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiOverviewElement;
import de.fh.kiel.advancedjava.pojomodel.rest.service.ClassHandlingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PackageOverviewControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PackageOverviewControllerTest.class);

    @Autowired
    PackageOverviewController packageOverviewController;

    @Autowired
    TestDataBaseController testDataBaseController;

    @BeforeEach
    void clearNodes(){
        testDataBaseController.buildTestDataBase();
    }

    @Test
    void getOverview() {
        List<ApiOverviewElement> list = packageOverviewController.getOverview("testpackage");
        LOGGER.info(list.toString());
        assertEquals(6, list.size());
        list = packageOverviewController.getOverview("");
        assertEquals(12, list.size());
        list = packageOverviewController.getOverview("testpackage.subpackage");
        assertEquals(1, list.size());

    }
}