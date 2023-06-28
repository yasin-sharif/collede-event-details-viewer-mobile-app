package com.example.saveethaevents;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import logic.EventFunctions;
import logic.Message;


public class detailView extends AppCompatActivity {

    DialogInterface.OnClickListener dialog;
    private static final int READ_CALENDAR=1;
    private static final int WRITE_CALENDAR=2;
    @Override
    public void onCreate(Bundle sis){
        super.onCreate(sis);
        setContentView(R.layout.activity_detailed_view);

        Intent intent=getIntent();
        String title=intent.getStringExtra("title"),dept=intent.getStringExtra("dept"),desig=intent.getStringExtra("desig"),
        user=intent.getStringExtra("user");
        Button edit=findViewById(R.id.dv_edit), delete=findViewById(R.id.dv_delete),
        register=findViewById(R.id.eventRegister),schedule=findViewById(R.id.ScheduleEvent);

        register.setVisibility(View.INVISIBLE);
        register.setEnabled(false);
        edit.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);
        schedule.setVisibility(View.INVISIBLE);

        String[] args=EventFunctions.getAndSetDetailView(title,dept,this,edit,delete,register,user);

        findViewById(R.id.dv_back).setOnClickListener(view -> {
            Intent intentback=new Intent(this,tabAct.class);
            startActivity(intentback);
        });

        findViewById(R.id.dv_edit).setOnClickListener(view -> {
            /*  args[0]=type, args[1]=date, args[2]=timeE,
                args[3]=timeS, args[4]=coord, args[5]=coordNum,
                args[6]=link, args[7]=loc, args[8]=addedby      */
            Intent intent2=new Intent(this,addEvents1.class);
            intent2.putExtra("args",args);
            intent2.putExtra("title",title);
            /*
            intent2.putExtra("type",args[0]);
            intent2.putExtra("date",args[1]);
            intent2.putExtra("timeE",args[2]);
            intent2.putExtra("timeS",args[3]);
            intent2.putExtra("coord",args[4]);
            intent2.putExtra("coordNum",args[5]);
            intent2.putExtra("link",args[6]);
            intent2.putExtra("loc",args[7]); */
            startActivity(intent2);
        });

        findViewById(R.id.dv_delete).setOnClickListener(view ->{

            dialog= (dialogInterface, i) -> {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    EventFunctions.deleteEvent(title, dept, getApplicationContext());
                } else {
                    dialogInterface.dismiss();
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure to delete this event ?")
                    .setPositiveButton("Yes", dialog)
                    .setNegativeButton("No", dialog);
            AlertDialog alert= builder.create();
            alert.setTitle("Confirmation");
            alert.show();

        });

        findViewById(R.id.ScheduleEvent).setOnClickListener(view -> {
            //int a=checkPermission(Manifest.permission.READ_CALENDAR,READ_CALENDAR);
            int b=checkPermission(Manifest.permission.WRITE_CALENDAR,WRITE_CALENDAR);

            if( b==1){
                EventFunctions.schedule(args,title,getApplicationContext());
            }
        });

    }
    private int checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(detailView.this, new String[] { permission }, requestCode);
            return 0;
        }
        else{
            return 1;
        }
    }

    // This function is called when user accept or decline the permission.
// Request Code is used to check which permission called this function.
// This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_CALENDAR) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Message.message("click schedule again to add remainder",getApplicationContext());

            }
            else {
                Message.message("cant add remainder without calendar permission",getApplicationContext());
            }
        }
    }
}
