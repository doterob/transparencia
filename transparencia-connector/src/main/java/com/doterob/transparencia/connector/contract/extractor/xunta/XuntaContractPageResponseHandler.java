package com.doterob.transparencia.connector.contract.extractor.xunta;

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
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by dotero on 15/05/2016.
 */
public class XuntaContractPageResponseHandler implements ResponseHandler<XuntaContractPage> {

    private static final Logger LOG = LogManager.getLogger(XuntaContractPageResponseHandler.class);

    @Override
    public XuntaContractPage handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        final HttpEntity entity = response.getEntity();
        final String html = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(response.getEntity()));

        try {
            final Document doc = Jsoup.parse(html);
            final Elements divLink = doc.select("div.paxinador").last().select("li");
            final Elements nextLink = divLink.size() > 0 ? divLink.last().select("a") : divLink;
            return new XuntaContractPage(Arrays.asList(doc.select("td.c115").select("span").text().split(" ")),
                    nextLink.size() > 0 ? nextLink.attr("href") : null);
        } catch (Exception e){
            LOG.error("Error parseando índice de contratos", e);
            throw e;
        }
    }
}
