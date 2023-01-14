package com.mahmut.krankrent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class AddCar extends AppCompatActivity {
    private ImageView anaSayfa, profileIcon, addCarIcon, arabaDuzenle, iconCikis, favoritePage, aracFotosu,adminPage;
    private Spinner aracModel, aracMarka, aracEkleSehir;
    private DatabaseReference mReference, mReference2, mReference3, mReferenceUser, mReferenceAddCar;
    private TextView txtAracinSehri;
    private CheckBox aracKonumdaMi;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private EditText aracModelYili, aracKiraBedeli;
    private HashMap<String, Object> mData;
    ProgressDialog pd;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://krankrent-60ae7.appspot.com/");
    private String kiraBedeli, modelYili;
    private Button aracKaydet;
    int SELECT_PICTURE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        mReference = FirebaseDatabase.getInstance().getReference("Markalar");
        mReference3 = FirebaseDatabase.getInstance().getReference("Sehir");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mReferenceUser = FirebaseDatabase.getInstance().getReference("kullanicilar").child(mUser.getUid());
        mReferenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("adminMi").getValue().toString() == "true"){
                    adminPage.setVisibility(View.VISIBLE);
                }
                else{
                    adminPage.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        mReferenceAddCar = FirebaseDatabase.getInstance().getReference();
        aracMarka = (Spinner) findViewById(R.id.cmbAracMarka);
        aracModel = (Spinner) findViewById(R.id.cmbAracModel);
        aracEkleSehir = (Spinner) findViewById(R.id.cmbAracEkleSehir);
        aracFotosu = (ImageView) findViewById(R.id.aracFotosu);
        aracKonumdaMi = (CheckBox) findViewById(R.id.aracKonumdaMi);
        txtAracinSehri = (TextView) findViewById(R.id.txtAracinSehri);
        aracModelYili = (EditText) findViewById(R.id.aracModelYili);
        aracKiraBedeli = (EditText) findViewById(R.id.aracKiraBedeli);
        adminPage = (ImageView)findViewById(R.id.adminPage);
        aracKaydet = (Button) findViewById(R.id.aracKaydet);
        aracKonumdaMi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aracKonumdaMi.isChecked()) {
                    txtAracinSehri.setVisibility(View.INVISIBLE);
                    aracEkleSehir.setVisibility(View.INVISIBLE);
                } else {
                    txtAracinSehri.setVisibility(View.VISIBLE);
                    aracEkleSehir.setVisibility(View.VISIBLE);
                }
            }
        });
        adminPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCar.this, AdminSpecialPage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        anaSayfa = (ImageView) findViewById(R.id.iconMainPage);
        profileIcon = (ImageView) findViewById(R.id.profileIcon);
        addCarIcon = (ImageView) findViewById(R.id.addCarIcon);
        arabaDuzenle = (ImageView) findViewById(R.id.arabaDuzenle);
        iconCikis = (ImageView) findViewById(R.id.iconCikis);
        favoritePage = (ImageView) findViewById(R.id.favoritePage);
        favoritePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCar.this, FavoritePage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        ArrayList<String> markaList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.my_selected_item, markaList);
        arrayAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        ArrayList<String> modelList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapterModel = new ArrayAdapter<String>(this, R.layout.my_selected_item, modelList);
        arrayAdapter.setDropDownViewResource(R.layout.my_dropdown_item);
        ArrayList<String> sehirList = new ArrayList<>();
        ArrayAdapter<String> sehirListAdapter = new ArrayAdapter<String>(this, R.layout.my_selected_item, sehirList);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snp : snapshot.getChildren()) {
                    markaList.add(snp.getKey().toString());
                }
                aracMarka.setAdapter(arrayAdapter);
                aracMarka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mReference2 = FirebaseDatabase.getInstance().getReference("Markalar").child(aracMarka.getSelectedItem().toString());
                        modelList.clear();
                        mReference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snp : snapshot.getChildren()) {
                                    modelList.add(snp.getValue().toString());
                                }
                                aracModel.setAdapter(arrayAdapterModel);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddCar.this, "Hata", Toast.LENGTH_SHORT).show();
            }
        });
        mReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snp : snapshot.getChildren()) {
                    sehirList.add(snp.getValue().toString());
                }
                aracEkleSehir.setAdapter(sehirListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        addCarIcon.setVisibility(View.INVISIBLE);
        anaSayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCar.this, LoginAfterMain.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCar.this, ProfilePage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        arabaDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCar.this, EditCar.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        iconCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(AddCar.this, LoginPage.class));
                overridePendingTransition(R.anim.sag, R.anim.sol);
            }
        });
        aracKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aracKiraBedeli.getText().toString().isEmpty() && aracModelYili.getText().toString().isEmpty()) {
                    Toast.makeText(AddCar.this, "Lütfen Tüm Alanları Eksiksiz Doldurun", Toast.LENGTH_SHORT).show();
                } else {
                    mReferenceUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String adAl = snapshot.child("Adi").getValue().toString() + " " + snapshot.child("Soyadi").getValue().toString();
                            String sehirAl = snapshot.child("Sehir").getValue().toString();
                            String noAl = snapshot.child("kullaniciTel").getValue().toString();
                            kiraBedeli = aracKiraBedeli.getText().toString();
                            modelYili = aracModelYili.getText().toString();
                            mData = new HashMap<>();
                            if (aracKonumdaMi.isChecked()) {
                                mData.put("Paylasan", adAl);
                                mData.put("paylasanUid", mUser.getUid());
                                mData.put("Marka", aracMarka.getSelectedItem());
                                mData.put("Model", aracModel.getSelectedItem());
                                mData.put("ModelYili", modelYili);
                                mData.put("KiraBedeli", kiraBedeli);
                                mData.put("Konum", sehirAl);
                                mData.put("iletisim", noAl);
                                mData.put("kiraliMi", false);
                                mData.put("eskiFiyat", 0);
                                mData.put("favoriyeAlanlar", 0);

                            } else {
                                mData.put("Paylasan", adAl);
                                mData.put("Marka", aracMarka.getSelectedItem());
                                mData.put("Model", aracModel.getSelectedItem());
                                mData.put("paylasanUid", mUser.getUid());
                                mData.put("ModelYili", modelYili);
                                mData.put("KiraBedeli", kiraBedeli);
                                mData.put("Konum", aracEkleSehir.getSelectedItem().toString());
                                mData.put("iletisim", noAl);
                                mData.put("kiraliMi", false);
                                mData.put("eskiFiyat", 0);
                            }
                            mReferenceAddCar.child("araclar").child(mData.get("Konum").toString()).child(mData.get("paylasanUid").toString()).child(aracMarka.getSelectedItem() + mUser.getUid() + aracModel.getSelectedItem() + modelYili).setValue(mData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AddCar.this, "Araç Kaydı Başarılı", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddCar.this, LoginAfterMain.class));
                                        overridePendingTransition(R.anim.sag, R.anim.sol);
                                    } else {
                                        Toast.makeText(AddCar.this, "Araç Kaydı Başarısız", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                }
            }
        });
    }
}