package com.doterob.transparencia.model;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dotero on 07/05/2016.
 */
public class Contract {

    private final String id;
    private final String date;
    private final String subject;
    private final String contractorId;
    private final String contractorName;
    private final String organizationArea;
    private final String amount;

    public Contract(String id, String date, String subject, String contractorId, String contractorName, String organizationArea, String amount) {
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

    public String getDate() {
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

    public String getAmount() {
        return amount;
    }

    public boolean isValid(){
        return StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(date)
                && StringUtils.isNotEmpty(subject) && StringUtils.isNotEmpty(contractorId)
                && StringUtils.isNotEmpty(contractorName) && StringUtils.isNotEmpty(organizationArea)
                && StringUtils.isNotEmpty(amount);
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
