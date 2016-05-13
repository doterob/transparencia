package com.doterob.transparencia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dotero on 07/05/2016.
 */
public class Contract {

    @JsonProperty("codigo")
    private final String id;
    @JsonProperty("fecha")
    private final Date date;
    @JsonProperty("concepto")
    private final String subject;
    @JsonProperty("cif")
    private final String contractorId;
    @JsonProperty("contratante")
    private final String contractorName;
    @JsonProperty("area")
    private final String organizationArea;
    @JsonProperty("importe")
    private final Float amount;

    public Contract(String id, Date date, String subject, String contractorId, String contractorName, String organizationArea, Float amount) {
        this.id = id;
        this.date = date;
        this.subject = subject;
        this.contractorId = contractorId;
        this.contractorName = contractorName;
        this.organizationArea = organizationArea;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }

    public String getContractorId() {
        return contractorId;
    }

    public String getContractorName() {
        return contractorName;
    }

    public String getOrganizationArea() {
        return organizationArea;
    }

    public Float getAmount() {
        return amount;
    }

    public boolean isValid(){
        return StringUtils.isNotEmpty(id) && date != null
                && StringUtils.isNotEmpty(subject) && StringUtils.isNotEmpty(contractorId)
                && StringUtils.isNotEmpty(contractorName) && StringUtils.isNotEmpty(organizationArea)
                && amount != null;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", subject='" + subject + '\'' +
                ", contractorId='" + contractorId + '\'' +
                ", contractorName='" + contractorName + '\'' +
                ", organizationArea='" + organizationArea + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
