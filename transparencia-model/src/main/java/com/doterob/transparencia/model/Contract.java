package com.doterob.transparencia.model;

import java.util.Date;

/**
 * Created by dotero on 16/05/2016.
 */
public class Contract {

    private final String id;
    private final Date date;
    private final String subject;
    private final Float amount;
    private final String url;
    private final String category;
    private final Integer lot;

    public Contract(String id, Date date, String subject, Float amount, String url, String category, Integer lot) {
        this.id = id;
        this.date = date;
        this.subject = subject;
        this.amount = amount;
        this.url = url;
        this.category = category;
        this.lot = lot;
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

    public Float getAmount() {
        return amount;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public Integer getLot() {
        return lot;
    }


}
