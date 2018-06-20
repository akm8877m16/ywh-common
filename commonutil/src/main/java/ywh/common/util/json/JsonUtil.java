package ywh.common.util.json;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ywh.common.util.exception.DescribeException;
import ywh.common.util.exception.ExceptionEnum;

import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String getJsonFromObject(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            throw new DescribeException("get json error "+e, ExceptionEnum.JSON_ERROR.getCode());
        } catch (JsonMappingException e) {
            throw new DescribeException("get json error "+e,ExceptionEnum.JSON_ERROR.getCode());
        } catch (IOException e) {
            throw new DescribeException("get json error "+e,ExceptionEnum.JSON_ERROR.getCode());
        }
    }

}
