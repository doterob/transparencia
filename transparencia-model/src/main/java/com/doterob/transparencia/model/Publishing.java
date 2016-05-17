package com.doterob.transparencia.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by dotero on 08/05/2016.
 */
@JsonAutoDetect
public class Publishing {

    @JsonProperty("codigo")
    private final String code;
    @JsonProperty("fecha")
    private final Date date;
    @JsonProperty("concepto")
    private final String subject;
    @JsonProperty("categoria")
    private final String category;
    @JsonProperty("importe")
    private final Float amount;
    @JsonProperty("lote")
    private final Integer lot;
    @JsonProperty("url")
    private final String url;

    @JsonProperty("entidad")
    private final String organizationName;
    @JsonProperty("organo")
    private final String organizationArea;
    @JsonProperty("ambito")
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

    public Publishing(String code, Date date, String subject, String category, String contractorId, String contractorName, String organizationArea,
                      Float amount, String organizationName, String url, Integer lot,  OrganizationType type,
                      String address, String locality, String province, String state, Location coordinates) {
        this.code = code;
        this.date = date;
        this.subject = subject;
        this.category = category;
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

    public String getCategory(){ return category;}

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

        private  String code;
        private  Date date;
        private  String subject;
        private String category;
        private  Float amount;
        private  Integer lot;
        private  String url;

        private  String organizationName;
        private  String organizationArea;
        private  OrganizationType type;

        private  String contractorId;
        private  String contractorName;
        private  String address;
        private  String locality;
        private  String province;
        private  String state;
        private  Location coordinates;

        public Builder setContract(ContractComplex contract) {
            setContract(contract.getContract());
            setOrganization(contract.getOrganization());
            contractorId = contract.getNif();
            contractorName = contract.getCompanyName();
            return this;
        }

        public Builder setContract(Contract contract) {
            code  = contract.getId();
            date = contract.getDate();
            subject = contract.getSubject();
            category = contract.getCategory();
            amount = contract.getAmount();
            lot = contract.getLot();
            url = contract.getUrl();
            return this;
        }

        public Builder setOrganization(Organization organization) {
            organizationName = organization.getName();
            organizationArea = organization.getArea();
            type = organization.getType();
            return this;
        }

        public Builder setCompany(Company company) {
            if(company != null) {
                contractorId = company.getId();
                contractorName = company.getName();
                if(company.getAddress() != null) {
                    address = company.getAddress().getAddress();
                    locality = company.getAddress().getLocality();
                    province = company.getAddress().getProvince();
                    state = company.getAddress().getState();
                    coordinates = new Location(company.getAddress().getCoordinates().getX(), company.getAddress().getCoordinates().getY());
                }
            }
            return this;
        }

        public Publishing build(){

            return new Publishing(code, date, subject, category, contractorId, contractorName, organizationArea, amount, organizationName, url, lot, type, address, locality, province, state, coordinates);
        }
    }
}
