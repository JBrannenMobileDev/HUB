package jjpartnership.hub.data_layer.data_models;

import java.util.List;

/**
 * Created by jbrannen on 3/6/18.
 */

public class CompanyBasic {
    private String companyBasicId;
    private String companyId;
    private String name;
    private String address;
    private String emailDomain;
    private boolean salesCompany;
    private boolean buyerCompany;
    private List<String> industryIdList;

    public CompanyBasic() {
    }

    public CompanyBasic(String companyBasicId, String companyId, String name, List<String> industryIdList,
                        String address, String emailDomain, boolean salesCompany, boolean buyerCompany) {
        this.companyBasicId = companyBasicId;
        this.companyId = companyId;
        this.name = name;
        this.industryIdList = industryIdList;
        this.address = address;
        this.emailDomain = emailDomain;
        this.salesCompany = salesCompany;
        this.buyerCompany = buyerCompany;
    }

    public CompanyBasic(CompanyBasicRealm realm){
        this.companyBasicId = realm.getCompanyBasicId();
        this.companyId = realm.getCompanyId();
        this.name = realm.getName();
        this.industryIdList = realm.getIndustryIdList();
        this.address = realm.getAddress();
        this.emailDomain = realm.getEmailDomain();
        this.salesCompany = realm.isSalesCompany();
        this.buyerCompany = realm.isBuyerCompany();
    }

    public List<String> getIndustryIdList() {
        return industryIdList;
    }

    public void setIndustryIdList(List<String> industryIdList) {
        this.industryIdList = industryIdList;
    }

    public boolean isSalesCompany() {
        return salesCompany;
    }

    public void setSalesCompany(boolean salesCompany) {
        this.salesCompany = salesCompany;
    }

    public boolean isBuyerCompany() {
        return buyerCompany;
    }

    public void setBuyerCompany(boolean buyerCompany) {
        this.buyerCompany = buyerCompany;
    }

    public String getCompanyBasicId() {
        return companyBasicId;
    }

    public void setCompanyBasicId(String companyBasicId) {
        this.companyBasicId = companyBasicId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }
}
