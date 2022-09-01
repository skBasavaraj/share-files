package com.example.filesharing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.example.filesharing.adapters.ViewPagerAdapter;
import com.example.filesharing.fragments.AppFragment;
import com.example.filesharing.fragments.DocFragment;
import com.example.filesharing.fragments.ImageFragment;
import com.example.filesharing.fragments.audioFragemt;
import com.example.filesharing.fragments.videoFragment;
import com.example.filesharing.model.ItemList;
import com.example.filesharing.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewPagers extends AppCompatActivity   {
    public ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public  ArrayAdapter adapter;
    ListView listView;
     FloatingActionButton  fab1;
     public List<ItemList> items = new ArrayList<>();
         @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_items_activty);
        viewPager = findViewById(R.id.viewpager);
         fab1=findViewById(R.id.floatingActionButton);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DiscoverList.list.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Select Some Items", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ViewPagers.this, DiscoverList.class);
                    startActivity(intent);
                }
            }
        });

          viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
             viewPagerAdapter.add(new videoFragment(), "Videos");
             viewPagerAdapter.add(new audioFragemt(), "Audios");
             viewPagerAdapter.add(new DocFragment(),"Docx");
            viewPagerAdapter.add(new ImageFragment(), "Images");
            viewPagerAdapter.add(new AppFragment(),"Apps");
           viewPager.setAdapter(viewPagerAdapter);
           viewPager. setOffscreenPageLimit(5);
           tabLayout = findViewById(R.id.tab_layout);
           tabLayout.setupWithViewPager(viewPager);

     }

   /* public  void PassItem(List<  ItemList> select)
    {
             items=select;
     }
*/
  /* public void vis(){

           fab.setVisibility(View.VISIBLE);
   }*/



}