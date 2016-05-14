package com.doterob.transparencia.connector.geocoding;

import com.doterob.transparencia.model.Address;
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
import java.util.Arrays;

/**
 * Created by dotero on 13/05/2016.
 */
public class GoogleGeocodingService implements AddressService {

    private static final Logger LOG = LogManager.getLogger(GoogleGeocodingService.class);
    private static final String API_ENDPOINT = "https://maps.googleapis.com/maps/api/geocode/json?address=";

    @Override
    public Address getAddress(String location){

        Address result = null;

        try{

            final HttpClient client = HttpClients.createDefault();
            final HttpGet request = new HttpGet(API_ENDPOINT + new URLCodec().encode(location));
            final HttpResponse response = client.execute(request);

            try {

                final ObjectMapper mapper = new ObjectMapper();
                final HttpEntity entity = response.getEntity();

                final GoogleGeocodingResponse obj = mapper.readValue (EntityUtils.toString(entity), GoogleGeocodingResponse.class);

                if(obj.status == "OK" && obj.results != null && obj.results.length > 0) {

                    final Address.Builder builder = new Address.Builder();

                    builder.setAddress(location);
                    builder.setCoordinates(new Point2D.Double(Double.valueOf(obj.results[0].geometry.location.lat), Double.valueOf(obj.results[0].geometry.location.lng)));

                    for (GoogleGeocodingResponse.address_component a : obj.results[0].address_components) {
                        if(Arrays.asList(a.types).contains("locality")){
                            builder.setLocality(a.long_name);
                        }
                        if(Arrays.asList(a.types).contains("administrative_area_level_2")){
                            builder.setProvince(a.long_name);
                        }
                        if(Arrays.asList(a.types).contains("administrative_area_level_1")){
                            builder.setState(a.long_name);
                        }
                    }

                    result = builder.build();
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
