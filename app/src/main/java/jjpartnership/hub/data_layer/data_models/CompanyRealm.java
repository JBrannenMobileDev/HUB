package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jbrannen on 2/23/18.
 */

public class CompanyRealm extends RealmObject{
    @PrimaryKey private String companyId;
    private String name;
    private String location;
    private String companyType;
    private String companyEmailDomain;
    private RealmList<String> businessUnits;
    private RealmList<String> roles;
    private RealmList<EmployeeRealm> employeeList;
    private RealmList<CustomerCompanyRealm> customers;

    public CompanyRealm() {
    }

    public CompanyRealm(String name, String location, String companyType, RealmList<EmployeeRealm> employeeList) {
        this.name = name;
        this.location = location;
        this.companyType = companyType;
        this.employeeList = employeeList;
    }

    public CompanyRealm(Company company) {
        this.name = company.getName();
        this.location = company.getLocation();
        this.companyType = company.getCompanyType();
        this.employeeList = createEmployeeList(company.getEmployeeList());
        this.businessUnits = createBusinessUnits(company.getBusinessUnits());
        this.roles = createRoles(company.getRoles());
        this.companyEmailDomain = company.getCompanyEmailDomain();
        this.customers = createCustomersList(company.getCustomers());
    }

    private RealmList<CustomerCompanyRealm> createCustomersList(List<CustomerCompany> customers) {
        RealmList<CustomerCompanyRealm> realmCustomers = new RealmList<>();
        if(customers != null) {
            for (CustomerCompany customer : customers) {
                realmCustomers.add(new CustomerCompanyRealm(customer));
            }
            Collections.reverse(realmCustomers);
        }
        return realmCustomers;
    }

    private RealmList<String> createRoles(List<String> roles) {
        RealmList<String> realmRoles = new RealmList<>();
        if(roles != null) {
            for (String role : roles) {
                realmRoles.add(role);
            }
            Collections.reverse(realmRoles);
        }
        return realmRoles;
    }

    private RealmList<String> createBusinessUnits(List<String> businessUnits) {
        RealmList<String> realmBusinessUnits = new RealmList<>();
        if(businessUnits != null) {
            for (String unit : businessUnits) {
                realmBusinessUnits.add(unit);
            }
            Collections.reverse(realmBusinessUnits);
        }
        return realmBusinessUnits;
    }

    private RealmList<EmployeeRealm> createEmployeeList(List<Employee> employeeList) {
        RealmList<EmployeeRealm> employeeListRealm = new RealmList<>();
        if(employeeList != null) {
            for (Employee employee : employeeList) {
                employeeListRealm.add(new EmployeeRealm(employee));
            }
            Collections.reverse(employeeListRealm);
        }
        return employeeListRealm;
    }

    public RealmList<CustomerCompanyRealm> getCustomers() {
        return customers;
    }

    public void setCustomers(RealmList<CustomerCompanyRealm> customers) {
        this.customers = customers;
    }

    public String getCompanyEmailDomain() {
        return companyEmailDomain;
    }

    public void setCompanyEmailDomain(String companyEmailDomain) {
        this.companyEmailDomain = companyEmailDomain;
    }

    public RealmList<String> getBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(RealmList<String> businessUnits) {
        this.businessUnits = businessUnits;
    }

    public RealmList<String> getRoles() {
        return roles;
    }

    public void setRoles(RealmList<String> roles) {
        this.roles = roles;
    }

    public String getCompanyType() {
        return companyType != null ? companyType : "";
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public String getCompanyId() {
        return companyId != null ? companyId : "";
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location != null ? location : "";
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<EmployeeRealm> getEmployeeList() {
        return employeeList != null ? employeeList : new ArrayList<EmployeeRealm>();
    }

    public void setEmployeeList(RealmList<EmployeeRealm> employeeIdList) {
        this.employeeList = employeeIdList;
    }
}
