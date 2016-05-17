package com.doterob.transparencia.connector.company;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dotero on 15/05/2016.
 */
public class AxesorRequestBuilder {

    private static final Logger LOG = LogManager.getLogger(AxesorRequestBuilder.class);
    private static final String API_ENDPOINT = "https://www.axesor.es/buscar/empresas?q=";

    private static String url(String code){
        try {
            return API_ENDPOINT + new URLCodec().encode(code);
        } catch (EncoderException e){
            LOG.error("Error codificando " + code, e);
        }
        return API_ENDPOINT + code;
    }

    public static HttpGet find(String code){
        return new HttpGet(url(code));
    }

    public static Map<String, HttpGet> findAll(List<String> codes){

        final Map<String, HttpGet> result = new HashMap<>();
        for (String code : codes){
            result.put(code, find(code));
        }
        return result;
    }
}
