package com.example.map.pa1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.Holder> {
    private Context context;
    LayoutInflater inflater;
    private ArrayList<FirebaseContent> items;
    ViewGroup container;
    private StorageReference mStorageRef, mStorageRef2;
    private DatabaseReference mPostReference;
    private FirebaseImage fi;
    String image="";
    public ContentAdapter(Context context, ArrayList<FirebaseContent> data) {
        this.context = context;
        this.items = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_layout, parent, false);
            Holder holder = new Holder(view);
            return holder;


    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        int itemposition = position;
        final String UserName = items.get(itemposition).id;
        String imageName = items.get(itemposition).imageName;
        holder.id.setText(items.get(itemposition).id);
        holder.contents.setText(items.get(itemposition).content);
        holder.tag.setText(items.get(itemposition).tag);
        final long ONE_MEGABYTE = 1024 * 1024;
        mPostReference = FirebaseDatabase.getInstance().getReference("image_list");
        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(UserName).exists()) {
                    fi = dataSnapshot.child(UserName).getValue(FirebaseImage.class);
                    image = fi.imageName;

                        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
                        mStorageRef.getStorage();
                        StorageReference ref2 = mStorageRef.child(image);

                        ref2.getBytes(ONE_MEGABYTE).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                holder.image.setImageBitmap(bitmap);
                                //Toast.makeText(MainActivity.this,"Download success!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mStorageRef2 = FirebaseStorage.getInstance().getReference("Posts");
        mStorageRef2.getStorage();
        StorageReference ref2 = mStorageRef2.child(imageName);
            ref2.getBytes(ONE_MEGABYTE).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.contentImage.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.contentImage.setImageBitmap(bitmap);
                    //Toast.makeText(MainActivity.this,"Download success!", Toast.LENGTH_LONG).show();
                }
            });

        }



    @Override
    public int getItemCount() {
        return items.size();

    }


    public class Holder extends RecyclerView.ViewHolder {
        public ImageView image, contentImage;
        public TextView id;
        public TextView contents;
        public TextView tag;
        public Holder(View view) {
            super(view);
            image=(ImageView)view.findViewById(R.id.idImage);
            id=(TextView)view.findViewById(R.id.textView12);
            contents=(TextView)view.findViewById(R.id.textView13);
            tag=(TextView)view.findViewById(R.id.textView14);
            contentImage=(ImageView)view.findViewById(R.id.content_image);



        }
    }

}
