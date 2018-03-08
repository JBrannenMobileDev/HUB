package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 2/23/18.
 */

public class CompanyRealm extends RealmObject{
    @PrimaryKey private String companyId;
    private String name;
    private String address;
    private String companyEmailDomain;
    private boolean salesCompany;
    private boolean buyerCompany;
    private RealmList<String> roles;
    private RealmList<String> businessUnits;
    private RealmList<String> industryList;
    private RealmList<String> employeeList;
    private RealmList<String> accountList;

    public CompanyRealm() {
    }

    public CompanyRealm(String companyId, String name, String address, String companyEmailDomain,
                        RealmList<String> roles, RealmList<String> businessUnits, RealmList<String> industryList,
                        RealmList<String> employeeList, RealmList<String> accountList, boolean salesCompany, boolean buyerCompany) {
        this.companyId = companyId;
        this.name = name;
        this.address = address;
        this.companyEmailDomain = companyEmailDomain;
        this.roles = roles;
        this.businessUnits = businessUnits;
        this.industryList = industryList;
        this.employeeList = employeeList;
        this.accountList = accountList;
        this.salesCompany = salesCompany;
        this.buyerCompany = buyerCompany;
    }

    public CompanyRealm(Company company) {
        this.name = company.getName();
        this.address = company.getAddress();
        this.businessUnits = createBusinessUnits(company.getBusinessUnits());
        this.roles = createRoles(company.getRoles());
        this.companyEmailDomain = company.getCompanyEmailDomain();
        this.industryList = createIndustryList(company.getIndustryList());
        this.employeeList = createEmployeeList(company.getEmployeeList());
        this.accountList = createAccountList(company.getAccountList());
        this.salesCompany = company.isSalesCompany();
        this.buyerCompany = company.isBuyerCompany();
    }

    private RealmList<String> createIndustryList(List<String> industryList) {
        if(industryList != null) {
            RealmList<String> realmIndustries = new RealmList<>();
            for (String industry : industryList) {
                realmIndustries.add(industry);
            }
            Collections.reverse(realmIndustries);
            return realmIndustries;
        }else{
            return new RealmList<>();
        }
    }

    private RealmList<String> createRoles(List<String> roles) {
        if(roles != null) {
            RealmList<String> realmRoles = new RealmList<>();
            if (roles != null) {
                for (String role : roles) {
                    realmRoles.add(role);
                }
                Collections.reverse(realmRoles);
            }
            return realmRoles;
        }else{
            return new RealmList<>();
        }
    }

    private RealmList<String> createBusinessUnits(List<String> businessUnits) {
        if(businessUnits != null) {
            RealmList<String> realmBusinessUnits = new RealmList<>();
            if (businessUnits != null) {
                for (String unit : businessUnits) {
                    realmBusinessUnits.add(unit);
                }
                Collections.reverse(realmBusinessUnits);
            }
            return realmBusinessUnits;
        }else{
            return new RealmList<>();
        }
    }

    private RealmList<String> createEmployeeList(List<String> employeeList) {
        if(employeeList != null) {
            RealmList<String> realmEmployees = new RealmList<>();
            for (String employee : employeeList) {
                realmEmployees.add(employee);
            }
            Collections.reverse(realmEmployees);
            return realmEmployees;
        }else{
            return new RealmList<>();
        }
    }

    private RealmList<String> createAccountList(List<String> accountList) {
        if(accountList != null) {
            RealmList<String> realmAccounts = new RealmList<>();
            for (String industry : accountList) {
                realmAccounts.add(industry);
            }
            Collections.reverse(realmAccounts);
            return realmAccounts;
        }else{
            return new RealmList<>();
        }
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

    public String getCompanyEmailDomain() {
        return companyEmailDomain;
    }

    public void setCompanyEmailDomain(String companyEmailDomain) {
        this.companyEmailDomain = companyEmailDomain;
    }

    public RealmList<String> getRoles() {
        return roles;
    }

    public void setRoles(RealmList<String> roles) {
        this.roles = roles;
    }

    public RealmList<String> getBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(RealmList<String> businessUnits) {
        this.businessUnits = businessUnits;
    }

    public RealmList<String> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(RealmList<String> industryList) {
        this.industryList = industryList;
    }

    public RealmList<String> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(RealmList<String> employeeList) {
        this.employeeList = employeeList;
    }

    public RealmList<String> getAccountList() {
        return accountList;
    }

    public void setAccountList(RealmList<String> accountList) {
        this.accountList = accountList;
    }
}
