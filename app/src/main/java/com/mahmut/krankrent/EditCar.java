package com.mahmut.krankrent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditCar extends AppCompatActivity {
    private ImageView anaSayfa,profileIcon,addCarIcon,arabaDuzenle,iconCikis,duzenle;
    private Button btnSec,btnAracDuzenleKaydet,btnSil;
    private CardView seciliArac;
    private Spinner myCarList;
    private DatabaseReference mReferenceMyCar;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private EditText txtGunlukKira;
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
        btnAracDuzenleKaydet = (Button)findViewById(R.id.btnAracDuzenle);
        btnSil = (Button)findViewById(R.id.btnAracSil);
        txtGunlukKira = (EditText)findViewById(R.id.txtGunlukKira);
        txtGunlukKira.setEnabled(false);
        duzenle = (ImageView)findViewById(R.id.Imgduzenle);
        myCarList = (Spinner)findViewById(R.id.MyCarList);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        DatabaseReference mReferenceUserLoc = FirebaseDatabase.getInstance().getReference("kullanicilar")
                .child(mUser.getUid()).child("Sehir");
        mReferenceMyCar = FirebaseDatabase.getInstance().getReference("araclar");
        seciliArac = (CardView)findViewById(R.id.seciliArac);
        seciliArac.setVisibility(View.INVISIBLE);
        txtGunlukKira.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {}
        });
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
                                                        snapshot2.child("KiraBedeli").getRef().setValue(Integer.parseInt(txtGunlukKira.getText().toString()));
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