package sdk.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class jsonUtils<T> {

    public static boolean isJsonMime(String mime) {
        return mime != null && mime.matches("(?i)application\\/json(;.*)?");
    }

    public static HashMap<String, Object> toMap(String json) throws JsonProcessingException {
        try {
            return new ObjectMapper().readValue(json, HashMap.class);
        } catch (IOException e) {
            throw e;
        }
    }


    public static <T> String objectToJson(T item) throws JsonProcessingException {
        try {
            ObjectMapper mapper= getObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return  mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    public static <T> T jsonToObject(String json, Class clazz) throws JsonProcessingException {
        try {
            return (T) new ObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }
    public static <T> T jsonToObjectWithDateFormatter(String json, Class clazz) throws JsonProcessingException {
        try {
            return (T) getObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }
    public static <T> T jsonToObjectWithDateFormatter(String json, TypeReference type) throws JsonProcessingException {
        try {
            return (T) getObjectMapper().readValue(json, type);
        } catch (JsonProcessingException e) {
            throw e;
        }
    }

    public static ObjectMapper getObjectMapper(){
        final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(df);
        return  objectMapper;
    }
    public static <T> Map objectToMap(T item)  {
        ObjectMapper mapper= getObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return  mapper.convertValue(item, Map.class);
    }
    public static <T> HashMap objectToHashMap(T item)  {
        ObjectMapper mapper= getObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return  mapper.convertValue(item, HashMap.class);
    }
    public static <T> List<HashMap> objectToListOfHashMap(T item)  {
        ObjectMapper mapper= getObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        TypeReference<List<HashMap>> typeReference = new TypeReference<List<HashMap>>() {
        };

        return  mapper.convertValue(item, typeReference);
    }


}
