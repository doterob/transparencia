package com.doterob.transparencia.connector.company;

import com.doterob.transparencia.connector.geocoding.GoogleGeocodingResponse;
import com.doterob.transparencia.model.Company;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

import javax.print.attribute.standard.MediaSize;
import java.awt.*;
import java.io.IOException;

/**
 * Created by dotero on 14/05/2016.
 */
public class CompanyService {

    private static final Logger LOG = LogManager.getLogger(CompanyService.class);

    private static final String API_ENDPOINT = "http://www.infocif.es/general/empresas-informacion-listado-empresas.asp?Buscar=";
    private static final String NAME_FIELD = "h1.title.title-sm.title-margin.mtnone.roboto.mbnone";
    private static final String ADDRESS_FIELD = "div.col-md-10.col-sm-9.col-xs-12.mb10";

    public Company getInfo(String id) {

        try {

            final Document doc = Jsoup.connect(API_ENDPOINT + id).get();
            final String name = doc.select(NAME_FIELD).first().text();
            final String address = doc.select(ADDRESS_FIELD).first().text();

            return new Company(id, name, address, null);

        } catch (IOException e) {
            LOG.error(e);
        }

        return null;
    }
}
