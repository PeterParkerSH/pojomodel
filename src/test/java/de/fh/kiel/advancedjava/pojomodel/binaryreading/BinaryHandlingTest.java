package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BinaryHandlingTest {

    @Autowired
    TestDataBaseController testDataBaseController;

    @BeforeEach
    void initDataBase(){
        testDataBaseController.buildTestDataBase();
    };

    @Test
    void readFile() {

    }






}