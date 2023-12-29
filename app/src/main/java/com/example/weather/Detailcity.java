package com.example.weather;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Detailcity extends AppCompatActivity {

    private TextView temperatureTextView;
    private TextView cityTextView;
    private TextView humidityTextView;
    private TextView statusTextView;

    private Button button_Delete_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailcity);
        button_Delete_details = findViewById(R.id.button_Delete_details);
        String cityName = getIntent().getStringExtra("CITY_NAME");
        TextView cityNameTextView = findViewById(R.id.cityTextView);
        cityNameTextView.setText(cityName);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        temperatureTextView = findViewById(R.id.temperatureTextView);
        cityTextView = findViewById(R.id.cityTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        statusTextView = findViewById(R.id.statusTextView);

        fetchWeatherData(cityName);
        button_Delete_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Detailcity.this);
                builder.setMessage("Do you want to delete favorite city ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MyDatabaseHelper myDB = new MyDatabaseHelper(Detailcity.this);
                                myDB.deleteCity(cityName);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(Detailcity.this, sidebar.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void fetchWeatherData(String cityName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<WeatherApp> call = apiInterface.getWeatherData(cityName, "5e189e5e92d4a6d955e18e863a691000", "metric");
        call.enqueue(new Callback<WeatherApp>() {
            @Override
            public void onResponse(Call<WeatherApp> call, Response<WeatherApp> response) {
                WeatherApp responseBody = response.body();
                if (response.isSuccessful() && responseBody != null) {
                    double temperature = responseBody.getMain().getTemp();
                    temperatureTextView.setText("Temperature: " + temperature + " ºC");

                    String city = responseBody.getName();
                    cityTextView.setText("City: " + city);

                    int humidity = responseBody.getMain().getHumidity();
                    humidityTextView.setText("Humidity: " + humidity + "%");

                    String status = responseBody.getWeather().get(0).getDescription();
                    statusTextView.setText("Status: " + status);
                }
            }

            @Override
            public void onFailure(Call<WeatherApp> call, Throwable t) {
                t.printStackTrace();
            }

        });

    }
}
