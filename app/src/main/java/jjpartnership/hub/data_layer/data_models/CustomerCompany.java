package jjpartnership.hub.data_layer.data_models;

/**
 * Created by jbrannen on 2/28/18.
 */

public class CustomerCompany {
    private String companyName;
    private String location;
    private String companyEmailDomain;

    public CustomerCompany() {
    }

    public CustomerCompany(String companyName, String location, String companyEmailDomain) {
        this.companyName = companyName;
        this.location = location;
        this.companyEmailDomain = companyEmailDomain;
    }

    public CustomerCompany(CustomerCompanyRealm customer){
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
