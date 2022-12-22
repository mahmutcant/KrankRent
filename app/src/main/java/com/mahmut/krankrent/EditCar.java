package com.mahmut.krankrent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditCar extends AppCompatActivity {
    private ImageView anaSayfa,profileIcon,addCarIcon,arabaDuzenle,iconCikis,duzenle;
    private Button btnSec;
    private CardView seciliArac;
    private Spinner myCarList;
    private DatabaseReference mReferenceMyCar;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
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
        addCarIcon = (ImageView)findViewById(R.id.addCarIcon);
        iconCikis = (ImageView)findViewById(R.id.iconCikis);
        btnSec = (Button)findViewById(R.id.btnSec);
        duzenle = (ImageView)findViewById(R.id.Imgduzenle);
        myCarList = (Spinner)findViewById(R.id.MyCarList);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        DatabaseReference mReferenceUserLoc = FirebaseDatabase.getInstance().getReference("kullanicilar")
                .child(mUser.getUid()).child("Sehir");
        mReferenceMyCar = FirebaseDatabase.getInstance().getReference("araclar");
        seciliArac = (CardView)findViewById(R.id.seciliArac);
        seciliArac.setVisibility(View.INVISIBLE);
        btnSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(acikMi %2 == 0){
                    seciliArac.setVisibility(View.VISIBLE);
                    btnSec.setText("Vazgeç");
                }else {
                    seciliArac.setVisibility(View.INVISIBLE);
                    btnSec.setText("Seç");
                }
                acikMi +=1;
            }
        });
        mReferenceUserLoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mReferenceMyCar.child(snapshot.getValue().toString()).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snp : snapshot.getChildren()){

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
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
}