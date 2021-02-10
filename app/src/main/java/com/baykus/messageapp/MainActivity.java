

package com.baykus.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baykus.messageapp.Fragments.ChatFragment;
import com.baykus.messageapp.Fragments.SettingFragment;
import com.baykus.messageapp.Fragments.UserFragment;
import com.baykus.messageapp.Model.Chat;
import com.baykus.messageapp.Model.Users;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView profile_image;
    private LinearLayout linearLayout;
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private TextView textViewUserName;
    private ProgressDialog progressDialog;
    private TabLayout tabLayout;

    private ViewPager2 viewPager2;

    private Switch aSwitch;
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private ArrayList<String> fragmentTitleList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_image = findViewById(R.id.profile_image);
        toolbar = findViewById(R.id.myToolbar);
        tabLayout = findViewById(R.id.tablayout);
        viewPager2 = findViewById(R.id.viewPager2);
        textViewUserName = findViewById(R.id.textViewUserMain);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Oturum Açılıyor");
        progressDialog.setMessage("Lütfen Bekleyiniz...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        myRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);
                textViewUserName.setText(users.getUserName());
                if (users.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.drawable.sirtlan_kafasi);
                    progressDialog.dismiss();
                } else {
                    Glide.with(getApplicationContext()).load(users.getImageURL()).into(profile_image);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Error", "Failed to read value.", error.toException());
                progressDialog.dismiss();
            }
        });


        fragmentArrayList.add(new ChatFragment());
        fragmentArrayList.add(new UserFragment());
        fragmentArrayList.add(new SettingFragment());


        myRef = FirebaseDatabase.getInstance().getReference("Chats");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final MyViewPagerAdapter adapter;
                adapter = new MyViewPagerAdapter(MainActivity.this);
                viewPager2.setAdapter(adapter);


                int unread = 0;

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && !chat.isIsseen()) {
                        unread++;

                    }

                }

                if (unread == 0) {
                    fragmentTitleList.add("Sohbet");

                } else {
                    fragmentTitleList.add("(" + unread + ")" + "Sohbet");
                }


                fragmentTitleList.add("Kullanıcılar");
                fragmentTitleList.add("Profil/Ayarlar");


                new TabLayoutMediator(tabLayout, viewPager2,
                        (tab, position) -> {
                            tab.setText(fragmentTitleList.get(position));
                        }).attach();

                tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_chat_24);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_person_24);
                tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_settings_24);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private class MyViewPagerAdapter extends FragmentStateAdapter {

        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentArrayList.size();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemSignOut:
                ProgressDialog signOutDialog = new ProgressDialog(MainActivity.this);

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Oturum Kapatıldı", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();


                return true;
        }
        return false;
    }

    private void status(String status) {
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        myRef.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();

        status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();

        status("offline");
    }


}