package com.doterob.transparencia.model;

/**
 * Created by dotero on 08/05/2016.
 */
public class Organization  {

    private final String name;
    private final OrganizationType type;
    private final String area;

    public Organization(String name, OrganizationType type, String area) {
        this.name = name;
        this.type = type;
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public OrganizationType getType() {
        return type;
    }

    public String getArea() {
        return area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Organization)) return false;

        Organization that = (Organization) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != that.type) return false;
        return area != null ? area.equals(that.area) : that.area == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (area != null ? area.hashCode() : 0);
        return result;
    }
}
