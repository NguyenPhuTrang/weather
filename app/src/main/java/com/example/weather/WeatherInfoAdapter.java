package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WeatherInfoAdapter extends RecyclerView.Adapter<WeatherInfoAdapter.WeatherViewHolder> {

    private Context context;
    private ArrayList<String> cityNames;
    private ArrayList<String> weatherStatuses;
    private ArrayList<String> temperatures;
    private ArrayList<String> humidities;

    public WeatherInfoAdapter(Context context,
                              ArrayList<String> cityNames,
                              ArrayList<String> weatherStatuses,
                              ArrayList<String> temperatures,
                              ArrayList<String> humidities) {
        this.context = context;
        this.cityNames = cityNames;
        this.weatherStatuses = weatherStatuses;
        this.temperatures = temperatures;
        this.humidities = humidities;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_row, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.cityName.setText(cityNames.get(position));
        holder.weatherStatus.setText(weatherStatuses.get(position));
        holder.temperature.setText(temperatures.get(position));
        holder.humidity.setText(humidities.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return cityNames.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        TextView cityName, weatherStatus, temperature, humidity;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.cityTextView);
            weatherStatus = itemView.findViewById(R.id.statusTextView);
            temperature = itemView.findViewById(R.id.temperatureTextView);
            humidity = itemView.findViewById(R.id.humidityTextView);
        }
    }
}
