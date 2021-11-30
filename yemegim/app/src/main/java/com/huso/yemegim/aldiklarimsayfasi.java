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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class aldiklarimsayfasi extends AppCompatActivity {
    RecyclerView aldiklarimrecyclerview;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    ArrayList<String> urunadiarray;
    ArrayList<String> urunfiyatiarray;
    ArrayList<Double> alinanmiktararray;
    ArrayList<String> sayfadakikisiarray;
    recyclerview_aldiklarim_adapter recyclerview_aldiklarim_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aldiklarimsayfasi);

        aldiklarimrecyclerview=findViewById(R.id.aldiklarim_recyclerview);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        urunadiarray=new ArrayList<>();
        urunfiyatiarray=new ArrayList<>();
        alinanmiktararray=new ArrayList<>();
        sayfadakikisiarray=new ArrayList<>();

        String sayfadakikisi=firebaseUser.getUid();
        aldiklarim(sayfadakikisi);
    }
    public void aldiklarim(String sayfadakikisi){
        firebaseFirestore.collection("Alinanlar").document(sayfadakikisi).collection("Aldiklari").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                urunadiarray.clear();
                urunfiyatiarray.clear();
                alinanmiktararray.clear();
                sayfadakikisiarray.clear();
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        String urunadi=(String) snapshot.getString("urunadi");
                        String urunfiyati=(String) snapshot.getString("urunfiyati");
                        Double alinanmiktar=(Double) snapshot.getDouble("alinanmiktar");
                        String alinansayfadakikisi=(String) snapshot.getString("sayfadakikisi");

                        urunadiarray.add(urunadi);
                        urunfiyatiarray.add(urunfiyati);
                        alinanmiktararray.add(alinanmiktar);
                        sayfadakikisiarray.add(alinansayfadakikisi);

                        aldiklarimrecyclerview.setLayoutManager(new LinearLayoutManager(aldiklarimsayfasi.this));//recyclerview ın kaydırılması
                        recyclerview_aldiklarim_adapter = new recyclerview_aldiklarim_adapter(urunadiarray,urunfiyatiarray,alinanmiktararray,sayfadakikisiarray,firebaseFirestore,firebaseUser,storageReference);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                        aldiklarimrecyclerview.setAdapter(recyclerview_aldiklarim_adapter);//burada recyclerview ile adaptera bağlıyoruz
                        recyclerview_aldiklarim_adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}