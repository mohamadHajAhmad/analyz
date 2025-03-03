package sdk.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import sdk.model.AbstractModel;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONHelper {


    /**
     * To json from object.
     *
     * @param object the object
     * @return the string
     */
    public static String toJsonFromObject(Object object) {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * To map.
     *
     * @param jsonStr the json str
     * @return the map
     */
    public static Map<String, Object> toMap(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();

        try {
           map= new ObjectMapper().readValue(jsonStr, HashMap.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

    public static <T> List<T> toListGeneric(String jsonStr) {
        List<T> list = new ArrayList<T>();

        try {
            list = new ObjectMapper().readValue(jsonStr, List.class);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * converts the given model to JSON string encoding all the inner values as Object types.
     *
     * @param model
     *            the model
     * @return the string
     */
    public static String toJsonFromModel(AbstractModel model) {
        return toJsonFromMap(model.getProperties());
    }

    public static String toJsonFromMap(Map<String,Object> map) {
        String json = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(map);
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json.toString();
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

    public static ObjectMapper getObjectMapper(){
        final DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(df);
        return  objectMapper;
    }

}
