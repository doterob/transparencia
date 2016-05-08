package com.doterob.transparencia.elasticsearch;

import com.doterob.transparencia.model.Contract;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by dotero on 08/05/2016.
 */
public class ESClient implements com.doterob.transparencia.elasticsearch.Client {

    @Override
    public void index(List<Contract> contracts) throws UnknownHostException{

        Settings settings = Settings.settingsBuilder()
                .put("client.transport.sniff", true).build();

        Client client = TransportClient.builder().settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host1"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));

        IndexResponse response = client.prepareIndex("contratacion", "local")
                .setSource(contracts.toString())
                .get();

        client.close();
    }
}
