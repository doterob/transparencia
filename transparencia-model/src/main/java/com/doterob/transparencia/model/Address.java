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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address1 = (Address) o;

        if (address != null ? !address.equals(address1.address) : address1.address != null) return false;
        if (locality != null ? !locality.equals(address1.locality) : address1.locality != null) return false;
        if (province != null ? !province.equals(address1.province) : address1.province != null) return false;
        if (state != null ? !state.equals(address1.state) : address1.state != null) return false;
        return coordinates != null ? coordinates.equals(address1.coordinates) : address1.coordinates == null;

    }

    @Override
    public int hashCode() {
        int result = address != null ? address.hashCode() : 0;
        result = 31 * result + (locality != null ? locality.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
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
