package com.mahmut.krankrent.ui;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.mahmut.krankrent.R;

import org.w3c.dom.Text;

import java.util.List;

public class CarList extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> maintitle;
    private final List<String> subtitle;
    private final List<Integer> cost;
    private final List<String> city;
    public CarList(Activity context, List<String> maintitle,List<String> subtitle, List<Integer> cost, List<String> city) {
        super(context, R.layout.car_list, maintitle);
        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.cost = cost;
        this.city = city;
    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.car_list, null,true);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView carCostText = (TextView)rowView.findViewById(R.id.carCost);
        TextView carCity = (TextView)rowView.findViewById(R.id.carCity);
        titleText.setText(maintitle.get(position));
        subtitleText.setText(subtitle.get(position));
        carCostText.setText(cost.get(position).toString());
        carCity.setText(city.get(position));
        return rowView;
    };
}
