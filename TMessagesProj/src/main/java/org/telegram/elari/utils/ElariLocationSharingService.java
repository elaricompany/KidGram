package org.telegram.elari.utils;

import static org.telegram.messenger.AndroidUtilities.getActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.elarikg.messenger.R;
import org.telegram.elari.SharedPreferencesHelper;
import org.telegram.elari.elariapi.ElariApiClient;
import org.telegram.elari.elariapi.pojo.LocationBody;
import org.telegram.elari.elariapi.pojo.Response;
import org.telegram.elari.elariapi.pojo.location.Location;
import org.telegram.elari.elariapi.pojo.location.LocationsRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class ElariLocationSharingService extends Service {

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, createNotification());
        String tag = intent.getStringExtra("tag");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d("LocationService", "Latitude: " + latitude + ", Longitude: " + longitude);
                        sendLocation(location, tag);
                    }
                    stopSelf();
                })
                .addOnFailureListener(e -> {
                    Log.e("LocationService", "Failed to get location", e);
                    stopSelf();
                });

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification() {
        String channelId = "location_channel";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Geo Channel", NotificationManager.IMPORTANCE_LOW);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }

        return new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Получение геолокации")
                .setContentText("Сервис определяет ваше местоположение")
                .setSmallIcon(R.drawable.ic_kidgram_logo)
                .build();
    }


    void sendLocation(android.location.Location location, String tag){
        LocationBody body = new LocationBody();
        body.setLatitude(String.valueOf(location.getLatitude()));
        body.setLongitude(String.valueOf(location.getLongitude()));
//                                Random random = new Random();
//                                body.setLatitude("" + random.nextInt(60) + ".1545784");
//                                body.setLongitude("" + random.nextInt(60) + ".1545784");
        body.setTag(tag);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesHelper.PHONE_NUMBER_KEY, MODE_PRIVATE);
        String phoneNumber = new SharedPreferencesHelper(getActivity()).getCustomValue(SharedPreferencesHelper.PHONE_NUMBER);
        body.setManId("+" + phoneNumber);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            body.setBattery(String.valueOf(batLevel));
        } else {
            body.setBattery(String.valueOf(-1));
        }
//Log.e("ArtTest", "body. " + body.getTag()  + " "  + body.getLatitude() + " " + body.getLongitude() + " | " + body.getManId());
        //Старый метод, непонятно зачем он. Удаляем!!!!
//        ElariApiClient.getInstance()
//                .getApiService()
//                .sendLocation(body)
//                .enqueue(new Callback<Response>() {
//                    @Override
//                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
////Log.e("ArtTest", "response " + response.body());
//                        if (response.isSuccessful() && response.body() != null) {
//                            android.util.Log.d("fbs", "Location " + response.body().toString());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<org.telegram.elari.elariapi.pojo.Response> call, Throwable t) {
//                        android.util.Log.d("Location", t.toString());
//                    }
//                });

        // 2-й способ отправки геолокации с сохранением в историю
        //-------------------->
        final LocationsRequest body2 = new LocationsRequest();
        org.telegram.elari.elariapi.pojo.location.Location l = new org.telegram.elari.elariapi.pojo.location.Location();
        l.setLatitude(location.getLatitude());
        l.setLongitude(location.getLongitude());
        l.setLocationdate(getDate());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            l.setBattery(Double.valueOf(batLevel));
        } else {
            l.setBattery(-1.0);
        }
        ArrayList<Location> tmpLocations = new ArrayList<org.telegram.elari.elariapi.pojo.location.Location>();
        tmpLocations.add(l);
        body2.setLocations(tmpLocations);
        body2.setTag(tag);
        //body2.setAccountId();
        body2.setManId("+" + phoneNumber);
        Call<org.telegram.elari.elariapi.pojo.Response> call = ElariApiClient.getInstance().getApiService().setCurrentLocation(body2);
        call.enqueue(new Callback<org.telegram.elari.elariapi.pojo.Response>() {
            @Override public void onResponse(Call<org.telegram.elari.elariapi.pojo.Response> call, retrofit2.Response<Response> response) {
                if (response.body() != null) {

                }
            }
            @Override public void onFailure(Call<org.telegram.elari.elariapi.pojo.Response> call, Throwable t) {}
        });
    }

    private static String getDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssXX");
        return format.format(date);
    }
}