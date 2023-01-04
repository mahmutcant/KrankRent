package com.mahmut.krankrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmut.krankrent.ui.CarList;

import java.util.ArrayList;
import java.util.List;

public class FavoritePage extends AppCompatActivity {
    private ImageView iconMainPage,profileIcon,addCarIcon,arabaDuzenle,iconCikis,favoritePage;
    private ListView lstFavori;
    private DatabaseReference mReference,mReferenceCar;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    List<String> subtitle = new ArrayList<>();
    List<Integer> cost = new ArrayList<>();
    List<String> carCity = new ArrayList<>();
    List<String> maintitle = new ArrayList<>();
    List<String> kiraliAraclar = new ArrayList<>();
    List<String> telNo = new ArrayList<>();
    List<String> eskiFiyat = new ArrayList<>();
    List<String> paylasanUid = new ArrayList<>();
    List<String> aracKey = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_page);
        iconMainPage = (ImageView)findViewById(R.id.iconMainPage);
        profileIcon = (ImageView)findViewById(R.id.profileIcon);
        addCarIcon = (ImageView)findViewById(R.id.addCarIcon);
        arabaDuzenle = (ImageView)findViewById(R.id.arabaDuzenle);
        iconCikis = (ImageView)findViewById(R.id.iconCikis);
        favoritePage = (ImageView)findViewById(R.id.favoritePage);
        favoritePage.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        iconMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritePage.this, LoginAfterMain.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritePage.this, ProfilePage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        addCarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritePage.this, AddCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        arabaDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoritePage.this, EditCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        iconCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(FavoritePage.this, LoginPage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        mReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(mUser.getUid()).child("takipListesi");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp : snapshot.getChildren()){
                    System.out.println(snp.getValue());
                    mReferenceCar = FirebaseDatabase.getInstance().getReference("araclar");
                    mReferenceCar.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snp2 : snapshot.getChildren()){
                                for(DataSnapshot snapshot1 : snp2.getChildren()){
                                    for(DataSnapshot snapshot2 : snapshot1.getChildren()){
                                        if(snapshot2.getKey().toString() == snp.getValue()){
                                            System.out.println(snapshot2.getValue());
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}