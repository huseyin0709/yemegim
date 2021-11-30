package com.huso.yemegim;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class urunlersayfasi extends AppCompatActivity {
    RecyclerView urunlerrcyclerview;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    TextView hesaptakiparatextview;
    recyclerview_urunler_adapter recyclerview_urunler_adapter;
    ArrayList<String> urunadiarray;
    ArrayList<String> urunfiyatiarray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urunlersayfasi);

        urunlerrcyclerview=findViewById(R.id.urunler_recyclerView);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        hesaptakiparatextview=findViewById(R.id.hesaptakipara_textview);

        urunadiarray=new ArrayList<>();
        urunfiyatiarray=new ArrayList<>();

        String sayfadakikisi=firebaseUser.getUid();
        urunler();
        hesaptakipara(sayfadakikisi);
    }
    public void urunler(){
        firebaseFirestore.collection("Urunler").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        String urunadi=(String) snapshot.getString("adi");
                        String urunfiyati=(String) snapshot.getString("fiyat");
                        String sayfadakikisi=firebaseUser.getUid();

                        urunadiarray.add(urunadi);
                        urunfiyatiarray.add(urunfiyati);

                        firebaseFirestore.collection("Onayverilenler").document(sayfadakikisi).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if(documentSnapshot!=null){
                                    Double para=documentSnapshot.getDouble("paramiktari");

                                    urunlerrcyclerview.setLayoutManager(new LinearLayoutManager(urunlersayfasi.this));//recyclerview ın kaydırılması
                                    recyclerview_urunler_adapter = new recyclerview_urunler_adapter(urunadiarray,urunfiyatiarray,firebaseFirestore,firebaseUser,storageReference,para);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                                    urunlerrcyclerview.setAdapter(recyclerview_urunler_adapter);//burada recyclerview ile adaptera bağlıyoruz
                                    recyclerview_urunler_adapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                }
            }
        });
    }
    public void hesaptakipara(String sayfadakikisi){
        firebaseFirestore.collection("Onayverilenler").document(sayfadakikisi).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (documentSnapshot!=null){
                    Double para=documentSnapshot.getDouble("paramiktari");
                    hesaptakiparatextview.setText("Hesaptaki Para :"+para);
                }
            }
        });
    }
}