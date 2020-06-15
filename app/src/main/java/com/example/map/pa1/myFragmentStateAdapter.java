package com.example.map.pa1;

import android.content.ComponentCallbacks;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class myFragmentStateAdapter extends FragmentStateAdapter{

    String UserName;
    public myFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, String userName) {
        super(fragmentActivity);
        this.UserName=userName;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new PersonalFragment(UserName);
            case 1:
               return new PublicFragment(UserName);

        }
        return null;

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}