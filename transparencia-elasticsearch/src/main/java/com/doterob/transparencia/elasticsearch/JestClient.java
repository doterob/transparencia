package com.doterob.transparencia.elasticsearch;

import com.doterob.transparencia.model.Publishing;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

/**
 * Created by dotero on 08/05/2016.
 */
public class JestClient implements Client {

    @Override
    public void indexContracts(String index, List<Publishing> publishings) throws UnknownHostException, IOException {

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(JestConfig.getInstance().getHttpClientConfig());

        JestHttpClient client = (JestHttpClient) factory.getObject();

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));
            Bulk.Builder bulk = new Bulk.Builder()
                    .defaultIndex(index)
                    .defaultType(publishings.get(0).getType().toString());

            for (Publishing p : publishings) {
                bulk.addAction(new Index.Builder(mapper.writeValueAsString(p)).index(index).type(p.getType().toString()).build());
            }

            System.out.println(client.execute(bulk.build()).getItems().get(0).error);
        }finally {
            client.shutdownClient();
        }

    }
}
