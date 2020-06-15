package com.example.map.pa1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PersonalFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private DatabaseReference mPostReference;
    ViewGroup rootView;
    RecyclerView recyclerView;
    ContentAdapter adapter;
    ArrayList<FirebaseContent> data2, data3;
    public PersonalFragment() {
        // Required empty public constructor
    }

    public PersonalFragment(String userName) {
        this.mParam1=userName;
        this.mParam2=userName;
    }

    public static PersonalFragment newInstance(String userName) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, fragment.mParam1);
        args.putString(ARG_PARAM2, fragment.mParam2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String Id=mParam2;
        data2=new ArrayList<FirebaseContent>();
        data2.clear();
        data3=new ArrayList<FirebaseContent>();
        data3.clear();
        rootView=(ViewGroup)inflater.inflate(R.layout.fragment_personal,container,false);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_personal);
        mPostReference = FirebaseDatabase.getInstance().getReference("post_list/personal");
        mPostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    FirebaseContent fc=postSnapshot.getValue(FirebaseContent.class);
                    if(fc.id.equals(Id)){
                        data2.add(fc);
                    }else{
                        data3.add(fc);
                    }
                }
                data3.clear();
                adapter=new ContentAdapter(getActivity(),data2);
                adapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
