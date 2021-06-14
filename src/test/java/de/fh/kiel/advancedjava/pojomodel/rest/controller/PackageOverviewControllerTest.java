package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiPackageOverviewElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        List<ApiPackageOverviewElement> list = packageOverviewController.packageOverview("testpackage");
        LOGGER.info(list.toString());
        assertEquals(6, list.size());
        list = packageOverviewController.packageOverview("");
        assertEquals(12, list.size());
        list = packageOverviewController.packageOverview("testpackage.subpackage");
        assertEquals(1, list.size());
        list = packageOverviewController.packageOverview("java/util");
        assertEquals(1, list.size());
    }
}