package de.fh.kiel.advancedjava.pojomodel;

import de.fh.kiel.advancedjava.pojomodel.binaryreading.ClassHandling;
import de.fh.kiel.advancedjava.pojomodel.binaryreading.BinaryHandling;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoClassRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoInterfaceRepository;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoReferenceRepository;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.tree.ClassNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

@Controller
public class TestDataBaseController {

    @Autowired
    BinaryHandling binaryHandling;
    @Autowired
    ClassHandling classHandling;

    @Autowired
    PojoClassRepository pojoClassRepository;

    @Autowired
    PojoInterfaceRepository interfaceRepository;

    @Autowired
    PojoReferenceRepository referenceRepository;

    @Autowired
    PojoElementRepository pojoElementRepository;

    public void buildTestDataBase(){
        pojoElementRepository.deleteAll();


        URL url = this.getClass().getClassLoader().getResource("ExampleJar-1.0-SNAPSHOT.jar");
        File file = null;
        try {
            file = new File(url.toURI());
            FileInputStream input = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile("ExampleJar-1.0-SNAPSHOT.jar", "ExampleJar-1.0-SNAPSHOT.jar", "application/octet-stream", IOUtils.toByteArray(input));

            try {
                classHandling.handleClassNodes(binaryHandling.readFile(multipartFile));
            } catch (Exception e) {
                fail(e.getMessage());
            }

        } catch (Exception e) {
            fail(e.toString());
        }
    }
}
