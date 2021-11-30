package com.huso.yemegim;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class recyclerview_urunler_adapter extends RecyclerView.Adapter<recyclerview_urunler_adapter.Posturunler> {
    ArrayList<String> urunadiarray;
    ArrayList<String> urunfiyatiarray;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    Double para;

    public recyclerview_urunler_adapter(ArrayList<String> urunadiarray, ArrayList<String> urunfiyatiarray, FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser,StorageReference storageReference,Double para) {
        this.urunadiarray = urunadiarray;
        this.urunfiyatiarray = urunfiyatiarray;
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
        this.storageReference=storageReference;
        this.para=para;
    }

    @NonNull
    @Override
    public Posturunler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.urunler_recyclerview,parent,false);
        return new Posturunler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Posturunler holder, int position) {

        holder.urunaditextview.setText(urunadiarray.get(position));
        holder.fiyattextview.setText("Urun Fiyati :"+urunfiyatiarray.get(position)+" "+"TL");
        StorageReference newreference=storageReference.child(urunadiarray.get(position)+".jpg");
        newreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadurl=uri.toString();
                Picasso.get().load(downloadurl).into(holder.urunimageview);
            }
        });
        holder.satinalbutonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context=holder.itemView.getContext();
                final EditText editText=new EditText(context);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Miktar Gir");
                builder.setMessage("Kac Tane Almak İstermisiniz ?");
                builder.setView(editText);
                builder.setNegativeButton("Satın Alma",null);
                builder.setPositiveButton("Satın Al", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String miktaredittext=editText.getText().toString();
                        Double miktar=Double.parseDouble(miktaredittext);
                        Double urunfiyati=Double.parseDouble(urunfiyatiarray.get(position));
                        String sayfadakikisi=firebaseUser.getUid();
                        Double sonuc=para-miktar*urunfiyati;
                        firebaseFirestore.collection("Onayverilenler").document(sayfadakikisi).update("paramiktari",sonuc).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                        HashMap<String,Object> aldiklarimdata=new HashMap<>();
                        aldiklarimdata.put("urunadi",urunadiarray.get(position));
                        aldiklarimdata.put("urunfiyati",urunfiyatiarray.get(position));
                        aldiklarimdata.put("sayfadakikisi",sayfadakikisi);
                        aldiklarimdata.put("alinanmiktar",miktar);
                        firebaseFirestore.collection("Alinanlar").document(sayfadakikisi).collection("Aldiklari").add(aldiklarimdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(context,"Tebrikler Satın Alınmıştır...",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                builder.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return urunadiarray.size();
    }

    class Posturunler extends RecyclerView.ViewHolder{
        TextView urunaditextview,fiyattextview;
        ImageView urunimageview;
        Button satinalbutonu;

        public Posturunler(@NonNull View itemView) {
            super(itemView);
            urunaditextview=itemView.findViewById(R.id.urunadi_textview);
            fiyattextview=itemView.findViewById(R.id.Fiyati_textview);
            urunimageview=itemView.findViewById(R.id.urun_imageview);
            satinalbutonu=itemView.findViewById(R.id.satinal_butonu);


        }
    }
}
