package com.example.etessera20.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.etessera20.R;
import com.example.etessera20.models.Eveniment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = iniToolbar();

        iniNavigationView(toolbar);
    }

    private Toolbar iniToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return toolbar;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        closeDrawer();

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_lstFav:
                Intent intent2 = new Intent(this, FavoritesActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_portofel:
                Intent intent3 = new Intent(this, WalletActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_tikets:
                Intent intent5 = new Intent(this, MyTessera.class);
                startActivity(intent5);
                break;

            case R.id.nav_logOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent4 = new Intent(this, LoginActivity.class);
                startActivity(intent4);
                break;
        }
        return true;
    }

    private void iniNavigationView(Toolbar toolbar) {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void openDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            closeDrawer();
        }
        super.onBackPressed();
    }
}
