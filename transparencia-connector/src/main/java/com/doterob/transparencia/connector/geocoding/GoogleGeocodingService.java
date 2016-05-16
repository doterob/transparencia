package com.doterob.transparencia.connector.geocoding;

import com.doterob.transparencia.connector.HttpService;
import com.doterob.transparencia.model.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Created by dotero on 13/05/2016.
 */
public class GoogleGeocodingService extends HttpService implements AddressService {

    private static final Logger LOG = LogManager.getLogger(GoogleGeocodingService.class);
    private static final String API_ENDPOINT = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    private static volatile GoogleGeocodingService instance;

    public static GoogleGeocodingService getInstance(){
        if(instance == null){
            synchronized (GoogleGeocodingService.class){
                if (instance == null){
                    instance = new GoogleGeocodingService();
                }
            }
        }
        return instance;
    }

    private GoogleGeocodingService(){
        super();
    }

    @Override
    public Address getAddress(String location){

        try{

            return client.execute(new HttpGet(API_ENDPOINT + new URLCodec().encode(location)), new GoogleGeocodingResponseHandler());

        } catch (IOException | EncoderException e){
            LOG.error(e);
            System.out.println(e);
        }

        return  null;
    }
}
