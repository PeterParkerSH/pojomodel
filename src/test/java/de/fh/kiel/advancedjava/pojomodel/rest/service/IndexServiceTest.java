package de.fh.kiel.advancedjava.pojomodel.rest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class IndexServiceTest {

    @Autowired
    IndexService indexService;
    @Test
    void listUploadedFiles() {
        String res = indexService.listUploadedFiles();
        assertTrue(res.trim().startsWith("<div>"));
        assertFalse(res.contains("[POJOTABLE]"));

    }
}