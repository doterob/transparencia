package com.doterob.transparencia.elasticsearch;

import com.doterob.transparencia.model.Publishing;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by dotero on 08/05/2016.
 */
public interface Client {
    void indexContracts(String index, String type, List<Publishing> publishings) throws UnknownHostException, IOException;
}
