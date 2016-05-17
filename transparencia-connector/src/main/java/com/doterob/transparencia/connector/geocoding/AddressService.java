package com.doterob.transparencia.connector.geocoding;

import com.doterob.transparencia.model.Address;

/**
 * Created by dotero on 13/05/2016.
 */
public interface AddressService {
    Address getAddress(String location);
}
