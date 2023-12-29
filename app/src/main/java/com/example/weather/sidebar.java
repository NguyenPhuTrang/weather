package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class sidebar extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabaseHelper myDB;
    ArrayList<String> weather_id, weather_city, weather_note;
    CustomAdapter customAdapter;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);

        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sidebar.this, AddActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(sidebar.this);
        weather_id = new ArrayList<>();
        weather_city = new ArrayList<>();
        weather_note = new ArrayList<>();
        storeDataInArrays();
        customAdapter = new CustomAdapter(sidebar.this, weather_id, weather_city, weather_note);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(sidebar.this));



        auth = FirebaseAuth.getInstance();

        drawerLayout = findViewById(R.id.drawer_Layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View headerView = navigationView.getHeaderView(0);
        textView = headerView.findViewById(R.id.HeaderEmail);

        user = auth.getCurrentUser();
        if(user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == androidx.appcompat.R.id.home) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.favoriteCity) {
                    Intent intent = new Intent(sidebar.this, sidebar.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.settings) {
                    Toast.makeText(sidebar.this, "Setting", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.logout) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(sidebar.this);
                    builder.setMessage("Do you want to log out of the application ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intents = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intents);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else if (item.getItemId() == R.id.share) {
                    Toast.makeText(sidebar.this, "Share", Toast.LENGTH_SHORT).show();
                }
                else if (item.getItemId() == R.id.Notification) {
                    Toast.makeText(sidebar.this, "Notification", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        customAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String cityName = weather_city.get(position);
                Intent intent = new Intent(sidebar.this, Detailcity.class);
                intent.putExtra("CITY_NAME", cityName); // Truyền tên thành phố
                startActivity(intent);
            }
        });

    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0)
        {
            Toast.makeText(this, "No Data !", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                weather_id.add(cursor.getString(0));
                weather_city.add(cursor.getString(1));
                weather_note.add(cursor.getString(2));

            }
        }
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }
}