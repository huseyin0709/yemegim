package com.huso.yemegim;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class recyclerview_aldiklarim_adapter extends RecyclerView.Adapter<recyclerview_aldiklarim_adapter.Postaldiklarim> {
    ArrayList<String> urunadiarray;
    ArrayList<String> urunfiyatiarray;
    ArrayList<Double> alinanmiktararray;
    ArrayList<String> sayfadakikisiarray;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    StorageReference storageReference;

    public recyclerview_aldiklarim_adapter(ArrayList<String> urunadiarray, ArrayList<String> urunfiyatiarray, ArrayList<Double> alinanmiktararray, ArrayList<String> sayfadakikisiarray, FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser, StorageReference storageReference) {
        this.urunadiarray = urunadiarray;
        this.urunfiyatiarray = urunfiyatiarray;
        this.alinanmiktararray = alinanmiktararray;
        this.sayfadakikisiarray = sayfadakikisiarray;
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
        this.storageReference = storageReference;
    }

    @NonNull
    @Override
    public Postaldiklarim onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.aldiklarim_recyclerview,parent,false);
        return new recyclerview_aldiklarim_adapter.Postaldiklarim(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Postaldiklarim holder, int position) {
        holder.alinanurunadi.setText(urunadiarray.get(position));
        holder.alinanurunfiyati.setText("Aldığımız Fiyat : "+urunfiyatiarray.get(position));
        holder.alinanurunmiktari.setText("Kaç Tane Aldık : "+alinanmiktararray.get(position).toString());
        StorageReference newreference=storageReference.child(urunadiarray.get(position)+".jpg");
        newreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadurl=uri.toString();
                Picasso.get().load(downloadurl).into(holder.alinanimageview);
            }
        });

    }

    @Override
    public int getItemCount() {
        return urunadiarray.size();
    }

    class Postaldiklarim extends RecyclerView.ViewHolder{
        TextView alinanurunadi,alinanurunfiyati,alinanurunmiktari;
        ImageView alinanimageview;

        public Postaldiklarim(@NonNull View itemView) {
            super(itemView);

            alinanurunadi=itemView.findViewById(R.id.alinanurunadi_textview);
            alinanurunfiyati=itemView.findViewById(R.id.alinanfiyati_textview);
            alinanurunmiktari=itemView.findViewById(R.id.alinanmiktar_textview);
            alinanimageview=itemView.findViewById(R.id.alinanurun_imageview);
        }
    }
}
