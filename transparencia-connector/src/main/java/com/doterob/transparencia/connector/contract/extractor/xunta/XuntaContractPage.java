package com.doterob.transparencia.connector.contract.extractor.xunta;

import java.util.List;

/**
 * Created by dotero on 16/05/2016.
 */
public class XuntaContractPage {

    private final List<String> codes;
    private final String next;

    public XuntaContractPage(List<String> codes, String next) {
        this.codes = codes;
        this.next = next;
    }

    public List<String> getCodes() {
        return codes;
    }

    public String getNext() {
        return next;
    }
}
