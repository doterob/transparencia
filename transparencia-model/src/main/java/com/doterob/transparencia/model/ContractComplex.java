package com.doterob.transparencia.model;

/**
 * Created by dotero on 07/05/2016.
 */
public class ContractComplex {

    private final Contract contract;
    private final Organization organization;
    private final String nif;
    private final String companyName;

    public ContractComplex(Contract contract, Organization organization, String nif, String companyName) {
        this.contract = contract;
        this.organization = organization;
        this.nif = nif;
        this.companyName = companyName;
    }

    public Contract getContract() {
        return contract;
    }

    public Organization getOrganization() {
        return organization;
    }

    public String getNif() {
        return nif;
    }

    public String getCompanyName() {
        return companyName;
    }
}
