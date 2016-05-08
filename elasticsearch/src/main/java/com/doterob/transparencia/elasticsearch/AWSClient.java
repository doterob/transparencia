package com.doterob.transparencia.elasticsearch;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticsearch.AWSElasticsearchClient;
import com.amazonaws.services.elasticsearch.model.AddTagsRequest;
import com.amazonaws.services.elasticsearch.model.Tag;
import com.doterob.transparencia.model.Contract;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dotero on 08/05/2016.
 */
public class AWSClient implements com.doterob.transparencia.elasticsearch.Client {

    AWSElasticsearchClient client;

    private AWSClient(){
        client = new AWSElasticsearchClient();
        client.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));
        client.setEndpoint("es.eu-central-1.amazon.aws.com");
    }

    public void index(final List<Contract> contracts) throws UnknownHostException{

        final List<Tag> tags = new ArrayList<>();

        final AddTagsRequest request = new AddTagsRequest();
        request.withTagList(convertAll(contracts));

        client.addTags(request);
    }

    private List<Tag> convertAll(List<Contract> contracts){
        final List<Tag> result = new ArrayList<>();
        for(Contract c : contracts){
            result.addAll(convert(c));
        }
        return result;
    }

    private List<Tag> convert(Contract contract){
        final List<Tag> result = new ArrayList<Tag>();
        result.add(tag("id", contract.getId()));
        result.add(tag("date", contract.getDate()));
        result.add(tag("subject", contract.getSubject()));
        result.add(tag("entityId", contract.getEntityId()));
        result.add(tag("entityName", contract.getEntityId()));
        result.add(tag("area", contract.getArea()));
        result.add(tag("amount", contract.getAmount()));
        return result;
    }

    private Tag tag(String key, String value){
        final Tag result = new Tag();
        result.withKey(key);
        result.withValue(value);
        return result;
    }
}
