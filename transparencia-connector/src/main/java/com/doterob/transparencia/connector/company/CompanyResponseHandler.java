package com.doterob.transparencia.connector.company;

import com.doterob.transparencia.connector.geocoding.GoogleGeocodingService;
import com.doterob.transparencia.model.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dotero on 15/05/2016.
 */
public class CompanyResponseHandler implements ResponseHandler<Company> {

    private static final String ID_FIELD = "col-md-10.col-sm-9.col-xs-12.mtnone.mb10.sessionnif";
    private static final String NAME_FIELD = "h1.title.title-sm.title-margin.mtnone.roboto.mbnone";
    private static final String ADDRESS_FIELD = "div.col-md-10.col-sm-9.col-xs-12.mb10";

    @Override
    public Company handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        final Map<Integer, ContractComplex> result = new HashMap<Integer, ContractComplex>();

        final HttpEntity entity = response.getEntity();
        final String html = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(entity));
        final Document doc = Jsoup.parse(html);

        final String id = doc.select(ID_FIELD).first().text();
        final String name = doc.select(NAME_FIELD).first().text();
        final String address = doc.select(ADDRESS_FIELD).first().text();

        return new Company(id, name, GoogleGeocodingService.getInstance().getAddress(address));
    }
}
