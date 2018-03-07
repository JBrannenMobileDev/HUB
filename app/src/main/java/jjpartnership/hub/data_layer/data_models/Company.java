package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by jbrannen on 2/23/18.
 */

public class Company{
    private String companyId;
    private String name;
    private String address;
    private String companyEmailDomain;
    private boolean salesCompany;
    private boolean buyerCompany;
    private List<String> roles;
    private List<String> businessUnits;
    private List<String> industryList;
    private List<String> employeeList;
    private List<String> accountList;

    public Company() {
    }

    public Company(String companyId, String name, String address, String companyEmailDomain,
                   List<String> roles, List<String> businessUnits, List<String> industryList,
                   List<String> employeeList, List<String> accountList, boolean salesCompany, boolean buyerCompany) {
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

    public Company(CompanyRealm realmCompany) {
        this.companyId = realmCompany.getCompanyId();
        this.name = realmCompany.getName();
        this.address = realmCompany.getAddress();
        this.businessUnits = realmCompany.getBusinessUnits();
        this.roles = realmCompany.getRoles();
        this.companyEmailDomain = realmCompany.getCompanyEmailDomain();
        this.industryList = realmCompany.getIndustryList();
        this.employeeList = realmCompany.getEmployeeList();
        this.accountList = realmCompany.getAccountList();
        this.salesCompany = realmCompany.isSalesCompany();
        this.buyerCompany = realmCompany.isBuyerCompany();
    }

    public void addNewEmployee(String userId){
        if(employeeList == null){
            employeeList = new ArrayList<>();
        }
        employeeList.add(userId);
    }

    public void addIndustry(String industry){
        if(industryList == null){
            industryList = new ArrayList<>();
        }
        industryList.add(industry);
    }

    public void addAccount(String accountId) {
        if(accountList == null){
            accountList = new ArrayList<>();
        }
        accountList.add(accountId);
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(List<String> businessUnits) {
        this.businessUnits = businessUnits;
    }

    public List<String> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<String> industryList) {
        this.industryList = industryList;
    }

    public List<String> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<String> employeeList) {
        this.employeeList = employeeList;
    }

    public List<String> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<String> accountList) {
        this.accountList = accountList;
    }
}
