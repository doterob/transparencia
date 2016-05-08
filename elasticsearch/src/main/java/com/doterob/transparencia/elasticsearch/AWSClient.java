package com.doterob.transparencia.elasticsearch;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticsearch.AWSElasticsearchClient;
import com.amazonaws.services.elasticsearch.model.AddTagsRequest;
import com.amazonaws.services.elasticsearch.model.Tag;
import com.doterob.transparencia.model.Contract;
import com.doterob.transparencia.model.Publishing;

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
        client.setEndpoint("search-transparencia-3kwgan64yqnamybgoc5xip3wce.eu-central-1.es.amazonaws.com");
    }

    public void indexContracts(String index, List<Publishing> publishings) throws UnknownHostException{

        final List<Tag> tags = new ArrayList<>();

        final AddTagsRequest request = new AddTagsRequest();
        request.withTagList(convertAll(publishings));

        client.addTags(request);
    }

    private List<Tag> convertAll(List<? extends  Contract> contracts){
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
        result.add(tag("entityId", contract.getContractorId()));
        result.add(tag("entityName", contract.getContractorId()));
        result.add(tag("area", contract.getOrganizationArea()));
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
