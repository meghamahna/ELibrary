package com.example.capstone_elibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolBar);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.home){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            Toast.makeText(MainActivity.this, "Home Page selected", Toast.LENGTH_SHORT).show();
        }
        else if (menuItem.getItemId() == R.id.dictionary) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DictionaryFragment()).commit();
            Toast.makeText(MainActivity.this, "Dictionary selected", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.translator) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TranslatorFragment()).commit();
            Toast.makeText(MainActivity.this, "Translator selected", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.notes){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment()).commit();
            Toast.makeText(MainActivity.this, "Notes  selected", Toast.LENGTH_SHORT).show();
        }
        else if (menuItem.getItemId() == R.id.logout) {

            Toast.makeText(MainActivity.this, "You have successfully Logged out", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

//    public void logout(View view) {
//        FirebaseAuth.getInstance().signOut();
//        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//        finish();
//    }
}
