package com.doterob.transparencia.connector.geocoding;

import com.doterob.transparencia.model.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by dotero on 15/05/2016.
 */
public class GoogleGeocodingResponseHandler implements ResponseHandler<Address> {

    private static final Logger LOG = LogManager.getLogger(GoogleGeocodingResponseHandler.class);

    private static final String ID_FIELD = "col-md-10.col-sm-9.col-xs-12.mtnone.mb10.sessionnif";
    private static final String NAME_FIELD = "h1.title.title-sm.title-margin.mtnone.roboto.mbnone";
    private static final String ADDRESS_FIELD = "div.col-md-10.col-sm-9.col-xs-12.mb10";

    @Override
    public Address handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        final ObjectMapper mapper = new ObjectMapper();
        final HttpEntity entity = response.getEntity();

        final String json = EntityUtils.toString(entity);
        final GoogleGeocodingResponse obj = mapper.readValue (json, GoogleGeocodingResponse.class);

        if(obj.results != null && obj.results.length > 0) {

            final Address.Builder builder = new Address.Builder();

            builder.setAddress(obj.results[0].formatted_address);
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

            return builder.build();
        }

        //LOG.debug("No se ha obtenido dirección -> " + json);
        return null;
    }
}
