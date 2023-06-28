package logic;
/*
* custom list adapter
* */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.saveethaevents.R;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<ListFormat> {
    // invoke the suitable constructor of the ArrayAdapter class
    public CustomListAdapter(@NonNull Context context, ArrayList<ListFormat> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_format, parent, false);
        }

        ListFormat currentNumberPosition = getItem(position);

        assert currentNumberPosition != null;

        TextView textView1 = currentItemView.findViewById(R.id.title);
        textView1.setText(currentNumberPosition.getTitle());

        TextView textView2 = currentItemView.findViewById(R.id.date);
        textView2.setText(currentNumberPosition.getDate());

        TextView textView3 = currentItemView.findViewById(R.id.time);
        textView3.setText(currentNumberPosition.getTime());

        TextView textView4 = currentItemView.findViewById(R.id.location);
        textView4.setText(currentNumberPosition.getLoc());

        TextView textView5 = currentItemView.findViewById(R.id.department);
        textView5.setText(currentNumberPosition.getDept());

        TextView textView6 = currentItemView.findViewById(R.id.type);
        textView6.setText(currentNumberPosition.getType());
        // then return the recyclable view
        return currentItemView;
    }
}
