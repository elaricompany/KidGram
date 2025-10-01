package org.telegram.elari.utils;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static org.telegram.messenger.AndroidUtilities.getActivity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import org.elarikg.messenger.R;
import org.telegram.elari.SharedPreferencesHelper;
import org.telegram.elari.elariapi.ElariApiClient;
import org.telegram.elari.elariapi.pojo.LocationBody;
import org.telegram.elari.elariapi.pojo.Response;
import org.telegram.elari.elariapi.pojo.location.LocationsRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;

public class LocationWorker extends Worker {
    private static final String TAG = "LocationWorker";
    private static final int NOTIFICATION_ID = 123;
    String tag = "";
    private FusedLocationProviderClient fusedLocationClient;

    public LocationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e(TAG, "LocationWorker started");
        try {
            // Для expedited work на Android 12+ показываем уведомление
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                setForegroundAsync(createForegroundInfo());
            }
            tag = getInputData().getString("tag");
            //getLastKnownLocation();
            requestFreshLocation();
            return Result.success();

        } catch (Exception e) {
            Log.e(TAG, "Error getting location", e);
            return Result.failure();
        }
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location permission not granted");
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        Log.d(TAG, "Location received: " + latitude + ", " + longitude);

                        // Здесь отправляем координаты на сервер или сохраняем локально
                        sendLocationToServer(latitude, longitude, tag);
                    } else {
                        Log.w(TAG, "Location is null, requesting fresh location");
                        requestFreshLocation();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get location", e);
                    requestFreshLocation();
                });
    }

    private void requestFreshLocation() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateDistanceMeters(0)
                .setMaxUpdates(1)
                .setWaitForAccurateLocation(true)
                .build();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //Log.d(TAG, "Fresh location: " + latitude + ", " + longitude);
                    sendLocationToServer(latitude, longitude, tag);
                }
                fusedLocationClient.removeLocationUpdates(this);
            }
        }, Looper.getMainLooper());
    }

    private void sendLocationToServer(double latitude, double longitude, String tag) {

        LocationBody body = new LocationBody();
        body.setLatitude(String.valueOf(latitude));
        body.setLongitude(String.valueOf(longitude));
//                                Random random = new Random();
//                                body.setLatitude("" + random.nextInt(60) + ".1545784");
//                                body.setLongitude("" + random.nextInt(60) + ".1545784");
        body.setTag(tag);

        String phoneNumber = new SharedPreferencesHelper(getApplicationContext()).getCustomValue(SharedPreferencesHelper.PHONE_NUMBER);
        body.setManId("+" + phoneNumber);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager bm = (BatteryManager) getApplicationContext().getSystemService(BATTERY_SERVICE);
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
        l.setLatitude(latitude);
        l.setLongitude(longitude);
        l.setLocationdate(getDate());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager bm = (BatteryManager) getApplicationContext().getSystemService(BATTERY_SERVICE);
            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            l.setBattery( Double.valueOf(batLevel));
        } else {
            l.setBattery(-1.0);
        }
        ArrayList<org.telegram.elari.elariapi.pojo.location.Location> tmpLocations = new ArrayList<org.telegram.elari.elariapi.pojo.location.Location>();
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

    private ForegroundInfo createForegroundInfo() {
        String channelId = "location_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Location Updates",NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(getApplicationContext().getString(R.string.get_geolocation))
                .setContentText(getApplicationContext().getString(R.string.get_geolocation_body))
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
        } else {
            return new ForegroundInfo(NOTIFICATION_ID, notification);
        }

        //return new ForegroundInfo(NOTIFICATION_ID, notification);
    }
}