package com.example.map.pa1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    EditText content,tag;
    CheckBox check;
    ImageView imageView;
    String UserName, contents, hashTag, imageName="";
    Boolean bool;
    int count=0;
    private static final int PICK_IMAGE = 777;
    private StorageReference mStorageRef;
    Uri currentImageUri;
    public static int tmp=0;
    boolean che=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        Intent intent4 = getIntent();
        UserName = intent4.getStringExtra("UserName");
        imageView = (ImageView) findViewById(R.id.postImage);
        content = (EditText) findViewById(R.id.postContent);
        tag = (EditText) findViewById(R.id.postTag);
        check = (CheckBox) findViewById(R.id.checkbox);
        bool = false;
        mPostReference = FirebaseDatabase.getInstance().getReference("post_list");
        mStorageRef = FirebaseStorage.getInstance().getReference("Posts");
        imageView = findViewById(R.id.postImage);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }


        });
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (content.getText().toString().length() == 0) {
                    Toast.makeText(AddPostActivity.this, "Please input contents", Toast.LENGTH_LONG).show();
                } else {
                    mPostReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (check.isChecked()) {
                                //public
                                mPostReference = FirebaseDatabase.getInstance().getReference("post_list/public");
                                contents = content.getText().toString();
                                String Tag = tag.getText().toString();
                                String[] array = Tag.split(" ");
                                StringBuilder temp = new StringBuilder("");
                                for (int i = 0; i < array.length; i++) {
                                    temp.append("#" + array[i]);
                                }
                                if(temp.length()==1){
                                    hashTag="";
                                }else {
                                    hashTag = temp.toString();
                                }
                                postFirebaseContent(true);
                            } else {
                                //personal
                                mPostReference = FirebaseDatabase.getInstance().getReference("post_list/personal");
                                contents = content.getText().toString();
                                String Tag = tag.getText().toString();
                                String[] array = Tag.split(" ");
                                StringBuilder temp = new StringBuilder("");
                                for (int i = 0; i < array.length; i++) {
                                    temp.append("#" + array[i]);
                                }
                                if(temp.length()==1){
                                    hashTag="";
                                }else {
                                    hashTag = temp.toString();
                                }
                                postFirebaseContent(false);
                            }
                            count++;

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                Intent intent5 = new Intent(AddPostActivity.this, MainActivity.class);
                intent5.putExtra("UserName", UserName);
                startActivity(intent5);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageView=findViewById(R.id.postImage);
            currentImageUri = data.getData();
            imageView.setImageURI(data.getData());
            imageName=currentImageUri.getLastPathSegment();
            StorageReference ref=mStorageRef.child(currentImageUri.getLastPathSegment());
            UploadTask uploadTask=ref.putFile(currentImageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this,"Upload fail!", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    tmp++;
                    Toast.makeText(AddPostActivity.this,"Upload success!", Toast.LENGTH_LONG).show();
                    che=true;
                }
            });

        }
    }

    public void postFirebaseContent(boolean add){
        Map<String, Object> childUpdates=new HashMap<>();
        Map<String, Object> postValues=null;
        if(add){
            if(che){
                FirebaseContent post=new FirebaseContent(UserName,contents,hashTag,imageName);
                postValues=post.toMap2();
                childUpdates.put(contents,postValues);
                mPostReference= FirebaseDatabase.getInstance().getReference("post_list/public");
                mPostReference.updateChildren(childUpdates);
                //che=false;
            }else{
                FirebaseContent post=new FirebaseContent(UserName,contents,hashTag,"None");
                postValues=post.toMap2();
                childUpdates.put(contents,postValues);
                mPostReference= FirebaseDatabase.getInstance().getReference("post_list/public");
                mPostReference.updateChildren(childUpdates);
            }

        }else{
            if(che){
                FirebaseContent post=new FirebaseContent(UserName,contents,hashTag,imageName);
                postValues=post.toMap2();
                childUpdates.put(contents,postValues);
                mPostReference= FirebaseDatabase.getInstance().getReference("post_list/personal");
                mPostReference.updateChildren(childUpdates);
                //che=false;
            }else{
                FirebaseContent post=new FirebaseContent(UserName,contents,hashTag,"None");
                postValues=post.toMap2();
                childUpdates.put(contents,postValues);
                mPostReference= FirebaseDatabase.getInstance().getReference("post_list/personal");
                mPostReference.updateChildren(childUpdates);
            }

        }
        clearET();
    }
    public void clearET(){
        content.setText("");
        tag.setText("");
        check.setChecked(false);
    }
}
