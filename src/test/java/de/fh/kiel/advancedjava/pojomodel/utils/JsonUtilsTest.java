package de.fh.kiel.advancedjava.pojomodel.utils;

import de.fh.kiel.advancedjava.pojomodel.rest.restmodel.PojoStatistic;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JsonUtilsTest {

    @Test
    void objectToJsonString() {
        PojoStatistic pojoStatistic = PojoStatistic.builder().className("Classname").packageName("Packagename").build();
        PojoStatistic pojoStatistic2 = JsonUtils.jsonStringToObject(JsonUtils.objectToJsonString(pojoStatistic), PojoStatistic.class);
        assertEquals(pojoStatistic, pojoStatistic2);

    }
}