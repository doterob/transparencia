package com.doterob.transparencia.connector.contract.extractor.xunta;

import com.doterob.transparencia.model.Contract;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dotero on 14/05/2016.
 */
public class XuntaConnectorExecutor implements XuntaConnector {

    private static final Logger LOG = LogManager.getLogger(XuntaConnectorExecutor.class);

    @Override
    public List<Contract> extract(Date start, Date end) {

        final List<Contract> result = new ArrayList<>();

        final CookieStore cookies = new BasicCookieStore();
        final PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(MAX_CONNECTIONS);
        final CloseableHttpClient client = HttpClientBuilder.create().setConnectionManager(manager).setDefaultCookieStore(cookies).setMaxConnPerRoute(5).build();
        final ExecutorService executorService = Executors.newFixedThreadPool(MAX_CONNECTIONS);
        final FutureRequestExecutionService futureRequestExecutionService =
                new FutureRequestExecutionService(client, executorService);



        try {

            client.execute(XuntaRequestBuilder.session());
            final List<HttpRequestFutureTask<List<Contract>>> tasks = new ArrayList<HttpRequestFutureTask<List<Contract>>>();
            XuntaContractPage page = client.execute(XuntaRequestBuilder.search(start,end), new XuntaContractPageResponseHandler());
            do{

                for(final String code : page.getCodes()) {
                    tasks.add(futureRequestExecutionService.execute(
                            XuntaRequestBuilder.find(code), HttpClientContext.create(),
                            new ContractResponseHandler()));
                }

                if(page.getNext() != null) {
                    page = client.execute(XuntaRequestBuilder.next(page.getNext()), new XuntaContractPageResponseHandler());
                }
            }while (page.getNext() != null);

            for (HttpRequestFutureTask<List<Contract>> task : tasks){
                result.addAll(task.get());
            }

            return result;

        } catch (IOException | InterruptedException | ExecutionException e){
            LOG.error(e);
            System.out.println(e);
        } finally {

            try {
                client.close();
                manager.close();
            } catch (IOException e){
                    LOG.error(e);
                    System.out.println(e);
                }
        }

        return null;
    }
}
