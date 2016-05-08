package com.doterob.transparencia.elasticsearch;

import com.doterob.transparencia.model.Publishing;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.indices.mapping.PutMapping;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dotero on 08/05/2016.
 */
public class JestClient implements Client {

    @Override
    public void indexContracts(String index, List<Publishing> publishings) throws UnknownHostException, IOException {

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("https://search-transparencia-3kwgan64yqnamybgoc5xip3wce.eu-central-1.es.amazonaws.com")
                .defaultCredentials("transparencia-user","123456")
                .multiThreaded(true)
                .build());

        JestHttpClient client = (JestHttpClient) factory.getObject();

        Bulk.Builder bulk = new Bulk.Builder()
                .defaultIndex(index)
                .defaultType(publishings.get(0).getType().toString());

        for(Publishing p : publishings){
            System.out.println(client.execute(new Index.Builder(p).index(index).type(p.getType().toString()).build()).getErrorMessage());
        }

        //System.out.println(client.execute(bulk.build()).getErrorMessage());
       client.shutdownClient();
    }
}
