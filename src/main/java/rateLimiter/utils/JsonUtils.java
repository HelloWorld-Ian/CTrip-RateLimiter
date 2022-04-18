package rateLimiter.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.List;

/**
 *
 * 使用新版com.fasterxml.jackson.databind.ObjectMapper，
 * 而不是org.codehaus.jackson.map.ObjectMapper
 *
 * json工具类
 *
 * @author Iancy
 * @date 2022/1/17
 */
public class JsonUtils {

    private static final ObjectMapper mapper=new ObjectMapper();

    public static JsonNode readTree(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (IOException e) {
            throw new IllegalArgumentException("Illegal json style : " + jsonStr);
        }
    }

    public static <T> List<T> readValues(String jsonStr, Class<T> clazz) {
        CollectionType javaType = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        try {
            return mapper.readValue(jsonStr, javaType);
        } catch (IOException e) {
            throw new IllegalArgumentException("Illegal json style : " + jsonStr);
        }
    }

    public static String toJson(Object target){
        try {
            return mapper.writeValueAsString(target);
        } catch (IOException e) {
            throw new IllegalArgumentException("parse to string fail", e);
        }
    }

    public static <T> T readValue(String jsonStr, Class<T>type){
        try {
            return mapper.readValue(jsonStr,type);
        } catch (IOException e) {
            throw new IllegalArgumentException("parse to string fail", e);
        }
    }
}
