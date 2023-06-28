package com.example.saveethaevents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;

import logic.Message;
import logic.adapter_fragment;

public class tabAct extends AppCompatActivity {
    TabLayout tl;
    ViewPager vp;
    adapter_fragment frag_adap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tabs);

        FirebaseApp.initializeApp(this);

        //tab layout
        tl=findViewById(R.id.tabLayout);
        vp=findViewById(R.id.viewPager);
        tl.addTab(tl.newTab().setText("Dashboard"));
        tl.addTab(tl.newTab().setText("All Events"));
        tl.setTabGravity(TabLayout.GRAVITY_FILL);
        frag_adap = new adapter_fragment(
                getSupportFragmentManager());
        vp.setAdapter(frag_adap);
        tl.setupWithViewPager(vp);


    }

    // related to menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu,menu);
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.logout:
                logoutByClearingPreferences(loginAct.class);
                break;
            case R.id.about:
                Log.i("*****","calling about()");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutByClearingPreferences(Class file){
        SharedPreferences sp=getSharedPreferences("R.string.app_data",MODE_PRIVATE);
        SharedPreferences.Editor edit=sp.edit();
        edit.clear();
        edit.apply();
        Intent intent=new Intent(getApplicationContext(), file);
        startActivity(intent);
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
