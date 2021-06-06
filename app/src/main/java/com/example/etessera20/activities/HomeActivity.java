package com.example.etessera20.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.etessera20.models.Eveniment;
import com.example.etessera20.adaptors.EvenimentAdaptor;
import com.example.etessera20.adaptors.EventItemClickListener;
import com.example.etessera20.R;
import com.example.etessera20.models.Slide;
import com.example.etessera20.adaptors.SliderPagerAdaptor;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends DrawerActivity implements EventItemClickListener {
    private List<Slide> lstSlides;
    private ViewPager slidePager;
    private TabLayout indicator;
    private RecyclerView teatruRV, standupRv, concerteRv;
    private List<Eveniment> mListaEvenimente;
    private EvenimentAdaptor teatruAdaptor, standupAdaptor, concerteAdaptor;

    private DatabaseReference databaseEvents;

    private List<Eveniment> listTeatru = new ArrayList<Eveniment>();
    private List<Eveniment> listStandup = new ArrayList<Eveniment>();
    private List<Eveniment> listConcerte = new ArrayList<Eveniment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawerLayout.addView(contentView, 0);

        mListaEvenimente = new ArrayList<Eveniment>();

        iniViews();

        iniListForAdaptors();

    }

    @SuppressLint("ResourceType")
    private void iniAdaptors(List<Eveniment> listaEvenimente) {

        for(Eveniment e: listaEvenimente){
            if(e.getType().equals("teatru")){
                listTeatru.add(e);
            }
            if(e.getType().equals("stand-up")){
                listStandup.add(e);
            }
            if(e.getType().equals("concert")){
                listConcerte.add(e);
            }

        }


        teatruAdaptor = new EvenimentAdaptor(HomeActivity.this, listTeatru, HomeActivity.this::onEventClick);
        teatruRV.setAdapter(teatruAdaptor);

        standupAdaptor = new EvenimentAdaptor(HomeActivity.this, listStandup, HomeActivity.this::onEventClick);
        standupRv.setAdapter(standupAdaptor);

        concerteAdaptor = new EvenimentAdaptor(HomeActivity.this, listConcerte, HomeActivity.this::onEventClick);
        concerteRv.setAdapter(concerteAdaptor);

    }

    private void iniListForAdaptors() {
        databaseEvents = FirebaseDatabase.getInstance().getReference().child("Events");
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListaEvenimente.clear();
                for(DataSnapshot evenimentSnapshot : snapshot.getChildren()){
                    Eveniment eveniment = evenimentSnapshot.getValue(Eveniment.class);
                    mListaEvenimente.add(eveniment);
                }

                iniAdaptors(mListaEvenimente);
                iniSlider(mListaEvenimente);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniSlider(List<Eveniment> listaEvenimente) {
        lstSlides = new ArrayList<Slide>();
        List<String>listType = new ArrayList<String>();
        listType.add("teatru"); listType.add("stand-up"); listType.add("concert");
        int position = 0;
        for(Eveniment e: listaEvenimente){
            if(e.getType().equals(listType.get(position))){
                lstSlides.add(new Slide(e.getThumbnail(),e.getTitlu()));
                position++;
            }
        }

        SliderPagerAdaptor adaptor = new SliderPagerAdaptor(this, lstSlides);
        slidePager.setAdapter(adaptor);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        indicator.setupWithViewPager(slidePager, true);
    }

    public void iniViews() {
        slidePager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        teatruRV = (RecyclerView) findViewById(R.id.rv_teatru);
        teatruRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        standupRv = (RecyclerView) findViewById(R.id.rv_standup);
        standupRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        concerteRv = (RecyclerView) findViewById(R.id.rv_concerte);
        concerteRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onEventClick(Eveniment eveniment, ImageView eventImageView) {
        Intent intent = new Intent(this, EvenimentDetailsActivity.class);

        intent.putExtra("title", eveniment.getTitlu());
        intent.putExtra("imgCover", eveniment.getThumbnail());
        intent.putExtra("imgURL", eveniment.getCover_img());
        intent.putExtra("description", eveniment.getDescription());
        intent.putExtra("price", eveniment.getPrice());
        intent.putExtra("video", eveniment.getVideo());
        intent.putExtra("id", eveniment.getId());

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, eventImageView, "sharedName");

        startActivity(intent, options.toBundle());


        Toast.makeText(this, "item clicked: " + eveniment.getTitlu(), Toast.LENGTH_LONG).show();
    }

    class SliderTimer extends TimerTask{

        @Override
        public void run() {
            HomeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(slidePager.getCurrentItem()< lstSlides.size()-1){
                        slidePager.setCurrentItem(slidePager.getCurrentItem()+1);
                    }
                    else{
                        slidePager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filer(newText, listTeatru, teatruAdaptor);
                filer(newText, listStandup, standupAdaptor);
                filer(newText, listConcerte, concerteAdaptor);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void filer(String s, List<Eveniment> cautaList, EvenimentAdaptor cautaAdaptor){
        List<Eveniment> listFilter = new ArrayList<Eveniment>();

        for(Eveniment eveniment : cautaList){
            if(eveniment.getTitlu().toLowerCase().contains(s.toLowerCase())){
                listFilter.add(eveniment);
            }

        }

        cautaAdaptor.filterList(listFilter);
    }

}
