package com.mahmut.krankrent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
    private Button btnFiltrele,btnArttir,btnAzalt;
    private Spinner lstSehirFiltre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar spinner;
    private ImageView anaSayfa,profileIcon,addCarIcon,arabaDuzenle,iconCikis;
    private LinearLayout aracListesi;
    private TextView txtAdet;
    private int adetSayici = 1;
    List<String> subtitle = new ArrayList<>();
    List<Integer> cost = new ArrayList<>();
    List<String> carCity = new ArrayList<>();
    List<String> maintitle = new ArrayList<>();
    List<String> kiraliAraclar = new ArrayList<>();
    List<String> telNo = new ArrayList<>();
    List<String> eskiFiyat = new ArrayList<>();
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
        txtAdet = (TextView)findViewById(R.id.adetSayisi);
        btnArttir = (Button)findViewById(R.id.adetArttir);
        btnAzalt = (Button)findViewById(R.id.adetAzalt);
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
            public void onCancelled(@NonNull DatabaseError error) {}
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
        btnArttir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adetSayici++;
                txtAdet.setText(Integer.toString(adetSayici));
                veriAl(adetSayici);
            }
        });
        btnArttir.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                adetSayici+=10;
                txtAdet.setText(Integer.toString(adetSayici));
                veriAl(adetSayici);
                return true;
            }
        });
        btnAzalt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(adetSayici > 10){
                    adetSayici-=10;
                    veriAl(adetSayici);
                    txtAdet.setText(Integer.toString(adetSayici));

                }
                return true;
            }
        });
        btnAzalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adetSayici > 1){
                    adetSayici--;
                    veriAl(adetSayici);
                    txtAdet.setText(Integer.toString(adetSayici));
                }
            }
        });
        mReferenceCar = FirebaseDatabase.getInstance().getReference("araclar");
        mReferenceCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    for(DataSnapshot snapshot1 : snp.getChildren()){
                        for(DataSnapshot snp2 : snapshot1.getChildren()){
                            maintitle.add(snp2.child("Marka").getValue().toString()+" "+snp2.child("Model").getValue().toString()+" "+snp2.child("ModelYili").getValue().toString());
                            subtitle.add("İlan Sahibi : "+snp2.child("Paylasan").getValue().toString());
                            cost.add(Integer.parseInt(snp2.child("KiraBedeli").getValue().toString()));
                            carCity.add(snp2.child("Konum").getValue().toString() +" \n+90 "+snp2.child("iletisim").getValue().toString());
                            kiraliAraclar.add(snp2.child("kiraliMi").getValue().toString());
                            telNo.add(snp2.child("iletisim").getValue().toString());
                            eskiFiyat.add(snp2.child("eskiFiyat").getValue().toString());
                            CarList adapter=new CarList(LoginAfterMain.this, maintitle, subtitle,cost,carCity,kiraliAraclar,telNo,eskiFiyat);
                            lstAraclar = (ListView)findViewById(R.id.LstAraclar);
                            lstAraclar.setAdapter(adapter);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnFiltrele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veriAl(Integer.parseInt(txtAdet.getText().toString()));
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
    public void veriAl(int adet){
        maintitle.clear();
        subtitle.clear();
        cost.clear();
        carCity.clear();
        kiraliAraclar.clear();
        telNo.clear();
        eskiFiyat.clear();
        String selectedCity = lstSehirFiltre.getSelectedItem().toString();
        CarList adapter=new CarList(LoginAfterMain.this, maintitle, subtitle,cost,carCity,kiraliAraclar,telNo,eskiFiyat);
        lstAraclar = (ListView)findViewById(R.id.LstAraclar);
        lstAraclar.setAdapter(adapter);
        if(lstSehirFiltre.getSelectedItemPosition() != 0){
            mReferenceCar.child(lstSehirFiltre.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snp : snapshot.getChildren()){
                        for(DataSnapshot snapshot1 : snp.getChildren()){
                            System.out.println(snapshot1.getKey());
                            maintitle.add(snapshot1.child("Marka").getValue().toString()+" "+snapshot1.child("Model").getValue().toString()+" "+snapshot1.child("ModelYili").getValue().toString());
                            subtitle.add("İlan Sahibi : "+snapshot1.child("Paylasan").getValue().toString());
                            cost.add(Integer.parseInt(snapshot1.child("KiraBedeli").getValue().toString())*adet);
                            carCity.add(snapshot1.child("Konum").getValue().toString() +" +90 "+snapshot1.child("iletisim").getValue().toString());
                            kiraliAraclar.add(snapshot1.child("kiraliMi").getValue().toString());
                            telNo.add(snapshot1.child("iletisim").getValue().toString());
                            eskiFiyat.add(snapshot1.child("eskiFiyat").getValue().toString());
                            CarList adapter=new CarList(LoginAfterMain.this, maintitle, subtitle,cost,carCity,kiraliAraclar,telNo,eskiFiyat);
                            lstAraclar = (ListView)findViewById(R.id.LstAraclar);
                            lstAraclar.setAdapter(adapter);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }else {
            mReferenceCar.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snp : snapshot.getChildren()){
                        for(DataSnapshot snapshot1 : snp.getChildren()){
                            for(DataSnapshot snp1 : snapshot1.getChildren()){
                                maintitle.add(snp1.child("Marka").getValue().toString()+" "+snp1.child("Model").getValue().toString()+" "+snp1.child("ModelYili").getValue().toString());
                                subtitle.add("İlan Sahibi : "+snp1.child("Paylasan").getValue().toString());
                                cost.add(Integer.parseInt(snp1.child("KiraBedeli").getValue().toString())*adet);
                                carCity.add(snp1.child("Konum").getValue().toString()+" +90 "+snp1.child("iletisim").getValue().toString());
                                kiraliAraclar.add(snp1.child("kiraliMi").getValue().toString());
                                eskiFiyat.add(snp1.child("eskiFiyat").getValue().toString());
                                CarList adapter=new CarList(LoginAfterMain.this, maintitle, subtitle,cost,carCity,kiraliAraclar,telNo,eskiFiyat);
                                lstAraclar = (ListView)findViewById(R.id.LstAraclar);
                                lstAraclar.setAdapter(adapter);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
}