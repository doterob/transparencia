package com.doterob.transparencia.connector;

import com.doterob.transparencia.connector.company.CompanyService;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by dotero on 16/05/2016.
 */
public abstract class HttpService {

    private static final Logger LOG = LogManager.getLogger(CompanyService.class);
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
            client.close();
            manager.close();
        } catch (IOException e){
            LOG.error(e);
            System.out.println(e);
        }
    }
}
