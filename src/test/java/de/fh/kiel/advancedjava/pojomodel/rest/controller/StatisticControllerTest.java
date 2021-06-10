package de.fh.kiel.advancedjava.pojomodel.rest.controller;

import de.fh.kiel.advancedjava.pojomodel.TestDataBaseController;
import de.fh.kiel.advancedjava.pojomodel.repository.PojoElementRepository;
import de.fh.kiel.advancedjava.pojomodel.rest.controller.StatisticController;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.ApiPojoElement;
import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.PojoStatistic;
import de.fh.kiel.advancedjava.pojomodel.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatisticControllerTest {
    @Autowired
    PojoElementRepository pojoElementRepository;

    @Autowired
    StatisticController statisticController;

    @Autowired
    TestDataBaseController testDataBaseController;
    @Autowired
    private MockMvc mockMvc;

    String testRes = """
            {
              "className" : "PojoClass3",
              "packageName" : "testpackage",
              "attributeCount" : 0,
              "extendsClass" : {
                "className" : "Object",
                "packageName" : "java.lang"
              },
              "implementsList" : [ {
                "className" : "PojoInterface4",
                "packageName" : "testpackage"
              }, {
                "className" : "PojoInterface2",
                "packageName" : "testpackage"
              } ],
              "subClassCount" : 1,
              "usedAsAttributeCount" : 0,
              "numberOfClassesInPackage" : 5,
              "numberOfClassesWithName" : 1
            }""";

    @BeforeEach
    void clearNodes(){
        pojoElementRepository.deleteAll();
        testDataBaseController.buildTestDataBase();
    }

    private String buildStatisticRequest(String className, String packageName){
        packageName = packageName.replace("/", ".");
        return "/pojoStatistic?package="+packageName+"&name="+className;
    }

    @Test
    void pojoStatistic() throws Exception {

        AtomicReference<String> statisticJsonAtomic = new AtomicReference<>("");
        try {
            this.mockMvc.perform(get(buildStatisticRequest("PojoClass3", "testpackage"))).andDo(result -> {
                statisticJsonAtomic.set(result.getResponse().getContentAsString());
            }).andExpect(status().isOk());
        }catch (Exception e){
            fail(e.getMessage());
        }
        String res = statisticJsonAtomic.get();

        PojoStatistic pojoStatistic = JsonUtils.jsonStringToObject(res, PojoStatistic.class);
        PojoStatistic original = JsonUtils.jsonStringToObject(testRes, PojoStatistic.class);
        original.getImplementsList().sort(Comparator.comparing(ApiPojoElement::getClassName).thenComparing(ApiPojoElement::getPackageName));
        pojoStatistic.getImplementsList().sort(Comparator.comparing(ApiPojoElement::getClassName).thenComparing(ApiPojoElement::getPackageName));

        assertNotNull(original);
        assertEquals(original, pojoStatistic);
    }

    @Test
    void pojoStatisticDirect() throws Exception {

        AtomicReference<String> statisticJsonAtomic = new AtomicReference<>("");
        try {
            statisticJsonAtomic.set(statisticController.pojoStatistic("testpackage", "PojoClass3"));
        }catch (Exception e){
            fail(e.getMessage());
        }
        String res = statisticJsonAtomic.get();

        PojoStatistic pojoStatistic = JsonUtils.jsonStringToObject(res, PojoStatistic.class);
        PojoStatistic original = JsonUtils.jsonStringToObject(testRes, PojoStatistic.class);
        original.getImplementsList().sort(Comparator.comparing(ApiPojoElement::getClassName).thenComparing(ApiPojoElement::getPackageName));
        pojoStatistic.getImplementsList().sort(Comparator.comparing(ApiPojoElement::getClassName).thenComparing(ApiPojoElement::getPackageName));

        assertNotNull(original);
        assertEquals(original, pojoStatistic);
    }
}