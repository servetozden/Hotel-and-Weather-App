package com.example.foursquareproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foursquareproject.R;
import com.example.foursquareproject.listeners.MainPageListeners;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    private List<String> cityList;
    private final MainPageListeners listener;


    public CityListAdapter(List<String> items, MainPageListeners listener) {
        this.listener = listener;
        cityList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tvCity.setText(cityList.get(position));

        holder.tvCity.setOnClickListener(v -> {
            if (null != listener) {
                listener.onClickItem(cityList.get(position));
            }
        });

    }
    

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCity;

        ViewHolder(View view) {
            super(view);
            tvCity = itemView.findViewById(R.id.txtCityName);
        }
    }
}
