package com.mahmut.krankrent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {
    private ImageView anaSayfa,profileIcon,addCarIcon,arabaDuzenle,iconCikis,acAdi,acSoyad,acTelefon,acSehirSpinner;
    private Button btnKaydet,btnVazgec;
    private EditText txtProfilAdi,txtProfilSoyadi,txtProfilTelefon;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        txtProfilAdi = (EditText)findViewById(R.id.txtProfilAdi);
        txtProfilSoyadi = (EditText)findViewById(R.id.txtProfilSoyadi);
        txtProfilTelefon = (EditText)findViewById(R.id.txtProfilTelefon);
        profileIcon = (ImageView)findViewById(R.id.profileIcon);
        profileIcon.setVisibility(View.INVISIBLE);
        anaSayfa = (ImageView)findViewById(R.id.iconMainPage);
        addCarIcon = (ImageView)findViewById(R.id.addCarIcon);
        arabaDuzenle = (ImageView)findViewById(R.id.arabaDuzenle);
        iconCikis = (ImageView)findViewById(R.id.iconCikis);
        acAdi = (ImageView)findViewById(R.id.acAdi);
        acSoyad = (ImageView)findViewById(R.id.acSoyad);
        acTelefon = (ImageView)findViewById(R.id.acTelefon);
        acSehirSpinner = (ImageView)findViewById(R.id.acSehirSpinner);
        btnKaydet = (Button)findViewById(R.id.btnProfilKaydet);
        btnVazgec = (Button)findViewById(R.id.btnProfilVazgec);
        mReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(mUser.getUid());
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtProfilAdi.setText(snapshot.child("Adi").getValue().toString());
                txtProfilSoyadi.setText(snapshot.child("Soyadi").getValue().toString());
                txtProfilTelefon.setText(snapshot.child("kullaniciTel").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        acAdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtProfilAdi.setEnabled(true);
            }
        });
        acSoyad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtProfilSoyadi.setEnabled(true);
            }
        });
        acTelefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtProfilTelefon.setEnabled(true);
            }
        });
        iconCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ProfilePage.this, LoginPage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        anaSayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, LoginAfterMain.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        addCarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, AddCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        arabaDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, EditCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
    }
}