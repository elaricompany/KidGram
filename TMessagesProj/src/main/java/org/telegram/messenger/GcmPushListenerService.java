/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package org.telegram.messenger;

import static org.telegram.messenger.AndroidUtilities.getActivity;
import static org.telegram.messenger.LocaleController.getString;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.WorkManager;

import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.elarikg.messenger.R;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.elari.C;
import org.telegram.elari.ElariUtils;
import org.telegram.elari.SharedPreferencesHelper;
import org.telegram.elari.elariapi.ElariApiClient;
import org.telegram.elari.elariapi.pojo.ChatAccess;
import org.telegram.elari.elariapi.pojo.LocationBody;
import org.telegram.elari.elariapi.pojo.location.LocationsRequest;
import org.telegram.elari.utils.ElariLocationSharingService;
import org.telegram.elari.utils.LocationWorker;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.EditWidgetActivity;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GcmPushListenerService extends FirebaseMessagingService {
    public static final String CMD_GET_CODE = "1";
    public static final String CMD_DEVICE_BINDE = "2";
    public static final String CMD_DEVICE_UNBINDE = "3";
    public static final String CHAT_STATE_CHANGE = "CHAT_STATE_CHANGE";
    public static final String BINDING_STATE_CHANGE = "BINDING_STATE_CHANGE";
    private LocalBroadcastManager broadcaster;
    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.e("Arttest", "onMessageReceived --->");
        //if(true){return;}
        //Log.e("Arttest", ":" + 2);
        String from = message.getFrom();
        Map<String, String> data = message.getData();
        long time = message.getSentTime();

        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("FCM received data: " + data + " from: " + from);
        }
        String chatId = null, access = null;
        if (message.getData() != null) {
            if (message.getData().containsKey("chat_id")) {
                chatId = message.getData().get("chat_id");
            }
            if (message.getData().containsKey("access")) {
                access = message.getData().get("access");
            }
            if((chatId != null) && (access != null)){
                if(DialogsActivity.accessList !=null){
                    for(ChatAccess cht : DialogsActivity.accessList){
                        if(chatId.equals("" + cht.getId())){
                            cht.setAccess(Short.parseShort(access));
                            Bundle extras = new Bundle();
                            extras.putString("chat_id", chatId);
                            Intent intent = new Intent(CHAT_STATE_CHANGE);
                            intent.putExtras(extras);
                            intent.setAction(CHAT_STATE_CHANGE);
                            broadcaster.sendBroadcast(intent);
                            break;
                        }
                    }
                }
                return;
            }
        }
        Log.e("ArtTest", "message.getData() - " + message.getData());
        if (message.getData().containsKey("type")) {
            String type = message.getData().get("type");
            if (type.equals("newLink")) {
                //showCodeNotification(message.getData().get("code"));
                //Get location!!!
            } else if (type.equals("location")) {
                Log.e("ArtTest", "cmd send location " + isPermissionGranted());
                if (isPermissionGranted()) {
//                    Intent serviceIntent = new Intent(this, ElariLocationSharingService.class);
//                    serviceIntent.putExtra("tag", message.getData().get("tag"));
//                    ContextCompat.startForegroundService(this, serviceIntent);

                    //sendLocation(message.getData().get("tag"));

                    Data inputData = new Data.Builder().putString("tag", message.getData().get("tag")).build();
                    OneTimeWorkRequest locationWorkRequest = new OneTimeWorkRequest.Builder(LocationWorker.class)
                                    .setInputData(inputData)
                                    .build();
                    WorkManager.getInstance(this).enqueue(locationWorkRequest);
                }
            }
        }else if(message.getData().containsKey("tlg_cmd")) {
            String cmd = message.getData().get("tlg_cmd"); if(cmd == null) {cmd = "";}

Log.e("ArtTest", "message cmd " + cmd);
            Intent intent = new Intent(BINDING_STATE_CHANGE);
            intent.setAction(BINDING_STATE_CHANGE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                intent.addCategory(Intent.CATEGORY_HOME);
//            }
            switch(cmd){
                case CMD_DEVICE_BINDE:
                    broadcaster.sendBroadcast(intent);
                    break;
                case CMD_DEVICE_UNBINDE:
                    broadcaster.sendBroadcast(intent);
                    break;
                default:
                    break;
            }
        } else {
            PushListenerController.processRemoteMessage(PushListenerController.PUSH_TYPE_FIREBASE, data.get("p"), time);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        AndroidUtilities.runOnUIThread(() -> {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("Refreshed FCM token: " + token);
            }
            ApplicationLoader.postInitApplication();
            PushListenerController.sendRegistrationToServer(PushListenerController.PUSH_TYPE_FIREBASE, token);
        });
    }

    private boolean isPermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
}
