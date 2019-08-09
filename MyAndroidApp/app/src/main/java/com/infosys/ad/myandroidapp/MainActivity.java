package com.infosys.ad.myandroidapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private boolean bottomNavToggle=false;
   DatabaseHelper myDb;
    private EditText reg_passcode,reg_email,reg_pass,reg_phone,reg_hint,login_passcode;
    TextView dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Loading default navigation
        loadFragment(new Registration());


        //reg_msg= (TextView) findViewById(R.id.reg_msg);


        //Bottom Navigtion Load
        BottomNaSetter();


        //Variables initializaion



    }

    public void BottomNaSetter(){

        if(!this.bottomNavToggle) {
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
            BottomNavigationView navigation2 = (BottomNavigationView) findViewById(R.id.navigation2);
            navigation2.setVisibility(View.INVISIBLE);
            navigation.setVisibility(View.VISIBLE);
            navigation.setOnNavigationItemSelectedListener(this);

        }else{
            BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation2);
            BottomNavigationView navigation2 = (BottomNavigationView) findViewById(R.id.navigation);
            navigation2.setVisibility(View.INVISIBLE);

            navigation.setVisibility(View.VISIBLE);
            loadFragment(new Dashboard());
            navigation.setOnNavigationItemSelectedListener(this);
        }
    }


    //switching fragment
    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_registration:
                fragment = new Registration();
                break;

            case R.id.navigation_login:
                fragment = new Login();
                break;

            case R.id.navigation_logoff: {
                this.bottomNavToggle=false;
                fragment = new Login();
                BottomNaSetter();

            }
                break;

            case R.id.navigation_dashboard:

                fragment = new Dashboard();

                break;



        }

        return loadFragment(fragment);
    }
    public void loginClicked(View view){
       // Toast.makeText(getApplicationContext(),"Hello larry",Toast.LENGTH_SHORT).show();
        showData();
        this.bottomNavToggle=true;
        BottomNaSetter();

    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    public void RegistrationClick(View view){

        Registration RegFra= (Registration)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        View v = RegFra.getView();

        reg_passcode = (EditText) v.findViewById(R.id.reg_passcode);
        reg_email = (EditText) v.findViewById(R.id.reg_email);
        reg_pass = (EditText)v.findViewById(R.id.reg_pass);
        reg_hint = (EditText)v.findViewById(R.id.reg_hint);
        reg_phone = (EditText)v.findViewById(R.id.reg_phone);
        login_passcode = (EditText)v.findViewById(R.id.login_passcode);

        if( TextUtils.isEmpty(this.reg_email.getText()) || TextUtils.isEmpty(this.reg_pass.getText()) || TextUtils.isEmpty(this.reg_passcode.getText()) || TextUtils.isEmpty(this.reg_phone.getText()) || TextUtils.isEmpty(this.reg_hint.getText())){
            this.showMessage("Error","Fields are empty!");


        }else  if(this.reg_passcode.getText().toString().length()==6){
            this.myDb = new DatabaseHelper(this);
            if(myDb.insertData(Integer.parseInt(this.reg_passcode.getText().toString()),this.reg_email.getText().toString(),this.reg_pass.getText().toString(),this.reg_hint.getText().toString(),this.reg_phone.getText().toString()))
            this.showMessage("Success","Registration successful!");
            else
                this.showMessage("Error","DB ERROR!");

        }else{
            this.showMessage("Error","Passcode must be 6  digit long!");
        }
    }


    public void showData() {
//        Registration RegFra= (Registration)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        View v = RegFra.getView();
//        this.dashboard = (TextView)v.findViewById(R.id.dash);



        Cursor res = new DatabaseHelper(this).getAllData();
        if (res.getCount() == 0) {
            // show message
            showMessage("Error", "Nothing found");
            return;
        }else{
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {

                buffer.append("Email :"+ res.getString(1)+"\n");
                buffer.append("Hint :"+ res.getString(3)+"\n");
                buffer.append("Phone Number :"+ res.getString(4)+"\n\n");
            }

            // Show all data
           // this.dashboard.setText(buffer.toString());
            showMessage("Data",buffer.toString());
        }

    }

}
