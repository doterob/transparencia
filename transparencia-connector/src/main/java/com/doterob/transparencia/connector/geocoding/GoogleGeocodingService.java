package com.doterob.transparencia.connector.geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Created by dotero on 13/05/2016.
 */
public class GoogleGeocodingService implements GeocodingService {

    private static final Logger LOG = LogManager.getLogger(GoogleGeocodingService.class);
    private static final String API_ENDPOINT = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    @Override
    public Point2D getPoint(String location){

        Point2D result = null;

        try{

            final HttpClient client = HttpClients.createDefault();
            final HttpGet request = new HttpGet(API_ENDPOINT + new URLCodec().encode(location));
            final HttpResponse response = client.execute(request);

            try {

                final ObjectMapper mapper = new ObjectMapper();
                final HttpEntity entity = response.getEntity();

                final GoogleGeocodingResponse obj = mapper.readValue (EntityUtils.toString(entity), GoogleGeocodingResponse.class);

                if(obj.results != null && obj.results.length > 0) {
                    result = new Point2D.Double(Double.valueOf(obj.results[0].geometry.location.lat), Double.valueOf(obj.results[0].geometry.location.lng));
                }
            } finally {
                //response.close();
                //client.close();
            }
        } catch (IOException | EncoderException e){
            LOG.error(e);
            System.out.println(e);
        }

        return  result;
    }
}
