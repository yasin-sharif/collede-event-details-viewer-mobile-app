package logic;
/*
* various login checking functions
* */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class Check {

    public static boolean checkLoginUsingPreferences(Context context){
        SharedPreferences sp=context.getSharedPreferences("R.string.app_data",Context.MODE_PRIVATE);
        // check app data exist
        boolean c1=sp.contains("id");
        if(c1){
            // if exist
            return sp.getBoolean("status",false);
        }
        else{
            // create a shared preference
            SharedPreferences.Editor edit=sp.edit();
            edit.putString("id","");
            edit.putBoolean("status",false);
            edit.putString("dept","");
            edit.putString("desig","");
            edit.putString("name","");
            edit.putString("access","read");
            edit.apply();
            return false;
        }
    }

    public static void checkLoginUsingFirebase(String user, String pass, Button button, Context context, Class page) {
        /*
         * 5 = login details are correct
         * 9 = login details are not correct
         * 17 = fields are empty
         * */
        // checking the presence of all required field
        if (!user.equals("") && !pass.equals("")) {
            // generel designation
            String desig = "student";

            // detecting the user as faculty or student based on the starting characters of user id
            if ((int) user.charAt(0) > 96 && (int) user.charAt(0) < 123) {
                desig = "faculty";
            }

            DatabaseReference dr = FirebaseDatabase.getInstance("https://saveetha-events-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
            //
            button.setEnabled(false);

            // getting the password from datasnapshot
            String finalDesig = desig;
            dr.child(desig).child(user).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("*****", "Error getting data", task.getException());
                    } else {
                        DataSnapshot ds = task.getResult();
                        String passFromCloud = (String) ds.child("pass").getValue();

                        //Log.i("*****", passFromCloud);
                        if (pass.equals(passFromCloud)) {
                            Message.message("login successfull", context);
                            button.setEnabled(false);
                            SharedPreferences sp = context.getSharedPreferences("R.string.app_data", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = sp.edit();
                            edit.putString("id", user);
                            edit.putString("dept", (String) ds.child("dept").getValue());
                            edit.putString("desig", finalDesig);
                            edit.putString("name", (String) ds.child("name").getValue());
                            edit.putString("access", (String) ds.child("access").getValue());
                            edit.putBoolean("status", true);
                            edit.apply();
                            Intent act = new Intent(context, page);
                            act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(act);
                        } else {
                            Message.message("wrong username or password", context);
                            button.setEnabled(true);

                        }
                    }
                }
            });
        } else {
            Message.message("enter all required fields", context);
        }
    }
}
/*
    public void registerUserInFirebase(){
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

                //checking the success of wi=rite to db
                if(status==1){
                    Intent launch=new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(launch);
                }
                else{
                    button.setEnabled(true);
                }

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
        drin.child(desig).child(uid).setValue(node)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Message.message("successfully registered",getApplicationContext());
                        status=1;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Message.message("register failed",getApplicationContext());
                        status=0;
                    }
                });
    }
}
*/