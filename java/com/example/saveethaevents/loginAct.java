package com.example.saveethaevents;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import logic.Check;
import logic.Message;

public class loginAct extends AppCompatActivity {
    String user,pass;
    EditText uidin,passin;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        uidin=findViewById(R.id.uid);
        passin=findViewById(R.id.pw);
        context=getApplicationContext();
    }

    public void call_signup(View view){
        Intent act=new Intent(context, signupAct.class);
        startActivity(act);
    }

    public void call_login(View view){
        // getting the values
        user=uidin.getText().toString();
        pass=passin.getText().toString();
        Button button=findViewById(R.id.b_sub);
        Check.checkLoginUsingFirebase(user,pass,button,context,tabAct.class);
    }

    private long pressedTime=0;
    // for exiting on back press
    @Override
    public void onBackPressed() {
        if (pressedTime + 2500 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
            finish();
        } else {
            Message.message("press back again to exit app",getApplicationContext());
        }
        pressedTime = System.currentTimeMillis();
    }
}
