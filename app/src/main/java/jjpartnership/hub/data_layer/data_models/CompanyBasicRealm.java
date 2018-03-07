package jjpartnership.hub.data_layer.data_models;

import java.util.Collections;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 3/6/18.
 */

public class CompanyBasicRealm extends RealmObject{
    @PrimaryKey
    private String companyBasicId;
    private String companyId;
    private String name;
    private String address;
    private String emailDomain;
    private boolean salesCompany;
    private boolean buyerCompany;
    private RealmList<String> industryIdList;

    public CompanyBasicRealm() {
    }

    public CompanyBasicRealm(String companyBasicId, String companyId, String name, RealmList<String> industryIdList, String address, String emailDomain) {
        this.companyBasicId = companyBasicId;
        this.companyId = companyId;
        this.name = name;
        this.industryIdList = industryIdList;
        this.address = address;
        this.emailDomain = emailDomain;
    }

    public CompanyBasicRealm(CompanyBasic companyBasic){
        this.companyBasicId = companyBasic.getCompanyBasicId();
        this.companyId = companyBasic.getCompanyId();
        this.name = companyBasic.getName();
        this.industryIdList = createIndustryList(companyBasic.getIndustryIdList());
        this.address = companyBasic.getAddress();
        this.emailDomain = companyBasic.getEmailDomain();
        this.salesCompany = companyBasic.isSalesCompany();
        this.buyerCompany = companyBasic.isBuyerCompany();
    }

    private RealmList<String> createIndustryList(List<String> industryIdList) {
        RealmList<String> industryIdListRealm = new RealmList<>();
        for(String industryId : industryIdList){
            industryIdListRealm.add(industryId);
        }
        Collections.reverse(industryIdListRealm);
        return industryIdListRealm;
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

    public RealmList<String> getIndustryIdList() {
        return industryIdList;
    }

    public void setIndustryIdList(RealmList<String> industryIdList) {
        this.industryIdList = industryIdList;
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
