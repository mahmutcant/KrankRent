package com.mahmut.krankrent.ui;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mahmut.krankrent.R;
public class CarList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] maintitle;
    private final String[] subtitle;
    private final String[] cost;
    public CarList(Activity context, String[] maintitle,String[] subtitle, String[] cost) {
        super(context, R.layout.car_list, maintitle);
        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.cost = cost;
    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.car_list, null,true);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView carCostText = (TextView)rowView.findViewById(R.id.carCost);
        titleText.setText(maintitle[position]);
        subtitleText.setText(subtitle[position]);
        carCostText.setText(cost[position]);
        return rowView;
    };
}
