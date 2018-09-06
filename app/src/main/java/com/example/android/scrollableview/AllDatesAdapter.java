package com.example.android.scrollableview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.scrollableview.Classes.MyDate;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by chandan on 08-03-2017.
 */

public class AllDatesAdapter extends ArrayAdapter<MyDate> {

    Activity mActivity;

    public AllDatesAdapter(Activity activity, ArrayList<MyDate> arrayListDates) {
        super(activity, 0, arrayListDates);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final MyDate date = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_single_date, parent, false);
        }


        String fullDate =  date.getDate()+"/"+ date.getMonth() +"/"+ date.getYear();
        TextView textview = (TextView) convertView.findViewById(R.id.text_view_date_details);

        textview.setText(fullDate);

        return convertView;
    }


}

