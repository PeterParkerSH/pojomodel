package de.fh.kiel.advancedjava.pojomodel.binaryreading;

import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
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
class JarHandlingTest {

    @Autowired
    JarHandling jarHandling;
    @Autowired
    ClassHandling classHandling;

    @Autowired
    PojoClassRepository pojoClassRepository;

    @Autowired
    PojoInterfaceRepository interfaceRepository;

    @BeforeEach
    void clearNodes(){
        pojoClassRepository.deleteAll();
        interfaceRepository.deleteAll();
    };

    @Test
    void readFile() {
        URL url = this.getClass().getClassLoader().getResource("ExampleJar-1.0-SNAPSHOT.jar");
            File file = null;
            try {
                file = new File(url.toURI());
                FileInputStream input = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile("ExampleJar-1.0-SNAPSHOT.jar", "ExampleJar-1.0-SNAPSHOT.jar", "application/octet-stream", IOUtils.toByteArray(input));

                try {
                    for (ClassNode classNode: jarHandling.readFile(multipartFile)) {
                        classHandling.handleClassNode(classNode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    fail(e.getMessage());
                }

            } catch (Exception e) {
                fail(e.toString());
            }


    }
}