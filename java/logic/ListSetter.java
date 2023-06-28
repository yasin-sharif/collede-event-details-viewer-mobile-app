package logic;
/*
* displaying events as list item in
* dashboard and all events tabsS
* */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.saveethaevents.detailView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class ListSetter {
      public static void setList(DatabaseReference dr, String dept, Context context, ListView lv, FloatingActionButton fab){
        // custom arraylist
        ArrayList<ListFormat> array= new ArrayList<>();
        // getting all the events
        dr.child("events").child(dept).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("*****", "Error getting data", task.getException());
            } else {
                // getting the result as datasnapshot
                DataSnapshot ds = task.getResult();

                // creating a iterable and iterator to iterate on the datasnapshot
                Iterable<DataSnapshot> iterable=ds.getChildren();
                Iterator<DataSnapshot> iter=iterable.iterator();

                fab.setVisibility(View.INVISIBLE);

                // getting the cleanup details of database
                // dr.child("date").get().addOnCompleteListener(task2 -> {
                dr.child("clean").child(dept).get().addOnCompleteListener(task2 -> {
                    String title,date,time_s,time_e,location,department,type;
                    DataSnapshot buffer;
                    DataSnapshot ds2= task2.getResult();
                    String dateClean=(String) ds2.getValue();
                    // when the date to clean DB is already passed
                    Log.i("~",dateClean);
                    Log.i("~",Boolean.toString(DateCheck.isDatePast(dateClean,"yyyy-MM-dd")));
                    Log.i("~",Boolean.toString(DateCheck.isDateToday(dateClean,"yyyy-MM-dd")));
                    Log.i("~",Boolean.toString(DateCheck.isDateFuture(dateClean,"yyyy-MM-dd")));
                    if(DateCheck.isDatePast(dateClean,"yyyy-MM-dd")&& !(DateCheck.isDateToday(dateClean,"yyyy-MM-dd") && !(DateCheck.isDateFuture(dateClean,"yyyy-MM-dd")))){
                        Log.i("~~","calling clean DB");
                        while(iter.hasNext()){
                            buffer=iter.next();
                            title=buffer.getKey();
                            date=(String)buffer.child("date").getValue();
                            // when the event is expired
                            if(DateCheck.isDatePast(date,"yyyy-MM-dd")){
                                assert title != null;
                                Log.i("deleting",title);
                                dr.child("events").child(dept).child(title).removeValue();
                            }
                            else {
                                location = (String) buffer.child("loc").getValue();
                                time_s = (String) buffer.child("timeS").getValue();
                                time_e = (String) buffer.child("timeE").getValue();
                                type = (String) buffer.child("type").getValue();
                                department = dept;
                                array.add(new ListFormat(title, date, time_s, time_e, location, department, type));
                            }
                        }
                        dr.child("clean").child(dept).setValue(LocalDate.now().toString());
                        CustomListAdapter adapter=new CustomListAdapter(context,array);
                        lv.setAdapter(adapter);
                        fab.setVisibility(View.VISIBLE);
                    }
                    // date to clean db is not yet passed
                    else{
                        while(iter.hasNext()){
                            buffer=iter.next();
                            title=buffer.getKey();
                            date=(String)buffer.child("date").getValue();
                            location=(String)buffer.child("loc").getValue();
                            time_s=(String)buffer.child("timeS").getValue();
                            time_e=(String)buffer.child("timeE").getValue();
                            type = (String) buffer.child("type").getValue();
                            department=dept;
                            array.add(new ListFormat(title,date,time_s,time_e,location,department,type));
                        }
                        CustomListAdapter adapter=new CustomListAdapter(context,array);
                        lv.setAdapter(adapter);
                        fab.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


    }

    public static void setListAll(DatabaseReference dr, Context context, ListView lv, SwipeRefreshLayout swiper) {
        // custom arraylist
        ArrayList<ListFormat> array = new ArrayList<>();
        // for getting the events details
        dr.child("events").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("*****", "Error getting data", task.getException());
            } else {
                // getting the result as datasnapshot
                DataSnapshot ds = task.getResult();

                // creating a iterable and iterator to iterate on the datasnapshot
                Iterable<DataSnapshot> iterable = ds.getChildren();
                Iterator<DataSnapshot> iter = iterable.iterator();

                DataSnapshot buffer;
                String dept;
                while (iter.hasNext()) {
                    buffer = iter.next();
                    dept = (String) buffer.getKey();

                    assert dept != null;
                    String finalDept = dept;
                    // for getting the event of each department
                    dr.child("events").child(dept).get().addOnCompleteListener(task2 -> {
                        if (!task2.isSuccessful()) {
                            Log.e("*****", "Error getting data", task2.getException());
                        } else {
                            // getting the result as datasnapshot
                            DataSnapshot ds2 = task2.getResult();

                            // creating a iterable and iterator to iterate on the datasnapshot
                            Iterable<DataSnapshot> iterable2 = ds2.getChildren();
                            Iterator<DataSnapshot> iter2 = iterable2.iterator();

                            String title, date, time_s, time_e, location, department,type;
                            DataSnapshot buffer2;
                            while (iter2.hasNext()) {
                                buffer2 = iter2.next();
                                title = buffer2.getKey();
                                date = (String) buffer2.child("date").getValue();
                                location = (String) buffer2.child("loc").getValue();
                                time_s = (String) buffer2.child("timeS").getValue();
                                time_e = (String) buffer2.child("timeE").getValue();
                                type = (String) buffer2.child("type").getValue();
                                department = finalDept;
                                array.add(new ListFormat(title, date, time_s, time_e, location, department,type));
                            }
                            CustomListAdapter adapter=new CustomListAdapter(context,array);
                            lv.setAdapter(adapter);
                            if(swiper!=null){
                                swiper.setRefreshing(false);
                            }
                        }
                    });
                }
            }
        });
    }

/*
if(DateCheck.isDatePast(dateClean,"yyyy-MM-dd")&& !(DateCheck.isDateToday(dateClean,"yyyy-MM-dd") && !(DateCheck.isDateFuture(dateClean,"yyyy-MM-dd")))){
                        Log.i("~~","calling clean DB");
                        while(iter.hasNext()){
                            buffer=iter.next();
                            title=buffer.getKey();
                            date=(String)buffer.child("date").getValue();
                            // when the event is expired
                            if(DateCheck.isDatePast(date,"yyyy-MM-dd")){
                                assert title != null;
                                Log.i("deleting",title);
                                dr.child("events").child(dept).child(title).removeValue();
                            }
                            else {
                                location = (String) buffer.child("loc").getValue();
                                time_s = (String) buffer.child("timeS").getValue();
                                time_e = (String) buffer.child("timeE").getValue();
                                type = (String) buffer.child("type").getValue();
                                department = dept;
                                array.add(new ListFormat(title, date, time_s, time_e, location, department, type));
                            }
                        }
                        dr.child("date").setValue(LocalDate.now().toString());
                        CustomListAdapter adapter=new CustomListAdapter(context,array);
                        lv.setAdapter(adapter);
                    }
                    // date to clean db is not yet passed
                    else{
                        while(iter.hasNext()){
                            buffer=iter.next();
                            title=buffer.getKey();
                            date=(String)buffer.child("date").getValue();
                            location=(String)buffer.child("loc").getValue();
                            time_s=(String)buffer.child("timeS").getValue();
                            time_e=(String)buffer.child("timeE").getValue();
                            type = (String) buffer.child("type").getValue();
                            department=dept;
                            array.add(new ListFormat(title,date,time_s,time_e,location,department,type));
                        }
* */

}
