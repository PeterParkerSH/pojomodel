package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class IndexControllerTest {
    @Autowired
    IndexController indexController;

    @Test
    void listUploadedFiles() {
        String res = indexController.listUploadedFiles().getBody();
        assertTrue(res.trim().startsWith("<!DOCTYPE html>"));
        assertFalse(res.contains("[POJOTABLE]"));
    }
}