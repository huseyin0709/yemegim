package com.huso.yemegim;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class onayislemlerisayfasi extends AppCompatActivity {
    RecyclerView onayislemlerirecyclerview;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ArrayList<String> advesoyadarray;
    ArrayList<Double> paraarray;
    ArrayList<String> paraidarray;
    ArrayList<String> sayfadakikisiarray;
    recyclerview_onayislemleri_adapter recyclerview_onayislemleri_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onayislemlerisayfasi);

        onayislemlerirecyclerview=findViewById(R.id.onayislemleri_recyclerView);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        advesoyadarray=new ArrayList<>();
        paraarray=new ArrayList<>();
        paraidarray=new ArrayList<>();
        sayfadakikisiarray=new ArrayList<>();

        onaybekleyenisler();
    }
    public void onaybekleyenisler(){
        firebaseFirestore.collection("Para").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        String sayfadakikisi=(String) snapshot.getString("sayfadakikisi");
                        Double para=(Double) snapshot.getDouble("para");
                        String paraid=snapshot.getId();

                        firebaseFirestore.collection("Profil").document(sayfadakikisi).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if (documentSnapshot!=null){
                                    String ad=documentSnapshot.getString("ad");
                                    String soyad=documentSnapshot.getString("soyad");
                                    String advesoyad=ad+" "+soyad;

                                    advesoyadarray.add(advesoyad);
                                    paraarray.add(para);
                                    paraidarray.add(paraid);
                                    sayfadakikisiarray.add(sayfadakikisi);

                                    onayislemlerirecyclerview.setLayoutManager(new LinearLayoutManager(onayislemlerisayfasi.this));//recyclerview ın kaydırılması
                                    recyclerview_onayislemleri_adapter = new recyclerview_onayislemleri_adapter(advesoyadarray,paraarray,paraidarray,sayfadakikisiarray,firebaseFirestore,firebaseUser);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                                    onayislemlerirecyclerview.setAdapter(recyclerview_onayislemleri_adapter);//burada recyclerview ile adaptera bağlıyoruz
                                    recyclerview_onayislemleri_adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}