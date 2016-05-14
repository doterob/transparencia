package com.doterob.transparencia.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

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

    public static class Builder {

        private  String address;
        private  String locality;
        private  String province;
        private  String state;
        private  Point2D coordinates;

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setLocality(String locality) {
            this.locality = locality;
            return this;
        }

        public Builder setProvince(String province) {
            this.province = province;
            return this;
        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setCoordinates(Point2D coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Address build(){
            return new Address(address, locality, province, state, coordinates);
        }
    }
}
