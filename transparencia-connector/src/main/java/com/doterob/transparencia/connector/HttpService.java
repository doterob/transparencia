package com.doterob.transparencia.connector;

import com.doterob.transparencia.connector.company.CompanyService;
import com.doterob.transparencia.connector.contract.extractor.xunta.ContractResponseHandler;
import com.doterob.transparencia.connector.contract.extractor.xunta.XuntaRequestBuilder;
import com.doterob.transparencia.model.ContractComplex;
import org.apache.http.HttpRequest;
import org.apache.http.client.CookieStore;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Created by dotero on 16/05/2016.
 */
public abstract class HttpService {

    private static final Logger LOG = LogManager.getLogger(HttpService.class);
    private static final int MAX_CONNECTIONS = 10;

    private final PoolingHttpClientConnectionManager manager;
    protected final CloseableHttpClient client;
    protected final FutureRequestExecutionService futureRequestExecutionService;

    public HttpService(){
        this(MAX_CONNECTIONS);
    }
    public HttpService(Integer maxConnections){

        final CookieStore cookies = new BasicCookieStore();

        manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(MAX_CONNECTIONS);

        client = HttpClientBuilder.create().setConnectionManager(manager).setDefaultCookieStore(cookies).setMaxConnPerRoute(MAX_CONNECTIONS).build();
        futureRequestExecutionService = new FutureRequestExecutionService(client, Executors.newFixedThreadPool(MAX_CONNECTIONS));
    }

    public void close(){

        try {
            manager.close();
            client.close();
        } catch (IOException e){
            LOG.error("Error cerrando HttpClient", e);
        }
    }

    protected <R> R request(final HttpRequestBase request, final ResponseHandler<R> handler){

        try {
                return client.execute(request, handler);
            } catch (Exception e){
                LOG.error("Error obtener : " + request.getURI().toString(), e);
            }

        return null;
    }

    protected <R> Map<String, R> requestAll(final Map<String, ? extends HttpRequestBase> requests, final ResponseHandler<R> handler){

        final Map<String, R> result = new HashMap<String, R>();
        final Map<String, HttpRequestFutureTask<R>> tasks = new HashMap<String, HttpRequestFutureTask<R>>();

        for(final Map.Entry<String,  ? extends HttpRequestBase> request : requests.entrySet()) {
            tasks.put(request.getKey(), futureRequestExecutionService.execute(
                    request.getValue(), HttpClientContext.create(),
                    handler));
        }

        for (Map.Entry<String, HttpRequestFutureTask<R>> task : tasks.entrySet()){
            try {
                result.put(task.getKey(), task.getValue().get());
            } catch (Exception e){
                LOG.error("Error obtener : " + task.getKey(), e);
            }
        }

        return result;
    }
}
