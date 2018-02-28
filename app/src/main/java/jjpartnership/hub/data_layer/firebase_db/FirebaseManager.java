package jjpartnership.hub.data_layer.firebase_db;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import jjpartnership.hub.data_layer.DataManager;
import jjpartnership.hub.data_layer.data_models.Company;
import jjpartnership.hub.data_layer.data_models.CompanyType;
import jjpartnership.hub.data_layer.data_models.User;
import jjpartnership.hub.data_layer.data_models.CompanyRealm;
import jjpartnership.hub.data_layer.data_models.EmployeeRealm;
import jjpartnership.hub.data_layer.data_models.UserRealm;
import jjpartnership.hub.utils.UserPreferences;

import static android.content.ContentValues.TAG;

/**
 * Created by jbrannen on 2/24/18.
 */

public class FirebaseManager {
    private DatabaseReference userReference;
    private DatabaseReference companiesReference;
    private FirebaseDatabase database;

    public FirebaseManager() {
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("users").child(UserPreferences.getInstance().getUID());
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

    public void writeNewUser(UserRealm user) {
        database.getReference("users").push().setValue(user);
    }

    //setting updatedUser will replace the user currently in the database.
    public void updateUser(UserRealm updatedUser){
        database.getReference("users").child(updatedUser.getUid()).setValue(updatedUser);
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
        comp3Employees.add(new EmployeeRealm("hint", "trup", "htrup@gmail.com"));
        comp3Employees.add(new EmployeeRealm("lint", "tnil", "ltnil@gmail.com"));
        comp3Employees.add(new EmployeeRealm("butch", "er", "ber@gmail.com"));
        CompanyRealm comp3 = new CompanyRealm("Staples", "Riverside CA", CompanyType.CUSTOMER_COMPANY, comp3Employees);

        RealmList<EmployeeRealm> comp4Employees = new RealmList<>();
        comp4Employees.add(new EmployeeRealm("John", "Childers", "jchilders@gmail.com"));
        comp4Employees.add(new EmployeeRealm("Jonathan", "Brannen", "jbinvestments15@gmail.com"));
        comp4Employees.add(new EmployeeRealm("Shawna", "Brannen", "shawnaMccollom@yahoo.com"));
        CompanyRealm comp4 = new CompanyRealm("Thermo Fisher", "Los Angeles CA", CompanyType.SALES_COMPANY, comp4Employees);

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
}
