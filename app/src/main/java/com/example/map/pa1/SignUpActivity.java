package com.example.map.pa1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    String username="", password="", fullname="", email="", imageName="a";
    long birthday=0;
    int count=0;
    String sort ="username";
    EditText usernameET,passwordET, fullnameET, emailET, birthdayET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent intent=getIntent();
        usernameET=(EditText)findViewById(R.id.editText3);
        passwordET=(EditText)findViewById(R.id.editText4);
        fullnameET=(EditText)findViewById(R.id.editText5);
        birthdayET=(EditText)findViewById(R.id.editText6);
        emailET=(EditText)findViewById(R.id.editText7);

        mPostReference= FirebaseDatabase.getInstance().getReference("id_list");
        Button button=findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                username=usernameET.getText().toString();
                password=passwordET.getText().toString();
                fullname=fullnameET.getText().toString();
                final String birth=birthdayET.getText().toString();
                email=emailET.getText().toString();
                if((username.length()*password.length()*fullname.length()*birth.length()*email.length())==0){
                    Toast.makeText(SignUpActivity.this,"please fill all blanks", Toast.LENGTH_SHORT).show();
                }else {

                    mPostReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(count==0) {
                                if (dataSnapshot.child(username).exists()) {
                                    Toast.makeText(SignUpActivity.this, "Please use another username", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Sign up successfully", Toast.LENGTH_LONG).show();
                                    birthday = Long.parseLong(birth);
                                    postFirebaseDatabase(true);

                                    Intent intent1 = new Intent(SignUpActivity.this, loginActivity.class);

                                    intent1.putExtra("UserName", username);
                                    startActivity(intent1);
                                    count++;

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            }

        });

    }
    public void postFirebaseDatabase(boolean add){
        Map<String, Object> childUpdates=new HashMap<>();
        Map<String, Object> postValues=null;
        if(add){
            FirebasePost post=new FirebasePost(username, password,fullname,birthday,email);
            postValues=post.toMap();

        }
        childUpdates.put(username,postValues);
        mPostReference.updateChildren(childUpdates);
        clearET();
    }
    public void clearET(){
        usernameET.setText("");
        passwordET.setText("");
        fullnameET.setText("");
        birthdayET.setText("");
        emailET.setText("");
        birthday=0;
    }
}
