package com.doterob.transparencia.connector.company;

import com.doterob.transparencia.connector.geocoding.GoogleGeocodingService;
import com.doterob.transparencia.model.Company;
import com.doterob.transparencia.model.ContractComplex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dotero on 15/05/2016.
 */
public class AxesorResponseHandler implements ResponseHandler<Company> {

    private static final Logger LOG = LogManager.getLogger(AxesorResponseHandler.class);

    //private static final String ID_FIELD = "h2.col-md-10.col-sm-9.col-xs-12.mtnone.mb10.sessionnif";
    private static final String NAME_FIELD = "div.main_mid_colA";
    private static final String ADDRESS_FIELD = "Direccion";

    @Override
    public Company handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        final Map<Integer, ContractComplex> result = new HashMap<Integer, ContractComplex>();

        final HttpEntity entity = response.getEntity();
        final String html = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(entity));
        final Document doc = Jsoup.parse(html);

        final Element elementName = doc.getElementById("informacion_general");
        if(elementName == null){
            //LOG.warn("No se ha obtenido empresa -> " + html);
            return null;
        }

        final String id = null;
        final String name = elementName.select(NAME_FIELD).select("h3").text().trim();
        final String address = doc.getElementById(ADDRESS_FIELD).parent().select("dd").text();

        return new Company(id, name, GoogleGeocodingService.getInstance().getAddress(address));
    }
}
