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
            public void onFocusChange(View v, boolean hasFocus) {

            }
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
        mReferenceUserLoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mReferenceMyCar.child(snapshot.getValue().toString()).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.add("");
                        for (DataSnapshot snp : snapshot.getChildren()){
                            arrayList.add(snp.child("Marka").getValue().toString()+" "+snp.child("Model").getValue().toString());
                        }
                        Spinner s = (Spinner) findViewById(R.id.MyCarList);
                        s.setAdapter(arrayAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
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
                    mReferenceUserLoc.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mReferenceMyCar.child(snapshot.getValue().toString()).child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                                int sayac = 1;
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snp : snapshot.getChildren()){
                                        if(sayac == myCarList.getSelectedItemPosition()){
                                            txtGunlukKira.setText(snp.child("KiraBedeli").getValue().toString());
                                            sayac++;
                                            btnAracDuzenleKaydet.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    closeKeyboard();
                                                    snp.getRef().child("KiraBedeli").setValue(txtGunlukKira.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(EditCar.this, "Fiyat Düzenlendi", Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(EditCar.this, LoginAfterMain.class));
                                                                    overridePendingTransition(R.anim.sag, R.anim.sol);
                                                                }
                                                                else {
                                                                    Toast.makeText(EditCar.this, "Fiyat Düzenlemesi Gerçekleştirilemedi", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                    );
                                                }
                                            });
                                            btnSil.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    snp.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(EditCar.this, "Araç Başarıyla Silindi", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(EditCar.this, LoginAfterMain.class));
                                                                overridePendingTransition(R.anim.sag, R.anim.sol);
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        sayac++;
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {}
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