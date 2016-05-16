package com.doterob.transparencia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.org.apache.bcel.internal.generic.FLOAD;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dotero on 08/05/2016.
 */
public class Publishing {

    @JsonProperty("codigo")
    private final String code;
    @JsonProperty("fecha")
    private final Date date;
    @JsonProperty("concepto")
    private final String subject;
    @JsonProperty("importe")
    private final Float amount;
    @JsonProperty("lote")
    private final Integer lot;
    @JsonProperty("url")
    private final String url;

    @JsonProperty("entidad")
    private final String organizationName;
    @JsonProperty("area")
    private final String organizationArea;
    @JsonProperty("tipo")
    private final OrganizationType type;

    @JsonProperty("cif")
    private final String contractorId;
    @JsonProperty("contratante")
    private final String contractorName;
    @JsonIgnore
    private final String address;
    @JsonProperty("ayuntamiento")
    private final String locality;
    @JsonProperty("provincia")
    private final String province;
    @JsonProperty("autonomia")
    private final String state;
    @JsonProperty("coordenadas")
    private final Location coordinates;

    public Publishing(String code, Date date, String subject, String contractorId, String contractorName, String organizationArea,
                      Float amount, String organizationName, String url, Integer lot,  OrganizationType type,
                      String address, String locality, String province, String state, Location coordinates) {
        this.code = code;
        this.date = date;
        this.subject = subject;
        this.contractorId = contractorId;
        this.contractorName = contractorName;
        this.organizationArea = organizationArea;
        this.amount = amount;
        this.organizationName = organizationName;
        this.url = url;
        this.lot = lot;
        this.type = type;
        this.address = address;
        this.locality = locality;
        this.province = province;
        this.state = state;
        this.coordinates = coordinates;
    }

    public String getCode() {
        return code;
    }

    public Date getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }

    public Float getAmount() {
        return amount;
    }

    public Integer getLot() {
        return lot;
    }

    public String getUrl() {
        return url;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getOrganizationArea() {
        return organizationArea;
    }

    public OrganizationType getType() {
        return type;
    }

    public String getContractorId() {
        return contractorId;
    }

    public String getContractorName() {
        return contractorName;
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

    public Location getCoordinates() {
        return coordinates;
    }

    public static class Location{
        private final Double lat;
        private final Double lon;

        public Location(Double lat, Double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public Double getLat() {
            return lat;
        }

        public Double getLon() {
            return lon;
        }
    }

    public static class Builder {

        private Contract contract;
        private Organization organization;
        private Company company;

        public Builder setContract(Contract contract) {
            this.contract = contract;
            return this;
        }

        public Builder setOrganization(Organization organization) {
            this.organization = organization;
            return this;
        }

        public Builder setCompany(Company company) {
            this.company = company;
            return this;
        }

        public Publishing build(){

            return new Publishing(contract.getId(), contract.getDate(),contract.getSubject(), company.getId(), company.getName(), organization.getArea(),
                    contract.getAmount(), organization.getName(), contract.getUrl(), contract.getLot(), organization.getType(),
                    company.getAddress().getAddress(), company.getAddress().getLocality(), company.getAddress().getProvince(), company.getAddress().getState(),
                    new Location(company.getAddress().getCoordinates().getX(), company.getAddress().getCoordinates().getY()));
        }
    }
}
