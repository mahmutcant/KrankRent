package com.mahmut.krankrent.ui;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.mahmut.krankrent.R;

import java.util.List;

public class MyCarList extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> maintitle;
    public MyCarList(Activity context, List<String> maintitle) {
        super(context, R.layout.activity_my_car_list, maintitle);
        this.maintitle = maintitle;
        this.context = context;

    }
}