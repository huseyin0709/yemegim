package com.huso.yemegim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class recyclerview_onayislemleri_adapter extends RecyclerView.Adapter<recyclerview_onayislemleri_adapter.Postonay> {
    ArrayList<String> advesoyadarray;
    ArrayList<Double> paraarray;
    ArrayList<String> paraidarray;
    ArrayList<String> sayfadakikisiarray;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    public recyclerview_onayislemleri_adapter(ArrayList<String> advesoyadarray, ArrayList<Double> paraarray, ArrayList<String> paraidarray, ArrayList<String> sayfadakikisiarray, FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser) {
        this.advesoyadarray = advesoyadarray;
        this.paraarray = paraarray;
        this.paraidarray = paraidarray;
        this.sayfadakikisiarray = sayfadakikisiarray;
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @Override
    public Postonay onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.onayislemleri_recyclerview,parent,false);
        return new recyclerview_onayislemleri_adapter.Postonay(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Postonay holder, int position) {
        Context context=holder.itemView.getContext();
        holder.onaybekleyenkisitextview.setText(" "+advesoyadarray.get(position)+"       "+paraarray.get(position)+"TL");
        holder.onayverimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> onayparadata=new HashMap<>();
                onayparadata.put("paramiktari",paraarray.get(position));
                onayparadata.put("sayfadakikisi",sayfadakikisiarray.get(position));
                firebaseFirestore.collection("Onayverilenler").document(sayfadakikisiarray.get(position)).set(onayparadata).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"Onay Verildi...",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return advesoyadarray.size();
    }

    class Postonay extends RecyclerView.ViewHolder{
        TextView onaybekleyenkisitextview;
        ImageView onayverimageview,onayvermeimageview;

        public Postonay(@NonNull View itemView) {
            super(itemView);

            onaybekleyenkisitextview=itemView.findViewById(R.id.onaybekleyenkisi_textview);
            onayverimageview=itemView.findViewById(R.id.onayver_imageview);
            onayvermeimageview=itemView.findViewById(R.id.onayverme_imageview);
        }
    }
}
