package com.example.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.weather.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ActionBarDrawerToggle drawerToggle;

    ImageButton backButton;

    FirebaseAuth auth;
    FirebaseUser user;

    private ActivityMainBinding binding;


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fetchWeatherData("Hanoi");
        searchCity();
        auth = FirebaseAuth.getInstance();
        backButton = findViewById(R.id.backButton);
        user = auth.getCurrentUser();
        if(user == null)
        {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), sidebar.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void searchCity() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchWeatherData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchWeatherData(String cityName) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<WeatherApp> call = apiInterface.getWeatherData(cityName, "5e189e5e92d4a6d955e18e863a691000", "metric");
        call.enqueue(new Callback<WeatherApp>() {
            @Override
            public void onResponse(Call<WeatherApp> call, Response<WeatherApp> response) {
                WeatherApp responseBody = response.body();
                if (response.isSuccessful() && responseBody != null) {
                    String temperature = String.valueOf(responseBody.getMain().getTemp());
                    int humidity = responseBody.getMain().getHumidity();
                    double windSpeed = responseBody.getWind().getSpeed();
                    long sunRise = Long.parseLong(String.valueOf(responseBody.getSys().getSunrise()));
                    long sunSet = Long.parseLong(String.valueOf(responseBody.getSys().getSunset()));
                    int seaLevel = responseBody.getMain().getPressure();
                    String condition = responseBody.getWeather().get(0).getMain();
                    double maxTemp = responseBody.getMain().getTemp_max();
                    double minTemp = responseBody.getMain().getTemp_min();
                    binding.temp.setText(temperature + " ºC");
                    binding.weather.setText(condition);
                    binding.maxTemp.setText("Max Temp: " + maxTemp + " ºC");
                    binding.minTemp.setText("Min Temp: " + minTemp + " ºC");
                    binding.humidity.setText(humidity + " %");
                    binding.windSpeed.setText(windSpeed + " m/s");
                    binding.sunRise.setText(time(sunRise));
                    binding.sunSet.setText(time(sunSet));
                    binding.sea.setText(seaLevel + " hPa");
                    binding.conditions.setText(condition);
                    binding.day.setText(dayName(System.currentTimeMillis()));
                    binding.date.setText(date());
                    binding.cityName.setText(cityName);

                    changeImagesAccordingToWeatherCondition(condition);
                }
            }

            @Override
            public void onFailure(Call<WeatherApp> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void changeImagesAccordingToWeatherCondition(String condition) {
        switch (condition) {
            case "Clear Sky":
            case "Sunny":
            case "Clear":
                binding.getRoot().setBackgroundResource(R.drawable.sunny_background);
                binding.lottieAnimationView.setAnimation(R.raw.sun);
                break;
            case "Partly Clouds":
            case "Clouds":
            case "Overcast":
            case "Mist":
            case "Foggy":
                binding.getRoot().setBackgroundResource(R.drawable.colud_background);
                binding.lottieAnimationView.setAnimation(R.raw.cloud);
                break;
            case "Light Rain":
            case "Drizzle":
            case "Moderate Rain":
            case "Showers":
            case "Heavy Rain":
            case "Rain":
            case "Thunderstorm":
                binding.getRoot().setBackgroundResource(R.drawable.rain_background);
                binding.lottieAnimationView.setAnimation(R.raw.rain);
                break;
            case "Light Snow":
            case "Moderate Snow":
            case "Heavy Snow":
            case "Blizzard":
            case "Snow":
                binding.getRoot().setBackgroundResource(R.drawable.snow_background);
                binding.lottieAnimationView.setAnimation(R.raw.snow);
                break;
            default:
                binding.getRoot().setBackgroundResource(R.drawable.sunny_background);
                binding.lottieAnimationView.setAnimation(R.raw.sun);
                break;
        }
        binding.lottieAnimationView.playAnimation();
    }


    private String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }
    private String time(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000));
    }
    private String dayName(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }



}