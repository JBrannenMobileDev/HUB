package jjpartnership.hub.data_layer.firebase_db;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmResults;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.CompanyType;
import jjpartnership.hub.data_layer.data_models.CustomerCompany;
import jjpartnership.hub.data_layer.data_models.User;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.EmployeeRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.BaseCallback;
import jjpartnership.hub.utils.StringParsingUtil;
import jjpartnership.hub.utils.UserPreferences;

import static android.content.ContentValues.TAG;

/**
 * Created by jbrannen on 2/24/18.
 */

public class FirebaseManager {
    private DatabaseReference userReference;
    private DatabaseReference usersReference;
    private DatabaseReference companiesReference;
    private FirebaseDatabase database;

    public FirebaseManager() {
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        usersReference = database.getReference("users");
        companiesReference = database.getReference("companies");
        initDataListeners();
    }

    private void initDataListeners() {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) {
                    DataManager.getInstance().updateRealmUser(new UserRealm(user));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        };
        userReference.addValueEventListener(userListener);

        ValueEventListener companiesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<CompanyRealm> companies = new ArrayList<>();
                for(DataSnapshot company : dataSnapshot.getChildren()){
                    if(company.getValue(Company.class) != null) {
                        companies.add(new CompanyRealm(company.getValue(Company.class)));
                    }
                }
                if(companies.size() > 0) {
                    DataManager.getInstance().updateRealmCompanies(companies);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        };
        companiesReference.addValueEventListener(companiesListener);
    }

    public void writeNewUser(User user) {
        database.getReference("users").child(user.getUid()).setValue(user);
    }

    public void writeEmailPassword(String password){
        database.getReference("refer_application").child("email_password").setValue(password);
    }

    public void loadCompaniesToFirebase(){
        RealmList<CompanyRealm> companies = new RealmList<>();

        RealmList<EmployeeRealm> comp1Employees = new RealmList<>();
        comp1Employees.add(new EmployeeRealm("Adam", "Smith", "aSmith@gmail.com"));
        comp1Employees.add(new EmployeeRealm("Ryan", "Canty", "rcanty@gmail.com"));
        comp1Employees.add(new EmployeeRealm("John", "churt", "jchurt@gmail.com"));
        CompanyRealm comp1 = new CompanyRealm("Staples", "Murrieta CA", CompanyType.CUSTOMER_COMPANY, comp1Employees);

        RealmList<EmployeeRealm> comp2Employees = new RealmList<>();
        comp2Employees.add(new EmployeeRealm("Stemp", "Smith", "sSmith@gmail.com"));
        comp2Employees.add(new EmployeeRealm("tremp", "currn", "tcurrn@gmail.com"));
        comp2Employees.add(new EmployeeRealm("brent", "child", "bchild@gmail.com"));
        CompanyRealm comp2 = new CompanyRealm("Staples", "Temecula CA", CompanyType.CUSTOMER_COMPANY, comp2Employees);

        RealmList<EmployeeRealm> comp3Employees = new RealmList<>();
        comp3Employees.add(new EmployeeRealm("hint", "trup", "htrup@nationalmerchants.com"));
        comp3Employees.add(new EmployeeRealm("lint", "tnil", "ltnil@nationalmerchants.com"));
        comp3Employees.add(new EmployeeRealm("Jonathan", "Brannen", "jbrannen@nationalmerchants.com"));
        CompanyRealm comp3 = new CompanyRealm("National Merchants Association", "Temecula CA", CompanyType.CUSTOMER_COMPANY, comp3Employees);
        RealmList<String> businessUnits = new RealmList<>();
        businessUnits.add("Marketing");
        businessUnits.add("Inside Sales");
        businessUnits.add("Outside Sales");
        businessUnits.add("Engineering");
        comp3.setBusinessUnits(businessUnits);
        RealmList<String> roles = new RealmList<>();
        roles.add("VP");
        roles.add("CEO");
        roles.add("CTO");
        roles.add("Android Developer");
        comp3.setRoles(roles);
        comp3.setCompanyEmailDomain("nationalmerchants.com");
        CustomerCompany customer3 = new CustomerCompany();

        RealmList<EmployeeRealm> comp4Employees = new RealmList<>();
        comp4Employees.add(new EmployeeRealm("John", "Childers", "johnraychlder@gmail.com"));
        comp4Employees.add(new EmployeeRealm("Jonathan", "Brannen", "jbinvestments15@gmail.com"));
        comp4Employees.add(new EmployeeRealm("Shawna", "Brannen", "shawnaMccollom@yahoo.com"));
        CompanyRealm comp4 = new CompanyRealm("Thermo Fisher", "Los Angeles CA", CompanyType.SALES_COMPANY, comp4Employees);
        RealmList<String> businessUnits4 = new RealmList<>();
        businessUnits4.add("Marketing");
        businessUnits4.add("Sales");
        businessUnits4.add("Engineering");
        businessUnits4.add("ChemicalWeapons");
        comp4.setBusinessUnits(businessUnits4);
        RealmList<String> roles4 = new RealmList<>();
        roles4.add("VP");
        roles4.add("CEO");
        roles4.add("CTO");
        roles4.add("Manager");
        roles4.add("Sales Agent");
        comp4.setRoles(roles4);
        comp4.setCompanyEmailDomain("gmail.com");

        companies.add(comp1);
        companies.add(comp2);
        companies.add(comp3);
        companies.add(comp4);

        List<Company> firebaseCompanies = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            firebaseCompanies.add(new Company(companies.get(i)));
        }

        for(int i = 0; i < 4; i++){
            database.getReference("companies").push().setValue(firebaseCompanies.get(i));
        }
    }

    public void updateUser(String firstName, String lastName, String phoneNumber, String businessUnit, String role) {
        userReference.child("firstName").setValue(firstName);
        userReference.child("lastName").setValue(lastName);
        userReference.child("phoneNumber").setValue(phoneNumber);
        userReference.child("businessUnit").setValue(businessUnit);
        userReference.child("role").setValue(role);
        final BaseCallback<Company> companyBaseCallback = new BaseCallback<Company>() {
            @Override
            public void onResponse(Company company) {
                userReference.child("company").setValue(company);
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    getMatchingCompany(StringParsingUtil.parseEmailDomain(user.getEmail()), companyBaseCallback);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void getMatchingCompanyBoolean(final String emailDomain, final BaseCallback<Boolean> companyNameCallback) {
        companiesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Company> companyList = new ArrayList<>();
                Company matchingCompany = null;
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Company company = data.getValue(Company.class);
                    companyList.add(company);
                }
                for(Company company : companyList){
                    if(company.getCompanyEmailDomain() != null) {
                        if (company.getCompanyEmailDomain().equalsIgnoreCase(emailDomain)) {
                            matchingCompany = company;
                            break;
                        }
                    }
                }
                if(matchingCompany != null){
                    DataManager.getInstance().saveCompanyName(matchingCompany.getName());
                    companyNameCallback.onResponse(true);
                }else{
                    companyNameCallback.onResponse(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getMatchingCompany(final String emailDomain, final BaseCallback<Company> companyNameCallback) {
        companiesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Company> companyList = new ArrayList<>();
                Company matchingCompany = null;
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    Company company = data.getValue(Company.class);
                    companyList.add(company);
                }
                for(Company company : companyList){
                    if(company.getCompanyEmailDomain() != null) {
                        if (company.getCompanyEmailDomain().equalsIgnoreCase(emailDomain)) {
                            matchingCompany = company;
                            break;
                        }
                    }
                }
                if(matchingCompany != null){
                    DataManager.getInstance().saveCompanyName(matchingCompany.getName());
                    companyNameCallback.onResponse(matchingCompany);
                }else{
                    companyNameCallback.onResponse(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
