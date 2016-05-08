package com.doterob.transparencia.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dotero on 08/05/2016.
 */
public class Publishing extends Contract {

    private final String organizationName;
    private final OrganizationType type;
    private final String source;

    public Publishing(Organization organization, String source, Contract contract) {
        super(contract.getId(), contract.getDate(), contract.getSubject(), contract.getContractorId(),
                contract.getContractorName(), contract.getOrganizationArea(), contract.getAmount());
        this.organizationName = organization.getName();
        this.type = organization.getType();
        this.source = source;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public OrganizationType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public static class Builder {

        private Organization organization;
        private String source;

        public Builder setOrganization(Organization organization) {
            this.organization = organization;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public List<Publishing> build(List<Contract> contracts){

            final List<Publishing> result = new ArrayList<>();
            for (Contract c : contracts){
                result.add(new Publishing(organization, source, c));
            }

            return result;
        }
    }
}
