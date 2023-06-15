package at.technikum.util;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;


// source: https://stackoverflow.com/questions/60871973/serializing-and-deserializing-json-targeting-java-classes-with-jackson
public class JsonHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserialize(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String extractField(String json, String fieldName) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode fieldNode = rootNode.get(fieldName);
            return fieldNode.asText();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String extractObject(String json, String fieldName) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode fieldNode = rootNode.get(fieldName);
            return fieldNode.toString();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}