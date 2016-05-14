package com.doterob.transparencia.model;

import java.awt.geom.Point2D;

/**
 * Created by dotero on 14/05/2016.
 */
public class Address {

    private final String address;
    private final String locality;
    private final String province;
    private final String state;
    private final Point2D coordinates;

    public Address(String address, String locality, String province, String state, Point2D coordinates) {
        this.address = address;
        this.locality = locality;
        this.province = province;
        this.state = state;
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public String getLocality() {
        return locality;
    }

    public String getProvince() {
        return province;
    }

    public String getState() {
        return state;
    }

    public Point2D getCoordinates() {
        return coordinates;
    }
}
