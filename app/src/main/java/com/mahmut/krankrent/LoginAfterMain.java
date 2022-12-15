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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    private DatabaseReference mReference,mReferenceCar,mReferenceCity;
    private Button btnFiltrele;
    private Spinner lstSehirFiltre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar spinner;
    private ImageView anaSayfa,profileIcon,addCarIcon,arabaDuzenle,iconCikis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after_main);
        lblAracListesi = (TextView)findViewById(R.id.lblAracListesi);
        lstSehirFiltre = (Spinner)findViewById(R.id.lstSehirFiltre);
        btnFiltrele = (Button)findViewById(R.id.btnSehirFiltre);
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.my_selected_item,arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        mReferenceCity = FirebaseDatabase.getInstance().getReference("Sehir");
        mReferenceCity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.add("Tümü");
                for(DataSnapshot snp: snapshot.getChildren()){
                    arrayList.add(snp.getValue().toString());
                }
                Spinner s = (Spinner) findViewById(R.id.lstSehirFiltre);
                s.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginAfterMain.this, "Hata", Toast.LENGTH_SHORT).show();
            }
        });
        mReferenceCar = FirebaseDatabase.getInstance().getReference("araclar");
        List<String> maintitle = new ArrayList<>();
        List<String> subtitle = new ArrayList<>();
        List<String> cost = new ArrayList<>();
        List<String> carCity = new ArrayList<>();
        mReferenceCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    maintitle.add(snp.child("Marka").getValue().toString()+" "+snp.child("Model").getValue().toString()+" "+snp.child("ModelYili")
                            .getValue().toString());
                    subtitle.add("İlan Sahibi : "+snp.child("Paylasan").getValue().toString());
                    cost.add(snp.child("KiraBedeli").getValue().toString());
                    carCity.add(snp.child("Konum").getValue().toString());
                    CarList adapter=new CarList(LoginAfterMain.this, maintitle, subtitle,cost,carCity);
                    lstAraclar = (ListView)findViewById(R.id.LstAraclar);
                    lstAraclar.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        lstSehirFiltre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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