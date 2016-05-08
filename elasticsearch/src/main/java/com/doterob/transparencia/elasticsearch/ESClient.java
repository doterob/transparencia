package com.doterob.transparencia.elasticsearch;

import com.doterob.transparencia.model.Contract;
import com.doterob.transparencia.model.Organization;
import com.doterob.transparencia.model.Publishing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dotero on 08/05/2016.
 */
public class ESClient implements com.doterob.transparencia.elasticsearch.Client {

    @Override
    public void indexContracts(String index, List<Publishing> publishings) throws UnknownHostException{

        Settings settings = Settings.settingsBuilder()
                .put("client.transport.sniff", true).build();

        Client client = TransportClient.builder().build()
                //.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("52.29.199.198"), 80));
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("search-transparencia-3kwgan64yqnamybgoc5xip3wce.eu-central-1.es.amazonaws.com"), 80));

        ObjectMapper ob = new ObjectMapper();

        try {
            for (Publishing p : publishings) {
                IndexResponse response = client.prepareIndex(index, p.getType().toString())
                        .setSource(ob.writeValueAsString(p)).get();
            }
        }catch (JsonProcessingException e){System.out.println(e);}
        client.close();
    }
}
