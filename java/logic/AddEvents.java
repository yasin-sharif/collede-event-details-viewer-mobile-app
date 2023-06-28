package logic;
/*
* for adding events into firebase
* */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.saveethaevents.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddEvents {
    private static Class page;

    public static void getAndAddEvent(Activity act, Context context, String type, Class pagein){
        Button save=act.findViewById(R.id.AE1_save);
        save.setEnabled(false);

        String title,date,timeS,timeE,loc,link,coord,coordNum,dept,addedBy;
        EditText in_title,in_date,in_timeS,in_timeE,in_loc,in_link,in_coord,in_coordNum;

        in_title=act.findViewById(R.id.inp_title);
        in_date=act.findViewById(R.id.inp_date);
        in_timeS=act.findViewById(R.id.inp_timeS);
        in_timeE=act.findViewById(R.id.inp_timeE);
        in_loc=act.findViewById(R.id.inp_loc);
        in_link=act.findViewById(R.id.inp_link);
        in_coord=act.findViewById(R.id.inp_coord);
        in_coordNum=act.findViewById(R.id.inp_coordNum);

        title=in_title.getText().toString();
        date=in_date.getText().toString();
        timeS=in_timeS.getText().toString();
        timeE=in_timeE.getText().toString();
        loc=in_loc.getText().toString();
        if((in_link.getText().toString()).equals("")){
            link="null";
        }
        else{
            link=in_link.getText().toString();
        }
        coord=in_coord.getText().toString();
        coordNum=in_coordNum.getText().toString();

        // checking the presence of all required field
        if(!title.equals("") && !date.equals("") && !timeS.equals("") && !timeE.equals("") && !loc.equals("") && !coord.equals("") && !type.equals("")){
            SharedPreferences sp=context.getSharedPreferences("R.string.app_data",Context.MODE_PRIVATE);
            dept=sp.getString("dept","");
            addedBy=sp.getString("name","");
            Event node=new Event(type,date,timeS,timeE,coord,coordNum,link,loc,addedBy);
            page=pagein;
            writeToDB(node,"events",title,dept,context);
        }
        else{
            Message.message("except link all fields are important",context);
        }
        save.setEnabled(true);
    }

    public static void setEventDetails(Activity act, String[] args,String title){
        /*  args[0]=type, args[1]=date, args[2]=timeE,
                args[3]=timeS, args[4]=coord, args[5]=coordNum,
                args[6]=link, args[7]=loc, args[8]=addedby      */

        ((TextView)act.findViewById(R.id.inp_title)).setText(title);
        ((TextView)act.findViewById(R.id.inp_date)).setText(args[1]);
        ((TextView)act.findViewById(R.id.inp_timeE)).setText(args[2]);
        ((TextView)act.findViewById(R.id.inp_timeS)).setText(args[3]);
        ((TextView)act.findViewById(R.id.inp_loc)).setText(args[7]);
        ((TextView)act.findViewById(R.id.inp_link)).setText(args[6]);
        ((TextView)act.findViewById(R.id.inp_coord)).setText(args[4]);
        ((TextView)act.findViewById(R.id.inp_coordNum)).setText(args[5]);
        //todo: unable to set spinner value

    }

    private static void writeToDB(Event node,String branch,String parent,String dept,Context context){
        DatabaseReference dr= FirebaseDatabase.getInstance("https://saveetha-events-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        // for adding events
        dr.child(branch).child(dept).child(parent).setValue(node).addOnSuccessListener(aVoid -> {
            Message.message("successfully added Events",context);
            Intent intent= new Intent(context,page);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }).addOnFailureListener(e -> {
            Message.message("adding failed",context);
        });
    }
}
