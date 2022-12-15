package com.mahmut.krankrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.CollectionReference;
import com.mahmut.krankrent.ui.CarList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginAfterMain extends AppCompatActivity {
    private ListView lstAraclar;
    private TextView adVer,lblAracListesi;
    private DatabaseReference mReference;
    private DatabaseReference mReferenceCar;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar spinner;
    private ImageView anaSayfa,profileIcon,addCarIcon,arabaDuzenle,iconCikis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after_main);
        //ArrayList<String> araclarListesi = new ArrayList<>();
        //ArrayAdapter<String> araclarListesiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,araclarListesi);
        //lstAraclar.setAdapter(araclarListesiAdapter);
        lblAracListesi = (TextView)findViewById(R.id.lblAracListesi);
        anaSayfa = (ImageView)findViewById(R.id.iconMainPage);
        profileIcon = (ImageView)findViewById(R.id.profileIcon);
        addCarIcon = (ImageView)findViewById(R.id.addCarIcon);
        arabaDuzenle = (ImageView)findViewById(R.id.arabaDuzenle);
        iconCikis = (ImageView)findViewById(R.id.iconCikis);
        anaSayfa.setVisibility(View.INVISIBLE);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAfterMain.this, ProfilePage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        addCarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAfterMain.this, AddCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        arabaDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAfterMain.this, EditCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        iconCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(LoginAfterMain.this, LoginPage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        adVer = (TextView)findViewById(R.id.adVer);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        mReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(mUser.getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                spinner.setVisibility(View.GONE);
                String myString = snapshot.child("Adi").getValue().toString();
                String upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1).toLowerCase();
                adVer.setText(upperString);
                lblAracListesi.setText(snapshot.child("Sehir").getValue().toString() + " Konumundaki Araçların Listesi");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mReferenceCar = FirebaseDatabase.getInstance().getReference("araclar");
        /*String[] maintitle ={
                "Renault Clio","Ford Focus",
                "BMW 3 Serisi","Opel Astra",
                "Mercedes A180",
        };*/
        /*String[] subtitle ={
                "Mahmut Can","Ahmet Can",
                "Fake","Osuruk",
                "Sekssu",
        };*/
        /*String[] cost = {
                "4500","6590",
                "7124","4900",
                "6680"
        };*/
        List<String> maintitle = new ArrayList<>();
        List<String> subtitle = new ArrayList<>();
        List<String> cost = new ArrayList<>();
        mReferenceCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    maintitle.add(snp.child("Marka").getValue().toString()+" "+snp.child("Model").getValue().toString()+" "+snp.child("ModelYili")
                            .getValue().toString());
                    subtitle.add(snp.child("Konum").getValue().toString()+"/ İlan Sahibi"+snp.child("Paylasan").getValue().toString());
                    cost.add(snp.child("KiraBedeli").getValue().toString());
                    CarList adapter=new CarList(LoginAfterMain.this, maintitle, subtitle,cost);
                    lstAraclar = (ListView)findViewById(R.id.LstAraclar);
                    lstAraclar.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
    public void cikisYap(View v){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginPage.class));
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, LoginPage.class));
        }
    }
}