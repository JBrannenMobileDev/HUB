package jjpartnership.hub.data_layer.data_models;

/**
 * Created by jbrannen on 2/23/18.
 */

public class Employee{
    private String firstName;
    private String lastName;
    private String email;
    private String employeeId;

    public Employee() {
    }

    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Employee(EmployeeRealm employeeRealm) {
        this.firstName = employeeRealm.getFirstName();
        this.lastName = employeeRealm.getLastName();
        this.email = employeeRealm.getEmail();
        this.employeeId = employeeRealm.getEmployeeId();
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployeeId() {
        return employeeId != null ? employeeId : "";
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
