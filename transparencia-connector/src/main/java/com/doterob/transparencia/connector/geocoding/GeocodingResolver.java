package com.doterob.transparencia.connector.geocoding;

import java.awt.*;

/**
 * Created by dotero on 13/05/2016.
 */
public interface GeocodingResolver {
    Point getPoint(String location);
}
