package com.doterob.transparencia.connector.contract.extractor.xunta;

import com.doterob.transparencia.model.Contract;
import com.doterob.transparencia.model.ContractComplex;
import com.doterob.transparencia.model.Organization;
import com.doterob.transparencia.model.OrganizationType;
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
public class ContractResponseHandler implements ResponseHandler<List<ContractComplex>> {

    @Override
    public List<ContractComplex> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        final Map<Integer, ContractComplex> result = new HashMap<Integer, ContractComplex>();

        final HttpEntity entity = response.getEntity();
        final String html = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(entity));
        final Document doc = Jsoup.parse(html);

        final String code = doc.select("table.tablaDatos.senMarxe.senBordeTop").first().select("td").last().text().trim();
        final String organizationName = "Xunta de Galicia";
        final String area = doc.select("div.titulo").text().trim();
        final String subject = doc.select("table.tablaDatos.senMarxe").get(1).select("td").select("span").first().text();
        final String url = doc.select("table.tablaDatos.senMarxe.senBordeTop").first().select("td").select("a").text();
        final String type = doc.select("table.tablaDatos.senMarxe.senBordeTop").first().select("td").get(3).text();

        for(int i = 1; i < doc.getElementById("tabs-4").select("td.c65").size(); i++) {

            final String  _lot = doc.getElementById("tabs-4").select("td.c50").get(i).text().trim();
            final String _date = doc.getElementById("tabs-4").select("td.c65").get(i).text().trim();
            final String contractorName = doc.getElementById("tabs-3").select("td.c464").get(i).text().trim();
            final String contractorId = doc.getElementById("tabs-4").select("td.c242").get(i).text().trim().replace(contractorName, "").trim();
            final String _amount = doc.getElementById("tabs-4").select("td.celdaDerecha.c148").get(i-1).text().replace("€", "").replace(".", "").replace(",", ".").trim();

            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Integer lot = null;
            Date date = null;
            Float amount = null;
            try {
                lot = Integer.parseInt(_lot);
                date = formatter.parse(_date);
                amount = Float.parseFloat(_amount);
            } catch (ParseException | NumberFormatException e) {
            }

            final Contract contract = new Contract(code, date, subject, amount, url, type, lot);
            final Organization organization = new Organization(organizationName, OrganizationType.autonomica, area);
            result.put(lot, new ContractComplex(contract, organization, contractorId, contractorName));
        }

        return new ArrayList<>(result.values());
    }
}
