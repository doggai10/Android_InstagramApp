package com.example.map.pa1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
    private DatabaseReference mPostReference;
    String UserName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPostReference= FirebaseDatabase.getInstance().getReference("id_list");
        Button button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
               Intent intent=new Intent(loginActivity.this,SignUpActivity.class);
               startActivity(intent);
            }


        });

        Intent intent1=getIntent();
        UserName=intent1.getStringExtra("UserName");

        final EditText id=(EditText)findViewById(R.id.editText);
        id.setText(UserName);

        final EditText pw=(EditText)findViewById(R.id.editText2);

        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id.getText().toString().length()==0){
                    Toast.makeText(loginActivity.this,"Wrong Username", Toast.LENGTH_SHORT).show();
                } else {
                    mPostReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(id.getText().toString()).exists()){
                                FirebasePost fp=dataSnapshot.child(id.getText().toString()).getValue(FirebasePost.class);
                                if(fp.password.equals(pw.getText().toString())){
                                    Toast.makeText(loginActivity.this,"login successfully",Toast.LENGTH_LONG).show();
                                    Intent intent2=new Intent(loginActivity.this,MainActivity.class);
                                    intent2.putExtra("UserName",id.getText().toString());
                                    startActivity(intent2);
                                }else{
                                    Toast.makeText(loginActivity.this,"Wrong Password",Toast.LENGTH_LONG).show();
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
}

