package com.huso.yemegim;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class anasayfa extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    CardView onayislemlericardview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        onayislemlericardview=findViewById(R.id.onayislemleri_cardview);
        onayislemlericardview.setVisibility(View.INVISIBLE);
        if(firebaseUser.getUid().matches("P6O2inrBpXRi9Q0Un81dZpXGXMg1")){
            onayislemlericardview.setVisibility(View.VISIBLE);
        }
    }
    public void onayislemleributonu(View view){
        Intent intent=new Intent(anasayfa.this,onayislemlerisayfasi.class);
        startActivity(intent);
    }
    public void profilimiduzenlebutonu(View view){
        Intent intent=new Intent(anasayfa.this,profilduzenlemesayfasi.class);
        startActivity(intent);
    }
    public void urunlerbutonu(View view){
        Intent intent=new Intent(anasayfa.this,urunlersayfasi.class);
        startActivity(intent);
    }
    public void paraeklebutonu(View view){
        Intent intent=new Intent(anasayfa.this,paraeklesayfasi.class);
        startActivity(intent);
    }
    public void aldiklarimbutonu(View view){
        Intent intent=new Intent(anasayfa.this,aldiklarimsayfasi.class);
        startActivity(intent);
    }
    public void cikisyapbutonu(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(anasayfa.this);
        builder.setTitle("ÇIKIŞ YAP");
        builder.setMessage("Çıkış Yapmak İstermisiniz ?");
        builder.setNegativeButton("HAYIR",null);
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                Intent intenttosignup=new Intent(anasayfa.this,kullanicigirisi.class);
                startActivity(intenttosignup);
                finish();
            }
        });
        builder.show();
    }
}