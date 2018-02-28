package jjpartnership.hub.data_layer.data_models;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by jbrannen on 2/23/18.
 */

public class EmployeeRealm extends RealmObject{
    private String firstName;
    private String lastName;
    private String email;
    private String employeeId;

    public EmployeeRealm() {
    }

    public EmployeeRealm(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public EmployeeRealm(Employee employee) {
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        this.employeeId = employee.getEmployeeId();
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
