package com.mahmut.krankrent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
public class ProfilePage extends AppCompatActivity {
    private ImageView anaSayfa,profileIcon,addCarIcon,arabaDuzenle,iconCikis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        profileIcon = (ImageView)findViewById(R.id.profileIcon);
        profileIcon.setVisibility(View.INVISIBLE);
        anaSayfa = (ImageView)findViewById(R.id.iconMainPage);
        addCarIcon = (ImageView)findViewById(R.id.addCarIcon);
        arabaDuzenle = (ImageView)findViewById(R.id.arabaDuzenle);
        iconCikis = (ImageView)findViewById(R.id.iconCikis);
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