package logic;
/*
* taost creaters
* */


import android.content.Context;
import android.widget.Toast;

public class Message {
    public static void message(String text, Context context){
        Toast t=Toast.makeText(context, text, Toast.LENGTH_LONG);
        t.show();
    }
}
