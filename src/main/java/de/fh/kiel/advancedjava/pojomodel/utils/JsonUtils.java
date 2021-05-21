package de.fh.kiel.advancedjava.pojomodel.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fh.kiel.advancedjava.pojomodel.rest.FileUploadController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Converts an object to a json sting
     * @param obj object to convert
     * @return json string
     */
    public static String objectToJsonString(Object obj){
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error converting object to json", e);
        }
        return "";
    }

    /**
     * converts a json string to an Object
     * @param json json string to convert
     * @param classType T.class
     * @param <T> Object type
     * @return deserialized object instance
     */
    public static <T> T jsonStringToObject(String json, Class<T> classType){
        ObjectMapper om = new ObjectMapper();
        try {
            return om.readValue(json, classType);
        } catch (IOException e) {
            LOGGER.error("Error converting json to object", e);
            return null;
        }
    }
}
