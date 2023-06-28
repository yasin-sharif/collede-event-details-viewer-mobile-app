package com.example.saveethaevents;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import logic.CustomListAdapter;
import logic.ListFormat;
import logic.ListSetter;
import logic.Message;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboard #newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboard extends Fragment {

    FloatingActionButton fab,fab_reload;
    SharedPreferences sp;
    FragmentActivity fa;

    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dashboard.
     */
    /*
    // TODO: Rename and change types and number of parameters
    public static dashboard newInstance(String param1, String param2) {
        dashboard fragment = new dashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
    private DatabaseReference dr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
*/
        dr = FirebaseDatabase.getInstance("https://saveetha-events-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }


    // todo: add event action
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        fa=getActivity();
        fab=view.findViewById(R.id.fab);
        fab_reload=view.findViewById(R.id.fab_reload);
        Context context=getContext();
        sp=fa.getSharedPreferences("R.string.app_data",MODE_PRIVATE);
        if (sp.getString("desig","").equals("faculty")&&sp.getString("access","").equals("write")){
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(view1 -> {
                Intent intent=new Intent(context,addEvents1.class);
                startActivity(intent);
            });
        }
        else{
            fab.setVisibility(View.INVISIBLE);
        }




        ListView lv=fa.findViewById(R.id.listview_dash);
        assert fa != null;
        if(fa.findViewById(R.id.listview_dash) != null) {
            assert context != null;
            ArrayList<ListFormat> array;
            Log.i("``","calling setlist");

            ListSetter.setList(dr,sp.getString("dept",""),context,lv,fab_reload);
            fab_reload.setOnClickListener(view3 -> {
                ListSetter.setList(dr,sp.getString("dept",""),context,lv,fab_reload);
            });
            lv.setOnItemClickListener((adapterView, itemView, i, l) -> {
                // i = position (int)
                // l = id (long)
                Intent intent=new Intent(context, detailView.class);
                intent.putExtra("dept",sp.getString("dept",""));
                intent.putExtra("desig",sp.getString("desig",""));
                intent.putExtra("title",String.valueOf(((TextView)itemView.findViewById(R.id.title)).getText()));
                intent.putExtra("user",sp.getString("name",""));
                startActivity(intent);
            });
        }
        else{
            Log.i("~~~~","listview id is empty");
        }


    }

    


}