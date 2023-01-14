package com.mahmut.krankrent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminSpecialPage extends AppCompatActivity {
    private ImageView anaSayfa, profileIcon, addCarIcon, arabaDuzenle, iconCikis, favoritePage, aracFotosu,adminPage;
    private TextView txtUyeSayisi,txtKiradaArac,txtAracSayisi;
    private DatabaseReference mReference,mReferenceUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_special_page);
        anaSayfa = (ImageView)findViewById(R.id.iconMainPage);
        addCarIcon = (ImageView)findViewById(R.id.addCarIcon);
        arabaDuzenle = (ImageView)findViewById(R.id.arabaDuzenle);
        profileIcon = (ImageView)findViewById(R.id.profileIcon);
        iconCikis = (ImageView)findViewById(R.id.iconCikis);
        adminPage = (ImageView)findViewById(R.id.adminPage);
        txtUyeSayisi = (TextView)findViewById(R.id.txtUyeSayisi);
        txtAracSayisi = (TextView)findViewById(R.id.txtAracSayisi);
        txtKiradaArac = (TextView)findViewById(R.id.txtKiradaArac);
        mReference =  FirebaseDatabase.getInstance().getReference("araclar");
        mReferenceUser = FirebaseDatabase.getInstance().getReference("kullanicilar");
        anaSayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSpecialPage.this, LoginAfterMain.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSpecialPage.this, ProfilePage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        addCarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSpecialPage.this, AddCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        arabaDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminSpecialPage.this, EditCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int ilanSayac = 0;
                int kiraliSayac = 0;
                for(DataSnapshot snp : snapshot.getChildren()){
                    for(DataSnapshot snapshot1 : snp.getChildren()){
                        ilanSayac += snapshot1.getChildrenCount();
                    }
                }
                txtAracSayisi.setText(String.valueOf(ilanSayac));
                for(DataSnapshot snp : snapshot.getChildren()){
                    for(DataSnapshot snapshot1 : snp.getChildren()){
                        for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                            if(snapshot2.child("kiraliMi").getValue().toString() == "true"){
                                kiraliSayac+=1;
                            }
                        }
                    }
                }
                txtKiradaArac.setText(String.valueOf(kiraliSayac));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int uyeSayac = 0;
                for(DataSnapshot snp : snapshot.getChildren()){
                    uyeSayac +=1;
                }
                txtUyeSayisi.setText(String.valueOf(uyeSayac));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}