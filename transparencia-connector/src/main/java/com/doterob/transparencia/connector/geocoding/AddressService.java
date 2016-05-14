package com.doterob.transparencia.connector.geocoding;

import com.doterob.transparencia.model.Address;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by dotero on 13/05/2016.
 */
public interface AddressService {
    Address getAddress(String location);
}
