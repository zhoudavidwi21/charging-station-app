package at.technikum.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// source: https://stackoverflow.com/questions/60871973/serializing-and-deserializing-json-targeting-java-classes-with-jackson
public class JsonHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(String json, Class<T> valueType) throws IOException {
        return objectMapper.readValue(json, valueType);
    }

    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) throws IOException {
        return objectMapper.readValue(json, valueTypeRef);
    }

    public static <T> List<T> fromJsonToList(String json, Class<T> elementType) throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
        return objectMapper.readValue(json, type);
    }

    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> keyType, Class<V> valueType) throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
        return objectMapper.readValue(json, type);
    }

    public static String toJson(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

}
