package com.e.yatra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Favourite extends AppCompatActivity {
    ListView listView ;
    static  ArrayList<String> places;
    static  ArrayList<LatLng> location;
  static  ArrayAdapter arrayAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        listView = (ListView) findViewById(R.id.favouritePlaces);

         places = new ArrayList<String>();
        places.add("add a place ");
        location =  new ArrayList<LatLng>();
        location.add(new LatLng(0,0));
         arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item,places);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(Favourite.this, Integer.toString(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(),MapsActivity2.class);
                intent.putExtra(" place number " ,position);
                startActivity(intent);
            }
        });

    }
}