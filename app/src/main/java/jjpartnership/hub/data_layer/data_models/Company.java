package jjpartnership.hub.data_layer.data_models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbrannen on 2/23/18.
 */

public class Company{
    private String companyId;
    private String name;
    private String location;
    private String companyType;
    private String companyEmailDomain;
    private List<String> businessUnits;
    private List<String> roles;
    private List<Employee> employeeList;

    public Company() {
    }

    public Company(String name, String location, String companyType, List<Employee> employeeList) {
        this.name = name;
        this.location = location;
        this.companyType = companyType;
        this.employeeList = employeeList;
    }

    public Company(CompanyRealm realmCompany) {
        this.companyId = realmCompany.getCompanyId();
        this.name = realmCompany.getName();
        this.location = realmCompany.getLocation();
        this.companyType = realmCompany.getCompanyType();
        this.employeeList = createEmployeeList(realmCompany.getEmployeeList());
        this.businessUnits = realmCompany.getBusinessUnits();
        this.roles = realmCompany.getRoles();
        this.companyEmailDomain = realmCompany.getCompanyEmailDomain();
    }

    private List<Employee> createEmployeeList(List<EmployeeRealm> employeeListRealm) {
        List<Employee> employeeList = new ArrayList<>();
        for(EmployeeRealm employeeRealm : employeeListRealm){
            employeeList.add(new Employee(employeeRealm));
        }
        return employeeList;
    }

    public String getCompanyEmailDomain() {
        return companyEmailDomain;
    }

    public void setCompanyEmailDomain(String companyEmailDomain) {
        this.companyEmailDomain = companyEmailDomain;
    }

    public List<String> getBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(List<String> businessUnits) {
        this.businessUnits = businessUnits;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
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

    public List<Employee> getEmployeeList() {
        return employeeList != null ? employeeList : new ArrayList<Employee>();
    }

    public void setEmployeeList(List<Employee> employeeIdList) {
        this.employeeList = employeeIdList;
    }
}
