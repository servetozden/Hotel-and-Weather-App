package com.example.foursquareproject;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foursquareproject.databinding.ActivityWeatherBinding;
import com.example.foursquareproject.viewmodel.MainViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public ActivityWeatherBinding binding;
    TextView city,temp,main,humidity,wind,realFeel,time;
    ImageView weatherImage;
    private FusedLocationProviderClient client;
    static int indexfor=5;
    static String lat;
    static String lon;
    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_weather);

        city = binding.textCityName;
        temp = binding.textDegree;
        main = binding.textDegreeInfo;
        humidity = binding.textHumidityValue;
        wind = binding.textWindValue;
        realFeel = binding.textRealWeelValue;
        weatherImage = binding.forecastIcon1;
        client = LocationServices.getFusedLocationProviderClient(this);
        time=binding.textDateValue;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nameCity = preferences.getString("city", "");
        String personName = preferences.getString("person", "");

        binding.textName.setText(personName.toUpperCase());


        if (ActivityCompat.checkSelfPermission(WeatherActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(WeatherActivity.this, ACCESS_FINE_LOCATION)){

                ActivityCompat.requestPermissions(WeatherActivity.this,
                        new String[]{ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(WeatherActivity.this,
                        new String[]{ACCESS_FINE_LOCATION}, 1);
            }
        }
        client.getLastLocation().addOnSuccessListener(WeatherActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    double latitude=Math.round(location.getLatitude() * 100.0)/100.0;
                    lat= String.valueOf(latitude);

                    double longitude=Math.round(location.getLongitude() * 100.0)/100.0;
                    lon= String.valueOf(longitude);

                    WeatherByLatLon(lat,lon);
                }else{
                    getCurrentWeather(nameCity);
                }

            }
        });

       // getCurrentWeather("Mersin");

    }



    public void getCurrentWeather(String city){
        OkHttpClient client = new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?q="+city+"&appid=3e80cc4fe44b2f34fa58d2823f3264d0")
                .get().build();

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response = client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                    Toast.makeText(WeatherActivity.this,"Error", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String data=response.body().string();
                    try {

                        JSONObject json=new JSONObject(data);
                        JSONObject city=json.getJSONObject("city");
                        JSONObject coord=city.getJSONObject("coord");
                        String lat =coord.getString("lat");
                        String lon=coord.getString("lon");

                         WeatherByLatLon(lat,lon);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            });

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private void WeatherByLatLon(String lat,String lon){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/forecast?lat="+lat+"&lon="+lon+"&appid=3e80cc4fe44b2f34fa58d2823f3264d0")
                .get().build();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response=client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String data=response.body().string();
                    try {

                        JSONObject json=new JSONObject(data);
                        TextView[] forecast = new TextView[5];
                        TextView[] forecastTemp=new TextView[5];
                        ImageView[] forecastIcons=new ImageView[5];
                        IdAssign(forecast,forecastTemp,forecastIcons);

                        indexfor=5;
                        for (int i=0;i<forecast.length;i++){
                            forecastCal(forecast[i],forecastTemp[i],forecastIcons[i],indexfor,json);
                        }

                        JSONArray list=json.getJSONArray("list");
                        JSONObject objects = list.getJSONObject(0);
                        JSONArray array=objects.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);

                        String description=object.getString("description");
                        String icons=object.getString("icon");

                        Date currentDate=new Date();
                        String dateString=currentDate.toString();
                        String[] dateSplit=dateString.split(" ");
                        String date=dateSplit[0]+", "+dateSplit[1] +" "+dateSplit[2];

                        JSONObject Main=objects.getJSONObject("main");
                        double temparature=Main.getDouble("temp");
                        String Temp=Math.round(temparature)+"°C";
                        double Humidity=Main.getDouble("humidity");
                        String hum=Math.round(Humidity)+"%";
                        double FeelsLike=Main.getDouble("feels_like");
                        String feelsValue=Math.round(FeelsLike)+"°";

                        JSONObject Wind=objects.getJSONObject("wind");
                        String windValue=Wind.getString("speed")+" "+"km/h";

                        JSONObject CityObject=json.getJSONObject("city");
                        String City=CityObject.getString("name");

                        setDataText(city,City);
                        setDataText(temp,Temp);
                        setDataText(main,description);
                        setDataImage(weatherImage,icons);
                        setDataText(time,date);
                        setDataText(humidity,hum);
                        setDataText(realFeel,feelsValue);
                        setDataText(wind,windValue);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setDataText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void setDataImage(final ImageView ImageView, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (value){
                    case "01d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "01n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "02d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "02n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "03d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "03n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "04d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "04n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "09d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "09n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "10d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "10n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "11d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "11n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "13d": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    case "13n": ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds)); break;
                    default:ImageView.setImageDrawable(getResources().getDrawable(R.drawable.clouds));

                }
            }
        });
    }
    private void forecastCal(TextView forecast,TextView forecastTemp,ImageView forecastIcons,int index,JSONObject json) throws JSONException {
        JSONArray list=json.getJSONArray("list");
        for (int i=index; i<list.length(); i++) {
            JSONObject object = list.getJSONObject(i);

            String dt=object.getString("dt_txt"); // dt_text.format=2020-06-26 12:00:00
            String[] a=dt.split(" ");
            if ((i==list.length()-1) && !a[1].equals("12:00:00")){
                String[] dateSplit=a[0].split("-");
                Calendar calendar=new GregorianCalendar(Integer.parseInt(dateSplit[0]),Integer.parseInt(dateSplit[1])-1,Integer.parseInt(dateSplit[2]));
                Date forecastDate=calendar.getTime();
                String dateString=forecastDate.toString();
                String[] forecastDateSplit=dateString.split(" ");
                String date=forecastDateSplit[0]+", "+forecastDateSplit[1] +" "+forecastDateSplit[2];
                setDataText(forecast, date);

                JSONObject Main=object.getJSONObject("main");
                double temparature=Main.getDouble("temp");
                String Temp=Math.round(temparature)+"°";
                setDataText(forecastTemp,Temp);

                JSONArray array=object.getJSONArray("weather");
                JSONObject object1=array.getJSONObject(0);
                String icons=object1.getString("icon");
                setDataImage(forecastIcons,icons);

                return;
            }
            else if (a[1].equals("12:00:00")){

                String[] dateSplit=a[0].split("-");
                Calendar calendar=new GregorianCalendar(Integer.parseInt(dateSplit[0]),Integer.parseInt(dateSplit[1])-1,Integer.parseInt(dateSplit[2]));
                Date forecastDate=calendar.getTime();
                String dateString=forecastDate.toString();
                String[] forecastDateSplit=dateString.split(" ");
                String date=forecastDateSplit[0]+", "+forecastDateSplit[1] +" "+forecastDateSplit[2];
                setDataText(forecast, date);


                JSONObject Main=object.getJSONObject("main");
                double temparature=Main.getDouble("temp");
                String Temp=Math.round(temparature)+"°";

                setDataText(forecastTemp,Temp);

                JSONArray array=object.getJSONArray("weather");
                JSONObject object1=array.getJSONObject(0);
                String icons=object1.getString("icon");
                setDataImage(forecastIcons,icons);


                indexfor=i+1;
                return;
            }
        }
    }

    private void IdAssign(TextView[] forecast,TextView[] forecastTemp,ImageView[] forecastIcons){
        forecast[0]=binding.forecastDay1;
        forecast[1]=binding.forecastDay2;
        forecast[2]=binding.forecastDay3;
        forecast[3]=binding.forecastDay4;
        forecast[4]=binding.forecastDay5;
        forecastTemp[0]=binding.forecastDegree1;
        forecastTemp[1]=binding.forecastDegree2;
        forecastTemp[2]=binding.forecastDegree3;
        forecastTemp[3]=binding.forecastDegree4;
        forecastTemp[4]=binding.forecastDegree5;
        forecastIcons[0]=binding.forecastIcon1;
        forecastIcons[1]=binding.forecastIcon2;
        forecastIcons[2]=binding.forecastIcon3;
        forecastIcons[3]=binding.forecastIcon4;
        forecastIcons[4]=binding.forecastIcon5;

    }


}
