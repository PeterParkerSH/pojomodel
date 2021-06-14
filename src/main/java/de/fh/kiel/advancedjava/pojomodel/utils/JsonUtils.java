package de.fh.kiel.advancedjava.pojomodel.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils(){
        throw new IllegalStateException("Utility class");
    }

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

    public static void validateJSON(String json) throws ValidationException{
        try (InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream("pojo-schema.json")) {
            assert inputStream != null;
            JSONObject jsonSchema = new JSONObject(
                    new JSONTokener(inputStream));
            JSONObject jsonObject = new JSONObject(json);
            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonObject);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
