package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> weather_id;
    private ArrayList<String> weather_city;
    private ArrayList<String> weather_note;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CustomAdapter(Context context, ArrayList<String> weather_id, ArrayList<String> weather_city, ArrayList<String> weather_note) {
        this.context = context;
        this.weather_id = weather_id;
        this.weather_city = weather_city;
        this.weather_note = weather_note;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return weather_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView weather_id_txt, weather_city_txt, weather_note_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            weather_id_txt = itemView.findViewById(R.id.weather_id_txt);
            weather_city_txt = itemView.findViewById(R.id.weather_city_txt);
            weather_note_txt = itemView.findViewById(R.id.weather_note_txt);
        }

        public void bindData(int position) {
            weather_id_txt.setText(weather_id.get(position));
            weather_city_txt.setText(weather_city.get(position));
            weather_note_txt.setText(weather_note.get(position));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}