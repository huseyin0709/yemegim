package com.huso.yemegim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class paraeklesayfasi extends AppCompatActivity {
FirebaseFirestore firebaseFirestore;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
EditText paramiktariedittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paraeklesayfasi);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        paramiktariedittext=findViewById(R.id.paramiktari_edittext);
    }
    public void paraeklebutonu(View view){
        String sayfadakikisi=firebaseUser.getUid();
        String paramiktari=paramiktariedittext.getText().toString();
        Double para=Double.parseDouble(paramiktari);
        HashMap<String,Object> paradata=new HashMap<>();
        paradata.put("para",para);
        paradata.put("sayfadakikisi",sayfadakikisi);
        firebaseFirestore.collection("Para").document(sayfadakikisi).set(paradata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(paraeklesayfasi.this,"Para Onay icin gonderilmistir",Toast.LENGTH_LONG).show();
                paramiktariedittext.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(paraeklesayfasi.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}