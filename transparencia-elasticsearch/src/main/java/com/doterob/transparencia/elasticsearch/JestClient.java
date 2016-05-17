package com.doterob.transparencia.elasticsearch;

import com.doterob.transparencia.model.Publishing;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by dotero on 08/05/2016.
 */
public class JestClient implements Client {

    private static final Logger LOG = LogManager.getLogger(JestClient.class);

    @Override
    public void indexContracts(String index, String type, List<Publishing> publishings) throws UnknownHostException, IOException {

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
                Index i = new Index.Builder(mapper.writeValueAsString(p)).index(index).type(type).build();
                bulk.addAction(i);
            }
            for(BulkResult.BulkResultItem r : client.execute(bulk.build()).getItems()) {
                if (r.error != null) {
                    LOG.error("Error indexando " + r.id + " -> " + r.error);
                }
            }
        }finally {
            client.shutdownClient();
        }

    }
}
