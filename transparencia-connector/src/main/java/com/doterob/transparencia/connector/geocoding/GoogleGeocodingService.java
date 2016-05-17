package com.doterob.transparencia.connector.geocoding;

import com.doterob.transparencia.connector.HttpService;
import com.doterob.transparencia.model.Address;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by dotero on 13/05/2016.
 */
public class GoogleGeocodingService extends HttpService implements AddressService {

    private static final Logger LOG = LogManager.getLogger(GoogleGeocodingService.class);

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

        final Address result = request(GoogleGeocodingRequestBuilder.find(location), new GoogleGeocodingResponseHandler());
        if(result == null){
            LOG.warn("Direccion no encontrada -> " + location);
        }
        return result;
    }
}
