package com.doterob.transparencia.elasticsearch;

import com.doterob.transparencia.model.Contract;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by dotero on 08/05/2016.
 */
public interface Client {
    void index(List<Contract> contracts) throws UnknownHostException;
}
