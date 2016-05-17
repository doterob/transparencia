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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger LOG = LogManager.getLogger(ContractResponseHandler.class);
    private static final String URL_BASE = "http://www.contratosdegalicia.es/licitacion";

    @Override
    public List<ContractComplex> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        final Map<Integer, ContractComplex> result = new HashMap<Integer, ContractComplex>();

        final HttpEntity entity = response.getEntity();
        final String html = StringEscapeUtils.unescapeHtml4(EntityUtils.toString(entity));
        final Document doc = Jsoup.parse(html);

        try {

            final String code = doc.select("table.tablaDatos.senMarxe.senBordeTop").first().select("td").last().text().trim();
            final String organizationName = "Xunta de Galicia";
            final String area = doc.select("div.titulo").text().trim();
            final String subject = doc.select("table.tablaDatos.senMarxe").get(1).select("td").select("span").first().text();
            final String url = URL_BASE + doc.select("table.tablaDatos.senMarxe.senBordeTop").first().select("td").select("a").get(0).attr("href");
            final String type = doc.select("table.tablaDatos.senMarxe.senBordeTop").first().select("td").get(3).text();

            for (int i = 1; i < doc.getElementById("tabs-4").select("td.c65").size(); i++) {

                final String _lot = doc.getElementById("tabs-4").select("td.c50").get(i).text().trim();
                final String _date = doc.getElementById("tabs-4").select("td.c65").get(i).text().trim();
                final String contrator = doc.getElementById("tabs-4").select("td.c242").get(i).text().trim();
                final String contractorId = contrator.substring(contrator.lastIndexOf(" "));
                final String contractorName = contrator.replace(contractorId, "").trim();
                final String _amount = doc.getElementById("tabs-4").select("td.celdaDerecha.c148").get(i - 1).text().replace("€", "").replace(".", "").replace(",", ".").trim();

                final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Integer lot = null;
                Date date = null;
                Float amount = null;
                try {
                    lot = _lot != "_" ? Integer.parseInt(_lot) : null;
                } catch (NumberFormatException e) {
                }

                try {
                    date = formatter.parse(_date);
                } catch (ParseException | NumberFormatException e) {
                }

                try {
                    amount = Float.parseFloat(_amount);
                } catch (NumberFormatException e) {
                }

                final Contract contract = new Contract(code, date, subject, amount, url, type, lot);
                final Organization organization = new Organization(organizationName, OrganizationType.autonomica, area);
                result.put(lot, new ContractComplex(contract, organization, contractorId, contractorName));
            }
        } catch (Exception e){
            LOG.error("Error obteniendo contrato " + html, e);
            throw e;
        }

        return new ArrayList<>(result.values());
    }
}
