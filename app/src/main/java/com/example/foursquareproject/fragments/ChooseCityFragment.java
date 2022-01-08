package com.example.foursquareproject.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.foursquareproject.R;
import com.example.foursquareproject.adapter.CityListAdapter;
import com.example.foursquareproject.listeners.MainPageListeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseCityFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_city, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView)  getView().findViewById(R.id.recyclerView);


        ArrayList<String> cityArrayList = new ArrayList<>(Arrays.asList(
                requireContext().getResources().getStringArray(R.array.turkey_city)));

        CityListAdapter cityListAdapter = new CityListAdapter(cityArrayList, getMainListener());

        recyclerView.setAdapter(cityListAdapter);
        recyclerView.setHasFixedSize(false);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (!(context instanceof MainPageListeners)) {
            throw new IllegalStateException(
                    "You should implement OnSendMoneyProcedureListener in your activity");
        }
    }

    private MainPageListeners getMainListener() {
        return (MainPageListeners) getActivity();
    }
}