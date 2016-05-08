package com.doterob.transparencia.model;

/**
 * Created by dotero on 08/05/2016.
 */
public class Organization  {

    private final String name;
    private final OrganizationType type;

    public Organization(String name, OrganizationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public OrganizationType getType() {
        return type;
    }
}
