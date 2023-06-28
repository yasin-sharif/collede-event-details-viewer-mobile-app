package com.example.saveethaevents;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import logic.CustomListAdapter;
import logic.ListFormat;
import logic.ListSetter;
import logic.Message;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link all_events #newInstance} factory method to
 * create an instance of this fragment.
 */
public class all_events extends Fragment {

    /*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public all_events() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment all_events.
     */
    /*
    // TODO: Rename and change types and number of parameters
    public static all_events newInstance(String param1, String param2) {
        all_events fragment = new all_events();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

         */

    }
    DatabaseReference dr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dr = FirebaseDatabase.getInstance("https://saveetha-events-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        return inflater.inflate(R.layout.fragment_all_events, container, false);
    }

    //todo: edit the function

    FragmentActivity fa;
    SwipeRefreshLayout swiper;
    SharedPreferences sp;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        fa=getActivity();
        Context context=getContext();
        sp=fa.getSharedPreferences("R.string.app_data",MODE_PRIVATE);

        ListView lv=fa.findViewById(R.id.listview_all);
        swiper=fa.findViewById(R.id.swiperAllEvents);
        assert fa != null;
        if(fa.findViewById(R.id.listview_all) != null) {
            assert context != null;
            ListSetter.setListAll(dr,context,lv,swiper);
            lv.setOnItemClickListener((adapterView, itemView, i, l) -> {
                // i = position (int)
                // l = id (long)
                Intent intent=new Intent(context, detailView.class);
                intent.putExtra("dept",String.valueOf(((TextView)itemView.findViewById(R.id.department)).getText()));
                intent.putExtra("desig",sp.getString("desig",""));
                intent.putExtra("title",String.valueOf(((TextView)itemView.findViewById(R.id.title)).getText()));
                intent.putExtra("user",sp.getString("name",""));
                startActivity(intent);
            });
        }
        else{
            Log.i("~~~~","listview id is empty");
        }

        swiper.setOnRefreshListener(
                () -> {
                    assert fa != null;
                    if(fa.findViewById(R.id.listview_all) != null) {
                        assert context != null;
                        ListSetter.setListAll(dr,context,lv,swiper);
                    }
                    else{
                        Log.i("~~~~","listview id is empty");
                    }
                }
        );
    }


}