package com.example.saveethaevents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import logic.Message;

public class signupAct extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] course={"cse","ece","it"};
    String dept,name,pass,cpass,uid,access="read";
    EditText password, cPassword, namein, unique;
    CheckBox write;
    Context context;

    private DatabaseReference dr;
    private int status=0; // 0=failure, 1=success

    //todo: create a MVC architect in
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_signup);
        Spinner spin=findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter ad=new ArrayAdapter(this,android.R.layout.simple_spinner_item,course);
        spin.setAdapter(ad);
        password=findViewById(R.id.in_pw);
        cPassword=findViewById(R.id.in_cpw);
        namein=findViewById(R.id.in_name);
        unique=findViewById(R.id.in_uid);
        write=findViewById(R.id.checkBox);
        context=getApplicationContext();
        dr= FirebaseDatabase.getInstance("https://saveetha-events-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        dept=parent.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }

    public void call_login(View view){
        Intent launch=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(launch);
    }

    public void call_register(View view){
        Button button=findViewById(R.id.b_regis);


        // getting the values
        name=namein.getText().toString();
        pass=password.getText().toString();
        cpass=cPassword.getText().toString();
        uid=unique.getText().toString();

        // checking the presence of all required field
        if(!name.equals("") && !pass.equals("") && !cpass.equals("") && !uid.equals("")){
            String desig="student";

            // ensuring password
            if(pass.equals(cpass)){

                // detecting the user as faculty to give write access
                if((int)uid.charAt(0)>96 && (int)uid.charAt(0)<123){
                    desig="faculty";
                    access=(write.isChecked()? (search_user()==1? "write":"read"):"read");
                }

                // writing to data base
                button.setEnabled(false);
                write_db(dr,desig,pass,dept,access,name,uid);

            }
            else{
                Message.message("confirm password does not match",getApplicationContext());
            }
        }
        else{

            Message.message("fill all the given fields",getApplicationContext());
        }

    }

    public int search_user(){
        // needs to be a authenticate faculty
        //todo INFUT insert a function to check the existence of a faculty in college db
        return 1;
    }

    // writing to a database
    public void write_db(@NonNull DatabaseReference drin, String desig, String pass, String dept, String access, String name, String uid){
        //todo INFUT check for the existence of an user
        User node=new User(pass,dept,access,name);
        drin.child(desig).child(uid).setValue(node).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Message.message("successfully registered",getApplicationContext());
                    Intent intent=new Intent(context,loginAct.class);
                    startActivity(intent);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Message.message("register failed",getApplicationContext());
                }
            });


    }

    @Override
    public void onBackPressed() {
            Message.message("back press disabled",getApplicationContext());
    }

}
