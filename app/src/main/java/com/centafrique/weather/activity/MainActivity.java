package com.centafrique.weather.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.centafrique.weather.R;
import com.centafrique.weather.adapter.DayForecastAdapter;
import com.centafrique.weather.helper_class.AppUtil;
import com.centafrique.weather.helper_class.Calculator;
import com.centafrique.weather.helper_class.CheckInternet;
import com.centafrique.weather.pojo.DailyForecastPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private String apiKey;
    private LottieAnimationView animation_view;
    private RecyclerView recyclerView;
    private DayForecastAdapter dayForecastAdapter;
    private ArrayList<DailyForecastPojo> dailyForecastPojoArrayList = new ArrayList<DailyForecastPojo>();

    private List<String> myDt = new ArrayList<>();

    private List<String> myDesc = new ArrayList<>();

    private List<Double> myMinTemp = new ArrayList<>();
    private List<Double> mymaxTemp = new ArrayList<>();

    private List<Integer> myWeatherId = new ArrayList<>();

    private TextView temp_text_view, description_text_view, humidity_text_view, wind_text_view;
    private Calculator calculator;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    String locationText = "";
    String locationLatitude = "";
    String locationLongitude = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getApplicationContext().getSharedPreferences("Coordinates", MODE_PRIVATE);
        editor = preferences.edit();
        calculator = new Calculator();

        temp_text_view = findViewById(R.id.temp_text_view);
        description_text_view = findViewById(R.id.description_text_view);
        humidity_text_view = findViewById(R.id.humidity_text_view);
        wind_text_view = findViewById(R.id.wind_text_view);
        animation_view = findViewById(R.id.animation_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.btnGetForecast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocation();

            }
        });

    }

    private ArrayList<DailyForecastPojo> getForecast(Double Lat, Double Long, Context context){

        myDt.clear();
        myDesc.clear();
        myMinTemp.clear();
        mymaxTemp.clear();
        myWeatherId.clear();
        dailyForecastPojoArrayList.clear();


        String URL = "https://api.openweathermap.org/data/2.5/onecall?lat="+Lat+"&lon="+Long+
                "&exclude=hourly&appid="+getResources().getString(R.string.open_weather_map_api) ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.e("-*-*-*response ", String.valueOf(response));

                try {

                    //Current Forecast
                    String txtTimeZone = response.getString("timezone");

                    JSONObject jsonObject = response.getJSONObject("current");

                    JSONArray jsonArrayWeather1 = jsonObject.getJSONArray("weather");
                    JSONObject CurrentWeatherObject = jsonArrayWeather1.getJSONObject(0);

                    String txtCurrentDate = jsonObject.getString("dt");
                    Double txtTemp = jsonObject.getDouble("temp");
                    int txtHumidity = jsonObject.getInt("humidity");
                    Double txtWindSpeed = jsonObject.getDouble("wind_speed");

                    int txtCurrentId = CurrentWeatherObject.getInt("id");
                    String txtCurrentDesc = CurrentWeatherObject.getString("description");

                    temp_text_view.setText(String.format(Locale.getDefault(), "%.0f°", calculator.getDegreeCelcius(txtTemp)));
                    humidity_text_view.setText(String.format(Locale.getDefault(), "%d%%", txtHumidity));
                    wind_text_view.setText(String.format(Locale.getDefault(), getResources().getString(R.string.wind_unit_label), txtWindSpeed));
                    description_text_view.setText(txtCurrentDesc);
                    animation_view.setAnimation(AppUtil.getWeatherAnimation(txtCurrentId));
                    animation_view.playAnimation();

                    JSONArray jsonArray = response.getJSONArray("daily");
                    for (int i=0; i<jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);

                        String txtDt = object.getString("dt");

                        JSONObject jsonObjectTemp = object.getJSONObject("temp");
                        JSONArray jsonArrayWeather = object.getJSONArray("weather");
                        JSONObject DailyWeatherObject = jsonArrayWeather.getJSONObject(0);

                        Double txtMin = jsonObjectTemp.getDouble("min");
                        Double txtmax = jsonObjectTemp.getDouble("max");

                        int txtId = DailyWeatherObject.getInt("id");
                        String txtDesc = DailyWeatherObject.getString("description");

                        myDt.add(txtDt);
                        myDesc.add(txtDesc);

                        myMinTemp.add(txtMin);
                        mymaxTemp.add(txtmax);

                        myWeatherId.add(txtId);


                    }

                    GetListData();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("-*-*-*error ", String.valueOf(error));

            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(

                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT

        ));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);

        return dailyForecastPojoArrayList;
    }

    private void GetListData() {

        for (int i= 0; i<myDt.size(); i++){

            DailyForecastPojo dailyForecastPojo= new DailyForecastPojo();

            String txtDt = myDt.get(i);
            Double txtMin = myMinTemp.get(i);
            Double txtmax = mymaxTemp.get(i);
            int txtId = myWeatherId.get(i);
            String txtDesc = myDesc.get(i);

            dailyForecastPojo.setId(txtId);
            dailyForecastPojo.setDescription(txtDesc);


            dailyForecastPojo.setDt(calculator.getDate(Long.parseLong(txtDt)));

            dailyForecastPojo.setMinTemp(calculator.getDegreeCelcius(txtMin));
            dailyForecastPojo.setMaxTemp(calculator.getDegreeCelcius(txtmax));


            dailyForecastPojoArrayList.add(dailyForecastPojo);

        }

        dayForecastAdapter = new DayForecastAdapter(getApplicationContext(), dailyForecastPojoArrayList);
        recyclerView.setAdapter(dayForecastAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        getForecast(-1.261122, 36.780050, MainActivity.this);

//        getLocation();

    }

    private void getLocation() {

        CheckInternet checkInternet = new CheckInternet();
        boolean isConnected = checkInternet.isConnected(this);
        if (isConnected) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {


                try {

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, this);

                    if (locationLatitude.equals("37.421998333333335") && locationLongitude.equals("-122.08400000000002")){

                        editor.putString("latitude", "-1.261122");
                        editor.putString("longitude", "36.780050");
                        editor.commit();


                    }else {

                        editor.putString("latitude", locationLatitude);
                        editor.putString("longitude", locationLongitude);
                        editor.commit();

                    }

                    FetchFromPreference();



                }catch (SecurityException e){
                    e.printStackTrace();
                }


            } else {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            }

        } else {

            Toast.makeText(this, "You must have internet connecting to proceed..", Toast.LENGTH_SHORT).show();
        }

    }

    private void FetchFromPreference() {

        String txtLat = preferences.getString("latitude", null);
        String txtLong = preferences.getString("longitude", null);

        if (txtLat != null && txtLong !=null){

            System.out.println("-*-*-* " + txtLat);

//            Double Lat = Double.valueOf(txtLat);
//            Double Longt = Double.valueOf(txtLong);
//
            getForecast(-1.261122, 36.780050, MainActivity.this);

        }else {

            Toast.makeText(this, "Try getting the coordinates again", Toast.LENGTH_SHORT).show();


        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLocation();

            } else {

                Intent intent=new Intent(this,SplashActivity.class);
                startActivity(intent);
                finish();

            }

        }

    }

    @Override
    public void onLocationChanged(Location location) {

        locationText = location.getLatitude() + "," + location.getLongitude();
        locationLatitude = location.getLatitude() + "";
        locationLongitude = location.getLongitude() + "";
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please Enable GPS", Toast.LENGTH_SHORT).show();

    }
}
