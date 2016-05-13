package com.doterob.transparencia.connector.geocoding;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;

/**
 * Created by dotero on 13/05/2016.
 */
public class GoogleGeocodingResolver implements GeocodingResolver {

    private static final Logger LOG = LogManager.getLogger(GoogleGeocodingResolver.class);
    private static final String API_ENDPOINT = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    @Override
    public Point getPoint(String location){

        Point result = null;

        final CloseableHttpClient client = HttpClients.createDefault();
        final HttpGet request = new HttpGet(API_ENDPOINT + location);

        try{

            final CloseableHttpResponse response = client.execute(request);

            try {

                final HttpEntity entity = response.getEntity();
                EntityUtils.consume(entity);

                final GoogleGeocodingResponse obj = (GoogleGeocodingResponse) new JSONPObject(entity.getContent().toString(), GoogleGeocodingResponse.class).getValue();

                if(obj.results != null && obj.results.length > 0) {
                    result = new Point(Integer.valueOf(obj.results[0].geometry.location.lat), Integer.valueOf(obj.results[0].geometry.location.lng));
                }
            } finally {
                response.close();
                client.close();
            }
        } catch (IOException e){
            LOG.error(e);
        }

        return  result;
    }
}
