package com.example.foursquareproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.example.foursquareproject.databinding.ActivityMainBinding;
import com.example.foursquareproject.fragments.ChooseCityFragment;
import com.example.foursquareproject.listeners.MainPageListeners;
import com.example.foursquareproject.viewmodel.MainViewModel;
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

    private MainViewModel viewModel;
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = DataBindingUtil.setContentView(this,R.layout.activity_main);




     binding.searchButton.setOnClickListener(view -> {

         String nameSize = String.valueOf(binding.textUserName.getText());
         if (nameSize == null){
             Toast.makeText(this,"Lütfen isim giriniz!", Toast.LENGTH_LONG);
         }else{
             SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
             SharedPreferences.Editor editor = preferences.edit();
             editor.putString("person", String.valueOf(binding.textUserName.getText()));
             editor.apply();

             binding.constraintScreen.setVisibility(View.GONE);
             getSupportFragmentManager().beginTransaction().replace(R.id.containerFragment,new ChooseCityFragment()).commit();

         }

              /* Intent myIntent = new Intent(MainActivity.this, WeatherActivity.class);
         MainActivity.this.startActivity(myIntent);*/

     });

     binding.textSelectCity.setOnClickListener(view -> {
        // viewModel.getCityPosition();


     });
    }


    @Override
    public void onClickItem(String items) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city",items);
        editor.apply();
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);



    }


}
