package com.huso.yemegim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class kayitolsayfasi extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    EditText kullaniciadiedittext,sifreedittext,yenisifeedittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayitolsayfasi);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        kullaniciadiedittext=findViewById(R.id.kayitol_edittext);
        sifreedittext=findViewById(R.id.sifre_kayitol_edittext);
        yenisifeedittext=findViewById(R.id.yenisifre_kayitol_edittext);
    }
    public void kaydetbutonu(View view){
        String kullaniciadi=kullaniciadiedittext.getText().toString();
        String sifre=sifreedittext.getText().toString();
        String yenisifre=yenisifeedittext.getText().toString();

        if(kullaniciadi.matches("") || sifre.matches("") || yenisifre.matches("")){
            Toast.makeText(kayitolsayfasi.this,"Bos Alan Mevcut!",Toast.LENGTH_LONG).show();
        }else {
            if (!sifre.matches(yenisifre)){
                Toast.makeText(kayitolsayfasi.this,"Sifre Uyusmamakta!",Toast.LENGTH_LONG).show();
            }else {
                firebaseAuth.createUserWithEmailAndPassword(kullaniciadi,sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(kayitolsayfasi.this,"Basarili bir sekilde kaydedildi...",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(kayitolsayfasi.this,anasayfa.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(kayitolsayfasi.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        System.out.println(e.getLocalizedMessage());
                    }
                });

            }


        }
    }
}