package com.mahmut.krankrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class LoginAfterMain extends AppCompatActivity {
    private ListView lstAraclar;
    private TextView adVer,lblAracListesi;
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar spinner;
    private ImageView menuIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after_main);
        lstAraclar = (ListView)findViewById(R.id.LstAraclar);
        lblAracListesi = (TextView)findViewById(R.id.lblAracListesi);
        menuIcon = (ImageView)findViewById(R.id.menuIcon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginAfterMain.this, "Menu Açılacak", Toast.LENGTH_SHORT).show();
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