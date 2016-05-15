package com.doterob.transparencia.connector.contract.extractor.xunta;

import com.doterob.transparencia.model.Contract;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by dotero on 15/05/2016.
 */
public class XuntaContractPageResponseHandler implements ResponseHandler<XuntaContractPage> {

    @Override
    public XuntaContractPage handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        final HttpEntity entity = response.getEntity();
        final String html = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(response.getEntity()));

        final Document doc = Jsoup.parse(html);
        final Elements nextLink = doc.select("div.paxinador").last().select("li").last().select("a");
        return new XuntaContractPage(Arrays.asList(doc.select("td.c115").select("span").text().split(" ")),
                nextLink.size() > 0 ? nextLink.attr("href") : null);
    }
}
