package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;

/**
 * Created by jbrannen on 2/28/18.
 */

public class CustomerCompanyRealm extends RealmObject{
    private String companyName;
    private String location;
    private String companyEmailDomain;

    public CustomerCompanyRealm() {
    }

    public CustomerCompanyRealm(String companyName, String location, String companyEmailDomain) {
        this.companyName = companyName;
        this.location = location;
        this.companyEmailDomain = companyEmailDomain;
    }

    public CustomerCompanyRealm(CustomerCompany customer){
        this.companyName = customer.getCompanyName();
        this.location = customer.getLocation();
        this.companyEmailDomain = customer.getCompanyEmailDomain();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCompanyEmailDomain() {
        return companyEmailDomain;
    }

    public void setCompanyEmailDomain(String companyEmailDomain) {
        this.companyEmailDomain = companyEmailDomain;
    }
}
