package com.example.foursquareproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.example.foursquareproject.databinding.ActivityMainBinding;
import com.example.foursquareproject.fragments.ChooseCityFragment;
import com.example.foursquareproject.listeners.MainPageListeners;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements MainPageListeners {


    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);



     binding.searchButton.setOnClickListener(view -> {
         //acıalcak bu comment
         /*binding.constraintScreen.setVisibility(View.GONE);
         getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,new ChooseCityFragment()).commit();*/

         Intent myIntent = new Intent(MainActivity.this, WeatherActivity.class);

         MainActivity.this.startActivity(myIntent);

     });
    }


    @Override
    public void onClickItem(String items) {
        Toast.makeText(this, "ssss",
                Toast.LENGTH_SHORT).show();
    }


}