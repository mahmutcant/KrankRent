package com.mahmut.krankrent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterPage extends AppCompatActivity {
    private DatabaseReference mReference,mReferenceUser;
    private FirebaseAuth mAuth;
    private Button btnIptal;
    private Button btnKayitOl;
    private EditText txtAd,txtSoyad,txtKullaniciAdi,txtSifre,txtSifreTekrar,txtMail,txtTelefon;
    private Spinner cmbSehir;
    private String adi,soyadi,kullaniciAdi,sifre,sifreTekrar,Email,sehir,telNo;
    private HashMap<String, Object> mData;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReference = FirebaseDatabase.getInstance().getReference("Sehir");
        mReferenceUser = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_register_page);
        btnIptal = (Button)findViewById(R.id.btnIptal);
        btnKayitOl = (Button)findViewById(R.id.btnKayitOl);
        txtAd = (EditText)findViewById(R.id.txtAdi);
        txtSoyad = (EditText)findViewById(R.id.txtSoyadi);
        txtKullaniciAdi = (EditText)findViewById(R.id.txtKullaniciAdi);
        txtSifre = (EditText)findViewById(R.id.txtSifre);
        txtSifreTekrar = (EditText)findViewById(R.id.txtSifreTekrar);
        txtTelefon = (EditText)findViewById(R.id.txtTelefon);
        txtMail = (EditText)findViewById(R.id.txtMail);
        cmbSehir = (Spinner) findViewById(R.id.cmbSehirler);
        mAuth = FirebaseAuth.getInstance();
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.my_selected_item,arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snp: snapshot.getChildren()){
                    arrayList.add(snp.getValue().toString());
                }
                Spinner s = (Spinner) findViewById(R.id.cmbSehirler);
                s.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterPage.this, "Hata", Toast.LENGTH_SHORT).show();
            }
        });
        btnIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void kayitOl(View v){
        adi = txtAd.getText().toString();
        soyadi = txtSoyad.getText().toString();
        kullaniciAdi = txtKullaniciAdi.getText().toString();
        sifre = txtSifre.getText().toString();
        sifreTekrar = txtSifreTekrar.getText().toString();
        sehir = cmbSehir.getSelectedItem().toString();
        Email = txtMail.getText().toString();
        telNo = txtTelefon.getText().toString();
        if(!TextUtils.isEmpty(adi) && !TextUtils.isEmpty(soyadi) && !TextUtils.isEmpty(kullaniciAdi) && !TextUtils.isEmpty(sifre) && !TextUtils.isEmpty(sifreTekrar) && !TextUtils.isEmpty(sehir)){
            mAuth.createUserWithEmailAndPassword(Email,sifre)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mUser = mAuth.getCurrentUser();
                                mData = new HashMap<>();
                                mData.put("Adi", adi);
                                mData.put("Soyadi", soyadi);
                                mData.put("Sehir", sehir);
                                mData.put("kullaniciEmail", Email);
                                mData.put("kullaniciTel", telNo);
                                mData.put("kullaniciID", mUser.getUid());
                                mData.put("kullaniciAdi",kullaniciAdi);
                                mReferenceUser.child("kullanicilar").child(mUser.getUid()).setValue(mData)
                                        .addOnCompleteListener(RegisterPage.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(RegisterPage.this, "Kayıt İşlemi Başarılı", Toast.LENGTH_SHORT).show();
                                                    Intent logged = new Intent(RegisterPage.this, LoginAfterMain.class);
                                                    startActivity(logged);
                                                    finish();
                                                }
                                                else {
                                                    Toast.makeText(RegisterPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }else {
                                Toast.makeText(RegisterPage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            System.out.println(sifre);
            System.out.println(sifreTekrar);
        }else{
            Toast.makeText(this, "Tüm alanları doldurduğunuzdan emin olunuz", Toast.LENGTH_SHORT).show();
        }
    }
}