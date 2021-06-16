package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiPackageOverviewElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

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
        ResponseEntity<List<ApiPackageOverviewElement>> responseEntity = packageOverviewController.packageOverview("testpackage");
        assertEquals(200, responseEntity.getStatusCode().value());
        List<ApiPackageOverviewElement> list = responseEntity.getBody();
        LOGGER.info(list.toString());
        assertEquals(6, list.size());
        list = packageOverviewController.packageOverview("").getBody();
        assertEquals(12, list.size());
        list = packageOverviewController.packageOverview("testpackage.subpackage").getBody();
        assertEquals(1, list.size());
        list = packageOverviewController.packageOverview("java/util").getBody();
        assertEquals(1, list.size());
    }
}