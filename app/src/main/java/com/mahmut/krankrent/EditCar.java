package com.mahmut.krankrent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class EditCar extends AppCompatActivity {
    private ImageView anaSayfa,profileIcon,addCarIcon,arabaDuzenle,iconCikis,duzenle,favoritePage,adminPage;
    private Button btnSec,btnAracDuzenleKaydet,btnSil;
    private CardView seciliArac;
    private Spinner myCarList;
    private DatabaseReference mReferenceMyCar,mReferenceUser;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText txtGunlukKira;
    private RadioButton kirali,kiraliDegil;
    int acikMi = 0;
    String sehirAl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_car);
        profileIcon = (ImageView)findViewById(R.id.profileIcon);
        arabaDuzenle = (ImageView)findViewById(R.id.arabaDuzenle);
        arabaDuzenle.setVisibility(View.INVISIBLE);
        anaSayfa = (ImageView)findViewById(R.id.iconMainPage);
        adminPage = (ImageView)findViewById(R.id.adminPage);
        addCarIcon = (ImageView)findViewById(R.id.addCarIcon);
        iconCikis = (ImageView)findViewById(R.id.iconCikis);
        favoritePage = (ImageView)findViewById(R.id.favoritePage);
        btnSec = (Button)findViewById(R.id.btnSec);
        btnAracDuzenleKaydet = (Button)findViewById(R.id.btnAracDuzenle);
        btnSil = (Button)findViewById(R.id.btnAracSil);
        txtGunlukKira = (EditText)findViewById(R.id.txtGunlukKira);
        kirali = (RadioButton)findViewById(R.id.kirali);
        kiraliDegil = (RadioButton)findViewById(R.id.kiraliDegil);
        txtGunlukKira.setEnabled(false);
        duzenle = (ImageView)findViewById(R.id.Imgduzenle);
        myCarList = (Spinner)findViewById(R.id.MyCarList);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        DatabaseReference mReferenceUserLoc = FirebaseDatabase.getInstance().getReference("kullanicilar")
                .child(mUser.getUid()).child("Sehir");
        mReferenceMyCar = FirebaseDatabase.getInstance().getReference("araclar");
        mReferenceUser = FirebaseDatabase.getInstance().getReference("kullanicilar").child(mUser.getUid());
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("adminMi").getValue().toString() == "true"){
                    adminPage.setVisibility(View.VISIBLE);
                }
                else{
                    adminPage.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        seciliArac = (CardView)findViewById(R.id.seciliArac);
        seciliArac.setVisibility(View.INVISIBLE);
        txtGunlukKira.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {}
        });
        favoritePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditCar.this, FavoritePage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        adminPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditCar.this, AdminSpecialPage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acikMi % 2 == 0){
                    seciliArac.setVisibility(View.VISIBLE);
                    btnSec.setText("Vazgeç");
                }else {
                    seciliArac.setVisibility(View.INVISIBLE);
                    btnSec.setText("Seç");
                }
                acikMi +=1;
            }
        });
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.my_selected_item,arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        mReferenceMyCar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    if(snp.child(mUser.getUid()).getValue() != null){
                        for(DataSnapshot snapshot1 : snp.getChildren()){
                            for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                                arrayList.add("");
                                arrayList.add(snapshot2.getKey());
                            }
                            Spinner s = (Spinner) findViewById(R.id.MyCarList);
                            s.setAdapter(arrayAdapter);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myCarList.getSelectedItemPosition() == 0){
                    Toast.makeText(EditCar.this, "Hiçbir Araç seçilmedi", Toast.LENGTH_SHORT).show();
                    seciliArac.setVisibility(View.INVISIBLE);
                }
                else{
                    seciliArac.setVisibility(View.VISIBLE);
                    mReferenceMyCar.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            btnAracDuzenleKaydet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!txtGunlukKira.getText().toString().isEmpty()){
                                        for(DataSnapshot snp : snapshot.getChildren()){
                                            for(DataSnapshot snapshot1 : snp.getChildren()){
                                                for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                                                    if(snapshot2.getKey() == myCarList.getSelectedItem()){
                                                        snapshot2.child("eskiFiyat").getRef().setValue(snapshot2.child("KiraBedeli").getValue());
                                                        snapshot2.child("KiraBedeli").getRef().setValue(Integer.parseInt(txtGunlukKira.getText().toString()));
                                                        for(DataSnapshot snapshot3 : snapshot2.child("takibeAlanlar").getChildren()){
                                                            if(snapshot3.getValue().toString().length() > 0){
                                                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(snapshot3.getValue().toString()
                                                                        ,"Takip Edilen Araç Hk.",
                                                                        "Takip Ettiğiniz Aracın Fiyatı Değişti",getApplicationContext(),EditCar.this);
                                                                notificationsSender.SendNotifications();
                                                            }
                                                        }
                                                        startActivity(new Intent(EditCar.this, LoginAfterMain.class));
                                                        overridePendingTransition(R.anim.sag, R.anim.sol);
                                                    }
                                                }
                                            }
                                        }   
                                    }else{
                                        Toast.makeText(EditCar.this, "Günlük Kira Bedeli Boş Bırakılamaz", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            kirali.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for(DataSnapshot snp : snapshot.getChildren()){
                                        for(DataSnapshot snapshot1 : snp.getChildren()){
                                            for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                                                if(snapshot2.getKey() == myCarList.getSelectedItem()){
                                                    snapshot2.child("kiraliMi").getRef().setValue(true);
                                                    startActivity(new Intent(EditCar.this, LoginAfterMain.class));
                                                    overridePendingTransition(R.anim.sag, R.anim.sol);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            kiraliDegil.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for(DataSnapshot snp : snapshot.getChildren()){
                                        for(DataSnapshot snapshot1 : snp.getChildren()){
                                            for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                                                if(snapshot2.getKey() == myCarList.getSelectedItem()){
                                                    snapshot2.child("kiraliMi").getRef().setValue(false);
                                                    startActivity(new Intent(EditCar.this, LoginAfterMain.class));
                                                    overridePendingTransition(R.anim.sag, R.anim.sol);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                            btnSil.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for(DataSnapshot snp : snapshot.getChildren()){
                                        for(DataSnapshot snapshot1 : snp.getChildren()){
                                            for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                                                if(snapshot2.getKey() == myCarList.getSelectedItem()){
                                                    snapshot2.getRef().removeValue();
                                                    startActivity(new Intent(EditCar.this, LoginAfterMain.class));
                                                    overridePendingTransition(R.anim.sag, R.anim.sol);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                }
            }
        });
        duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtGunlukKira.setEnabled(true);
                txtGunlukKira.requestFocus();
            }
        });
        iconCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(EditCar.this, LoginPage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        anaSayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditCar.this, LoginAfterMain.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditCar.this, ProfilePage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        addCarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditCar.this, AddCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });

    }
    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}