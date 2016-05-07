package com.doterob.transparencia.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by dotero on 07/05/2016.
 */
public class Contract {

    private final String id;
    private final String date;
    private final String subject;
    private final String entityId;
    private final String entityName;
    private final String area;
    private final String amount;

    public Contract(String id, String date, String subject, String entityId, String entityName, String area, String amount) {
        this.id = id;
        this.date = date;
        this.subject = subject;
        this.entityId = entityId;
        this.entityName = entityName;
        this.area = area;
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

    public String getEntityId() {
        return entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getArea() {
        return area;
    }

    public String getAmount() {
        return amount;
    }

    public boolean isValid(){
        return StringUtils.isNotEmpty(id) && StringUtils.isNotEmpty(date)
                && StringUtils.isNotEmpty(subject) && StringUtils.isNotEmpty(entityId)
                && StringUtils.isNotEmpty(entityName) && StringUtils.isNotEmpty(area)
                && StringUtils.isNotEmpty(amount);
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", subject='" + subject + '\'' +
                ", entityId='" + entityId + '\'' +
                ", entityName='" + entityName + '\'' +
                ", area='" + area + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
