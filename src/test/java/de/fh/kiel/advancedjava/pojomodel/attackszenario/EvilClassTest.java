package de.fh.kiel.advancedjava.pojomodel.attackszenario;

import de.fh.kiel.advancedjava.pojomodel.rest.controller.FileUploadController;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EvilClassTest {

    @Autowired
    FileUploadController fileUploadController;



    @Test
    void contextLoads() throws IOException {
        EvilClass evilClass = new EvilClass();
        File file = new File("evilresult.txt");
        assertTrue(file.exists());
        file.delete();

        File file2 = new File("build/classes/java/main/de/fh/kiel/advancedjava/pojomodel/EvilClass.class");
        FileInputStream fis = new FileInputStream(file2);
        MockMultipartFile mpf = new MockMultipartFile("file", FilenameUtils.getName("EvilClass.class"), "application/octet-stream", IOUtils.toByteArray(fis));
        fileUploadController.uploadFile(mpf);
        file = new File("evilresult.txt");
        assertFalse(file.exists());
    }

}