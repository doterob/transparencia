package com.doterob.transparencia.connector.geocoding;

import com.doterob.transparencia.model.Address;
import com.doterob.transparencia.model.Company;
import com.doterob.transparencia.model.ContractComplex;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dotero on 15/05/2016.
 */
public class GoogleGeocodingResponseHandler implements ResponseHandler<Address> {

    private static final String ID_FIELD = "col-md-10.col-sm-9.col-xs-12.mtnone.mb10.sessionnif";
    private static final String NAME_FIELD = "h1.title.title-sm.title-margin.mtnone.roboto.mbnone";
    private static final String ADDRESS_FIELD = "div.col-md-10.col-sm-9.col-xs-12.mb10";

    @Override
    public Address handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        final ObjectMapper mapper = new ObjectMapper();
        final HttpEntity entity = response.getEntity();

        final GoogleGeocodingResponse obj = mapper.readValue (EntityUtils.toString(entity), GoogleGeocodingResponse.class);

        if(obj.status == "OK" && obj.results != null && obj.results.length > 0) {

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

        return null;
    }
}
