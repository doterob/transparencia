package com.doterob.transparencia.connector.company;

import com.doterob.transparencia.connector.HttpService;
import com.doterob.transparencia.connector.contract.extractor.xunta.ContractResponseHandler;
import com.doterob.transparencia.connector.contract.extractor.xunta.XuntaContractPage;
import com.doterob.transparencia.connector.contract.extractor.xunta.XuntaContractPageResponseHandler;
import com.doterob.transparencia.connector.contract.extractor.xunta.XuntaRequestBuilder;
import com.doterob.transparencia.connector.geocoding.GoogleGeocodingService;
import com.doterob.transparencia.model.Company;
import com.doterob.transparencia.model.ContractComplex;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dotero on 14/05/2016.
 */
public class CompanyService extends HttpService {

    private static final Logger LOG = LogManager.getLogger(CompanyService.class);

    private static final String API_ENDPOINT = "http://www.infocif.es/general/empresas-informacion-listado-empresas.asp?Buscar=";

    private final GoogleGeocodingService service;

    public CompanyService(){
        super();
        service = GoogleGeocodingService.getInstance();
    }

    public GoogleGeocodingService getService(){
        return service;
    }

    public Map<String, Company> getInfo(List<String> nifs) {

        final Map<String, Company> result = new HashMap<>();

        try {

            client.execute(XuntaRequestBuilder.session());
            final List<HttpRequestFutureTask<Company>> tasks = new ArrayList<HttpRequestFutureTask<Company>>();

            for(String nif : new HashSet<>(nifs)){
                tasks.add(futureRequestExecutionService.execute(
                        new HttpGet(API_ENDPOINT + nif), HttpClientContext.create(),
                        new CompanyResponseHandler()));
            }

            for (HttpRequestFutureTask<Company> task : tasks){
                final Company company = task.get();
                result.put(company.getId(), company);
            }

            return result;

        } catch (IOException | InterruptedException | ExecutionException e){
            LOG.error(e);
            System.out.println(e);
        }

        return null;
    }

    public void close(){

        super.close();
        service.close();
    }
}
