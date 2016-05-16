package com.doterob.transparencia.connector.contract.extractor.xunta;

import com.doterob.transparencia.model.ContractComplex;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dotero on 14/05/2016.
 */
public class XuntaConnectorPooling implements XuntaConnector {

    private static final Logger LOG = LogManager.getLogger(XuntaConnectorPooling.class);

    public List<ContractComplex> extract(Date start, Date end) {

        final List<ContractComplex> result = new ArrayList<>();

        final CookieStore cookies = new BasicCookieStore();
        final PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(MAX_CONNECTIONS);
        final CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).setDefaultCookieStore(cookies).build();

        try {

            client.execute(XuntaRequestBuilder.session());

            final List<FindThread> threads = new ArrayList<FindThread>();
            XuntaContractPage page = client.execute(XuntaRequestBuilder.search(start,end), new XuntaContractPageResponseHandler());
            do {
                for (final String code : page.getCodes()) {
                    threads.add(new FindThread(client, code));
                }

                if(page.getNext() != null) {
                    page = client.execute(XuntaRequestBuilder.next(page.getNext()), new XuntaContractPageResponseHandler());
                }
            }while (page.getNext() != null);

            for (FindThread thread : threads){
                thread.start();
            }

            for (FindThread thread : threads){
                thread.join();
            }

            for (FindThread thread : threads){
                result.addAll(thread.getResult());
            }

            return result;

        } catch (IOException | InterruptedException e){
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

    private static class FindThread extends Thread {

        private final CloseableHttpClient client;
        private final HttpContext context;
        private final String code;
        private List<ContractComplex> result;

        public FindThread(CloseableHttpClient client, String code) {
            this.client = client;
            this.context = HttpClientContext.create();
            this.code = code;
        }

        @Override
        public void run() {
            try {

                result = client.execute(XuntaRequestBuilder.find(code), new ContractResponseHandler(), context);

            } catch (IOException e) {
                LOG.error(e);
            }
        }

        public List<ContractComplex> getResult(){
            return result;
        }

    }
}
