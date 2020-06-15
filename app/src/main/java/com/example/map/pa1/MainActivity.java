package com.example.map.pa1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ViewPager2 viewpager2;
    //VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
    String UserName = "", fullname = "", email = "", birth = "",imageName="";
    long birthday = 0;
    private DatabaseReference mPostReference,mPostReference2;
    private static final int PICK_IMAGE = 777;
    private StorageReference mStorageRef,mStorageRef2;
    NavigationView navigationView;
    View headerView;
    ImageView navImage;
    TextView navUsername;
    Menu menu;
    Uri currentImageUri;
    FirebasePost fp;
    FirebaseImage fi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent2 = getIntent();
        UserName = intent2.getStringExtra("UserName");
        Toolbar tb = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, tb, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.drawer);
        headerView = navigationView.getHeaderView(0);
        navImage= (ImageView) headerView.findViewById(R.id.drawer_image);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        mPostReference2=FirebaseDatabase.getInstance().getReference("image_list");
        mPostReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(UserName).exists()) {
                    fi = dataSnapshot.child(UserName).getValue(FirebaseImage.class);
                    imageName = fi.imageName;
                    mStorageRef2 = FirebaseStorage.getInstance().getReference("Images");
                    mStorageRef2.getStorage();
                    StorageReference ref2 = mStorageRef2.child(imageName);
                    if (!imageName.equals("None")) {
                        final long ONE_MEGABYTE = 1024 * 1024;
                        ref2.getBytes(ONE_MEGABYTE).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                navImage.setImageBitmap(bitmap);
                                //Toast.makeText(MainActivity.this,"Download success!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mPostReference = FirebaseDatabase.getInstance().getReference("id_list");
        mPostReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    fp = dataSnapshot.child(UserName).getValue(FirebasePost.class);
                    fullname = fp.fullname;
                    birthday = fp.birthday;
                    birth = Long.toString(birthday);
                    email = fp.email;
                    navigationView = (NavigationView) findViewById(R.id.drawer);
                    headerView = navigationView.getHeaderView(0);
                    navUsername = (TextView) headerView.findViewById(R.id.userid);
                    navUsername.setText(UserName);
                    menu = navigationView.getMenu();

                    menu.findItem(R.id.item1).setTitle(fullname);
                    menu.findItem(R.id.item2).setTitle(birth);
                    menu.findItem(R.id.item3).setTitle(email);


                    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            drawerLayout.closeDrawers();
                            switch (item.getItemId()) {
                                case R.id.item1: {
                                    break;
                                }
                                case R.id.item2: {
                                    break;
                                }
                                case R.id.item3: {
                                    break;
                                }
                            }

                            return false;

                        }


                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        Button button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                intent.putExtra("UserName", UserName);
                startActivity(intent);
            }


        });
        navImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);

            }


        });

        drawerLayout.closeDrawer(GravityCompat.START);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(new myFragmentStateAdapter(this, UserName));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                switch (position){
                    case 0:
                        tab.setText("Personal");
                        break;
                    case 1:
                        tab.setText("Public");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();

    }


    /*public void setUpViewPager(ViewPager viewPager) {

        adapter.addFragment(new PersonalFragment(), "Personal", UserName);
        adapter.addFragment(new PublicFragment(), "Public");
        viewPager.setAdapter(adapter);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.drawer);
            View headerView = navigationView.getHeaderView(0);
            ImageView navImage = (ImageView) headerView.findViewById(R.id.drawer_image);
            currentImageUri = data.getData();
            navImage.setImageURI(data.getData());
            StorageReference ref=mStorageRef.child(currentImageUri.getLastPathSegment());
            mPostReference2 = FirebaseDatabase.getInstance().getReference("image_list");
            FirebaseImage fi=new FirebaseImage(UserName,currentImageUri.getLastPathSegment());
            Map<String, Object> childUpdates=new HashMap<>();
            Map<String, Object> postValues=null;
            postValues=fi.toMap3();
            childUpdates.put(UserName,postValues);
            mPostReference2.updateChildren(childUpdates);
            UploadTask uploadTask=ref.putFile(currentImageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,"Upload fail!", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this,"Upload success!", Toast.LENGTH_LONG).show();

                }
            });

        }
    }

}

