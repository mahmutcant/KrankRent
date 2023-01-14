package com.mahmut.krankrent.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mahmut.krankrent.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CarList extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> maintitle;
    private final List<String> subtitle;
    private final List<Integer> cost;
    private final List<String> city;
    private final List<String> kiraliMi;
    private final List<String> telNo;
    private final List<String> eskiFiyat;
    private final List<String> paylasanUid;
    private final List<String> aracKey;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference,mReferenceCar,mReferenceLogo,mReferenceFavori;
    private HashMap<String, Object> mData;
    public CarList(Activity context, List<String> maintitle,List<String> subtitle, List<Integer> cost, List<String> city,List<String> kiraliMi,List<String> telNo,List<String> eskiFiyat,List<String> paylasanUid,List<String> aracKey) {
        super(context, R.layout.car_list, maintitle);
        this.context=context;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.cost = cost;
        this.city = city;
        this.kiraliMi = kiraliMi;
        this.telNo = telNo;
        this.eskiFiyat = eskiFiyat;
        this.paylasanUid = paylasanUid;
        this.aracKey = aracKey;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference("kullanicilar");
        mReferenceCar = FirebaseDatabase.getInstance().getReference("araclar");
        mReferenceLogo = FirebaseDatabase.getInstance().getReference("Logo");
    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.car_list, null,true);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView carCostText = (TextView)rowView.findViewById(R.id.carCost);
        TextView carCity = (TextView)rowView.findViewById(R.id.carCity);
        ImageView btnFavori = (ImageView) rowView.findViewById(R.id.btnFavori);
        LinearLayout aracListesi = (LinearLayout)rowView.findViewById(R.id.aracListesi);
        ImageView markaLogo = (ImageView)rowView.findViewById(R.id.MarkaLogo);
        ImageView costStatus = (ImageView)rowView.findViewById(R.id.costStatus);
        TextView txtIletisim = (TextView)rowView.findViewById(R.id.txtIletisim);
        markaLogo.setImageResource(R.drawable.ic_baseline_watch_later_24);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mReferenceLogo.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String link = snapshot.getValue(String.class);
                        Picasso.get().load(link).into(markaLogo);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        }, position*1000);
        aracListesi.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(kiraliMi.get(position) == "false"){
                    String phone = "+90 "+ telNo.get(position);
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    context.startActivity(intent);
                    return false;
                }
                else {
                    Toast.makeText(context, "Bu araç Şuanda Kiralı Lütfen Kiralanma Süresi Bitince Tekrar Deneyin! ", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
        btnFavori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            Log.w("","Fail",task.getException());
                            return;
                        }
                        String token = task.getResult();
                        mReferenceCar.child(city.get(position)).child(paylasanUid.get(position)).child(aracKey.get(position)).child("takibeAlanlar")
                                .child(mUser.getUid()).setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context,"Favori Listesine Eklendi", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        mReference.child(mUser.getUid()).child("takipListesi").child(aracKey.get(position)).setValue(aracKey.get(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "Araç Favorilerinize Eklendi", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        if(kiraliMi.get(position) == "true"){
            aracListesi.setBackgroundColor(Color.parseColor("#cccccc"));
            titleText.setTextColor(Color.parseColor("#202c56"));
        }else {
            aracListesi.setBackgroundColor(Color.parseColor("#edf0f7"));
        }
        titleText.setText(maintitle.get(position));
        subtitleText.setText(subtitle.get(position));
        carCostText.setText(cost.get(position).toString());
        carCity.setText(city.get(position));
        txtIletisim.setText("+90 "+telNo.get(position));
        if(eskiFiyat.get(position).length() <= 1){
            costStatus.setImageResource(R.drawable.ic_baseline_horizontal_rule_24);
        }
        else if(Integer.parseInt(eskiFiyat.get(position)) > Integer.parseInt(cost.get(position).toString())){
            costStatus.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        }
        else if(Integer.parseInt(eskiFiyat.get(position)) < Integer.parseInt(cost.get(position).toString())) {
            costStatus.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
        }
        else if(Integer.parseInt(eskiFiyat.get(position)) == Integer.parseInt(cost.get(position).toString())){
            costStatus.setImageResource(R.drawable.ic_baseline_horizontal_rule_24);
        }
        return rowView;
    };
}
