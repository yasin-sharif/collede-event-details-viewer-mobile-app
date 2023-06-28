package com.example.saveethaevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import logic.Check;

public class MainActivity extends AppCompatActivity {
    /*
     * DATABASE LIMITS
     * STORAGE : 1GB TOTAL
     * DOWNLOADS : 10GB/MONTH
     * USAGE REFRESH AT start of every month
     * */
    /*
     * login and signup were successfully working
     * 1 connection was active
     * 22.34kb downloaded
     * load <1%
     * */
    /*
    * last successfull compilation on 26.10.2022
    * deleting past activity from dashboard for their department alone is working fine
    * */

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_check);
        context=getApplicationContext();

        // checking already logined
        boolean log_status= Check.checkLoginUsingPreferences(context);
        if(log_status){
            // logged in
            //setContentView(R.layout.act_tabs);
            launch(context,tabAct.class);
        }
        else{
            // not logged in
            //setContentView(R.layout.activity_login);
            launch(context,loginAct.class);
        }
    }

    private void launch(Context context, Class page){
        Intent act=new Intent(context, page);
        startActivity(act);
    }
}