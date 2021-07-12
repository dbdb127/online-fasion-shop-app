package com.seungwoodev.project2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class MainActivity_Tab extends AppCompatActivity {

    private static String strEmail;
    private static String strName;
    private String fragmentTransaction;
    TabLayout tabLayout;
    ViewPager2 pager2;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        Intent intent = getIntent();
        strEmail = intent.getStringExtra("email");
        strName = intent.getStringExtra("name");
        fragmentTransaction = intent.getStringExtra("cart");

        tabLayout = findViewById(R.id.tab);
        pager2 = findViewById(R.id.viewPager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("First"));
        tabLayout.addTab(tabLayout.newTab().setText("Second"));
        tabLayout.addTab(tabLayout.newTab().setText("Third"));

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_checkroom_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_person_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_person_24);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public static String getUser(){
        return strEmail;
    }
    public static String getUsername() { return strName; }
}