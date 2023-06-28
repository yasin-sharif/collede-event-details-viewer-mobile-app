package com.example.saveethaevents;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import logic.AddEvents;

public class addEvents1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private String type;
    private Spinner in_type;
    private Context context;
    private final String[] typeArray={"WS","AV","SM"};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events1);
        //FirebaseApp.initializeApp(this);
        Button back,save;
        context=getApplicationContext();
        in_type=findViewById(R.id.inp_type);
        back=findViewById(R.id.AE1_back);
        save=findViewById(R.id.AE1_save);

        Intent rx=getIntent();
        String[] args=rx.getStringArrayExtra("args");
        if(!(args==null)){
            AddEvents.setEventDetails(this,args,rx.getStringExtra("title"));
        }

        ArrayAdapter ad=new ArrayAdapter(this,android.R.layout.simple_spinner_item, typeArray);
        in_type.setOnItemSelectedListener(this);
        in_type.setAdapter(ad);

        back.setOnClickListener(view1 -> {
            Intent intent=new Intent(context,tabAct.class);
            startActivity(intent);
        });
        save.setOnClickListener(view2 -> AddEvents.getAndAddEvent(this,context,type,tabAct.class));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        type=parent.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
