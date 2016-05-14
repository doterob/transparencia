package com.doterob.transparencia.model;

import java.awt.*;

/**
 * Created by dotero on 14/05/2016.
 */
public class Company {

    private final String id;
    private final String name;
    private final String location;
    private final Point coordinates;

    public Company(String id, String name, String location, Point coordinates) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.coordinates = coordinates;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;

        Company company = (Company) o;

        if (id != null ? !id.equals(company.id) : company.id != null) return false;
        if (name != null ? !name.equals(company.name) : company.name != null) return false;
        if (location != null ? !location.equals(company.location) : company.location != null) return false;
        return coordinates != null ? coordinates.equals(company.coordinates) : company.coordinates == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }
}
