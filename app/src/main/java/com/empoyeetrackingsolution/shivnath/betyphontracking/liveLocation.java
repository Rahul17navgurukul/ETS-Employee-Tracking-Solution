package com.empoyeetrackingsolution.shivnath.betyphontracking;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.empoyeetrackingsolution.shivnath.betyphontracking.model.AttendenceModel;
import com.empoyeetrackingsolution.shivnath.betyphontracking.model.locationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class liveLocation extends Service implements LocationListener {
    private Timer timer = new Timer();
    LocationManager locationManager;
    Context context;
    double locLati;
    double locLongi;
    Realm realm;
    private Object Runnable;


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

//        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//
//        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String value = preferences.getString("user_id", "0");
        final String  currentTime = preferences.getString("currentDate","1");


        scheduler(value,currentTime);

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    private void scheduler(final String value, final String currentTime) {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Testing"+"Working");
                System.out.println("Location Posted SuccessFull");
                System.out.println("lat+long"+locLati+">>"+locLongi);



                locationPostion(value,currentTime);
            }
        }, 0, 60*1000);//1 Minutes
    }

    private void locationPostion(String value, String currentTime) {

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        JSONArray payload = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", value);
            jsonObject.put("latitude", locLati);
            jsonObject.put("longitude", locLongi);
            jsonObject.put("creation_datetime", currentTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        payload.put(jsonObject);


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON, payload.toString());
        Request request = new Request.Builder()
                .url("http://159.65.145.32/api/geo/tracking/")
                .method("POST", body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String json = response.body().string();
                    System.out.println("Location Posting Success >>> " + json);

                }
            }
        });

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {

        locLati = location.getLatitude();
        locLongi = location.getLongitude();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

//            locationText.setText(locationText.getText() + "\n" + addresses.get(0).getAddressLine(0) + ", " +
//                            addresses.get(0).getAddressLine(1));

//                    +", "+addresses.get(0).getAddressLine(2)

        } catch (Exception e) {

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

//        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(getApplicationContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();


    }


}
