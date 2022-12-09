package com.mahmut.krankrent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.BarringInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }
    public void anaSayfaGirisYap(View v){
        Intent logIn = new Intent(MainActivity.this, LoginPage.class);
        startActivity(logIn);
    }
    public void kayitOl(View v){
        Intent logIn = new Intent(MainActivity.this, RegisterPage.class);
        startActivity(logIn);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, LoginAfterMain.class));
            finish();
        }
    }
}