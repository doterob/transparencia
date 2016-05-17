package com.doterob.transparencia.connector.geocoding;

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
public class GoogleGeocodingRequestBuilder {

    private static final Logger LOG = LogManager.getLogger(GoogleGeocodingRequestBuilder.class);
    private static final String API_ENDPOINT = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    private static String url(String location){
        try {
            return API_ENDPOINT + new URLCodec().encode(location);
        } catch (EncoderException e){
            LOG.error("Error codificando " + location, e);
        }
        return API_ENDPOINT + location;
    }

    public static HttpGet find(String location){
        return new HttpGet(url(location));
    }

    public static Map<String, HttpGet> findAll(List<String> locations){

        final Map<String, HttpGet> result = new HashMap<>();
        for (String location : locations){
            result.put(location, find(location));
        }
        return result;
    }
}
