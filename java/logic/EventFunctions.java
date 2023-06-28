package logic;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.saveethaevents.R;
import com.example.saveethaevents.tabAct;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;


public class EventFunctions {
    static private DatabaseReference dr;
    @SuppressLint("SetTextI18n")
    public static String[] getAndSetDetailView(String title, String dept, Activity act, Button edit, Button delete,Button register, String user){
        dr = FirebaseDatabase.getInstance("https://saveetha-events-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();


        String[] args =new String[9];
        dr.child("events").child(dept).child(title).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("*****", "Error getting data", task.getException());
            } else {
                // getting the result as datasnapshot
                DataSnapshot ds = task.getResult();
                args[0]=(String)ds.child("type").getValue();
                args[1]=(String)ds.child("date").getValue();
                args[2]=(String)ds.child("timeE").getValue();
                args[3]=(String)ds.child("timeS").getValue();
                args[4]=(String)ds.child("coord").getValue();
                args[5]=(String)ds.child("coordNum").getValue();
                args[6]=(String)ds.child("link").getValue();
                args[7]=(String)ds.child("loc").getValue();
                args[8]=(String)ds.child("addedBy").getValue();
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.dis_title)).setText(title);
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.dis_type)).setText(args[0]);
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.dis_date)).setText(args[1]);
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.inp_timeS)).setText("time start: "+args[3]);
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.dis_timeE)).setText("time end: "+args[2]);
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.dis_loc)).setText("location: "+args[7]);
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.dis_coord)).setText("coordinator name: "+args[4]);
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.dis_coordNum)).setText("coordinator number: "+args[5]);
                ((TextView)act.findViewById(com.example.saveethaevents.R.id.dis_addedBy)).setText("added by: "+args[8]);

                assert args[8] != null;
                edit.setVisibility(args[8].equals(user)? View.VISIBLE:View.INVISIBLE);
                delete.setVisibility(args[8].equals(user)? View.VISIBLE:View.INVISIBLE);
                act.findViewById(R.id.ScheduleEvent).setVisibility(View.VISIBLE);

                Log.i("link",args[6]);

                assert args[6] != null;
                if(args[6].equals("null")){
                    register.setVisibility(View.VISIBLE);
                    register.setText("No link provided");
                    register.setEnabled(false);
                }
                else{
                    register.setVisibility(View.VISIBLE);
                    register.setEnabled(true);
                    register.setOnClickListener(view -> {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(args[6]));
                        act.startActivity(intent);
                    });
                }

            }
        });
        return args;
    }

    public static void deleteEvent(String title, String dept,Context context){
        dr = FirebaseDatabase.getInstance("https://saveetha-events-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        Log.i("ack","calling the delete query");
        dr.child("events").child(dept).child(title).removeValue().addOnCompleteListener(avoid-> {
                    Message.message("event removed successfully", context);
                    Intent intent=new Intent(context, tabAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
        ).addOnFailureListener(avoid->
            Message.message("unable to remove event",context)
        );
    }

    public static void schedule(String[] args,String title,Context context){
        /*  args[0]=type, args[1]=date, args[2]=timeE,
                args[3]=timeS, args[4]=coord, args[5]=coordNum,
                args[6]=link, args[7]=loc, args[8]=addedby      */

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DTSTART, convertToEpoch(args[3],args[1]));
        values.put(CalendarContract.Events.DTEND,convertToEpoch(args[2],args[1]));
        values.put(CalendarContract.Events.DESCRIPTION, "event added");
        //todo: add events with various timezone
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        //values.put(CalendarContract.Events.DURATION, "+timezoneP1H");
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventId=Long.parseLong(uri.getLastPathSegment());
        // 3 hours
        ContentValues reminder = new ContentValues();
        reminder.put(CalendarContract.Reminders.EVENT_ID, eventId);
        reminder.put(CalendarContract.Reminders.MINUTES, 180);
        reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        context.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);
        // 1 hour
        ContentValues reminder2 = new ContentValues();
        reminder2.put(CalendarContract.Reminders.EVENT_ID, eventId);
        reminder2.put(CalendarContract.Reminders.MINUTES, 60);
        reminder2.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        context.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder2);
        // 15 min
        ContentValues reminder3 = new ContentValues();
        reminder3.put(CalendarContract.Reminders.EVENT_ID, eventId);
        reminder3.put(CalendarContract.Reminders.MINUTES, 15);
        reminder3.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        context.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder3);
        Message.message("reminder addded",context);



        /*
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", convertToEpoch(args[3],args[1]));
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "A Test Event from android app");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

         */
    }

    private static long convertToEpoch(String time,String date) {
        // time format hour:minute meridian
        String buffer = "", format = "T";

        int hour = -1, min = -1, meridian = -1, flag = 0, error = -1;
        // extractor
        // 0 = hour extraction
        // 1 = min extraction
        // 2 = meridian extraction
        char[] array = time.toCharArray();
        for (char i : array) {
            if (i == ':') {
                hour = Integer.parseInt(buffer);
                flag = 1;
                buffer = "";
            } else if (i == ' ') {
                min = Integer.parseInt(buffer);
                flag = 2;
                buffer = "";
            } else if (flag == 2) {
                if (i == 'a' || i == 'A') {
                    meridian = 0;
                    break;
                } else if ((i == 'p' || i == 'P')&&hour!=12) {
                    meridian = 12;
                    break;
                }
                else{
                    meridian=0;
                }
            } else {
                buffer += i;
            }
        }
        // error checking
        {
            error = ((hour > 0 && hour < 13) && (min > -1 && min < 60) && !(meridian == -1)) ? 0 : 1;
            if (error == 1) {
                System.out.println("error in time format");
                return -999;
            } else {
                /* format producer */
                String h,m;
                // convert to string

                //Log.i("time-min",String.valueOf(min));
                //Log.i("time-hour",String.valueOf(hour));
                hour+=meridian;
                m=(min<10)? ((min==0)? "00":"0"+min):""+min;
                h=(hour<10)?"0"+hour:""+hour;
                format = date + format + ((h) + ":" + m + ":" + "00");
                Log.i("format",format);
            }
        }
        // eppoch generator
        {
            LocalDateTime localDateTime = LocalDateTime.parse(format);
            Instant instant = Instant.now(); //can be LocalDateTime
            ZoneId systemZone = ZoneId.systemDefault(); // my timezone
            ZoneOffset offset = systemZone.getRules().getOffset(instant);
            long timeInSeconds = localDateTime.toEpochSecond(offset);
            return timeInSeconds*1000;
        }
    }



}
