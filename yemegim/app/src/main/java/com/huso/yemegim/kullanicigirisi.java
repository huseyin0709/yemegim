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
import com.google.firebase.firestore.FirebaseFirestore;

public class kullanicigirisi extends AppCompatActivity {
EditText kullaniciadigirisedittext,sifregirisedittext;
private FirebaseAuth firebaseAuth;
private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kullanici_girisi);

        kullaniciadigirisedittext=findViewById(R.id.kullanciadi_giris_edittext);
        sifregirisedittext=findViewById(R.id.sifre_giris_edittext);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser !=null){
            Intent intent=new Intent(kullanicigirisi.this,anasayfa.class);
            startActivity(intent);
        }

    }
    public void girisyapbutonu(View view){
        String kullaniciadigiris=kullaniciadigirisedittext.getText().toString();
        String sifregiris=sifregirisedittext.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(kullaniciadigiris,sifregiris).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(kullanicigirisi.this,"Giris Yapildi...",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(kullanicigirisi.this,anasayfa.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(kullanicigirisi.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public void kayitolbutonu(View view){
        Intent intent=new Intent(kullanicigirisi.this,kayitolsayfasi.class);
        startActivity(intent);
    }
}