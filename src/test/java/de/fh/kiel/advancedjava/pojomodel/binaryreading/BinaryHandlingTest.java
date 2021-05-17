package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.export.ExportFormat;
import de.fh.kiel.advancedjava.pojomodel.model.*;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.FileUploadController;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BinaryHandlingTest {

    @Autowired
    TestDataBaseController testDataBaseController;

    @Autowired
    PojoElementRepository pojoElementRepository;

    @Autowired
    PojoClassRepository pojoClassRepository;

    @Autowired
    PojoInterfaceRepository pojoInterfaceRepository;

    @Autowired
    PojoReferenceRepository pojoReferenceRepository;


    @BeforeEach
    void initDataBase(){
        testDataBaseController.buildTestDataBase();
    };

    @Test
    void readFile() {

    }

    @Test
    void exportFile(){

    }
}