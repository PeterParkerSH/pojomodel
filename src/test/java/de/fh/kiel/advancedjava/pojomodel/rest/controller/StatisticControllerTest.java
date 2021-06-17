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
import org.springframework.http.ResponseEntity;
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

        ResponseEntity<PojoStatistic> responseEntity = null;
        responseEntity = statisticController.pojoStatistic("testpackage", "PojoClass3");
        assertEquals(200, responseEntity.getStatusCode().value());

        var resultObject = new Object() {
            String resultStr = "";
        };
        this.mockMvc.perform(get(buildStatisticRequest("PojoClass3", "testpackage"))).andDo(result -> {
            resultObject.resultStr = result.getResponse().getContentAsString();
        }).andExpect(status().isOk());
        String res = resultObject.resultStr;

        PojoStatistic pojoStatistic = JsonUtils.jsonStringToObject(res, PojoStatistic.class);
        PojoStatistic original = JsonUtils.jsonStringToObject(testRes, PojoStatistic.class);
        original.getImplementsList().sort(Comparator.comparing(ApiPojoElement::getClassName).thenComparing(ApiPojoElement::getPackageName));
        pojoStatistic.getImplementsList().sort(Comparator.comparing(ApiPojoElement::getClassName).thenComparing(ApiPojoElement::getPackageName));

        assertNotNull(original);
        assertEquals(original, pojoStatistic);
    }
}