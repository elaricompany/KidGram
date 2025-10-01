package org.telegram.elari.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;

import org.elarikg.messenger.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;

    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS;



    private static List<String> REQUEST_PERMISSION_LIST = new ArrayList<String>(Arrays.asList(new String[]{
            PERMISSION_RECORD_AUDIO,
            PERMISSION_SEND_SMS,
            PERMISSION_GET_ACCOUNTS,
            PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE,
            PERMISSION_CAMERA,
            PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION,
            PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_READ_MEDIA_IMAGES,
            PERMISSION_WRITE_EXTERNAL_STORAGE
    }));

    public interface PermissionGrant {
        void onPermissionGranted(int requestCode);
    }


    public static void requestMultiPermissions(final Activity activity, String[] permissions, PermissionGrant grant) {
        Pair<ArrayList<String>, ArrayList<String>> pair = getNoGrantedPermission(activity, permissions, false);
        final List<String> permissionsList = pair.first;//需要申请权限的集合
        final List<String> shouldRationalePermissionsList = pair.second;

        if (permissionsList.size() > 0) {//需要申请权限的集合
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]),CODE_MULTI_PERMISSION);
        } else if (shouldRationalePermissionsList.size() > 0) {
            showMessageOKCancel(activity, "should open those permission",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(activity, shouldRationalePermissionsList.toArray(new String[shouldRationalePermissionsList.size()]),
                                    CODE_MULTI_PERMISSION);
                        }
                    });
        } else {
            grant.onPermissionGranted(CODE_MULTI_PERMISSION);
        }
    }

    private static void showMessageOKCancel(final Activity context, String message, View.OnClickListener okListener) {
//        new CustomAlertDialog(context)
//                .setTitleVisible(true)
//                .setCusTitle(R.string.grant_permissions_header_text)
//                .setMsg(message)
//                .setPositiveButton(R.string.ok, okListener)
//                .setNegativeButton(R.string.cancel, null)
//                .show();
    }

    /**
     * @param activity
     * @param requestCode  Need consistent with requestPermission
     * @param permissions
     * @param grantResults
     */
    public static void requestPermissionsResult(final Activity activity, final int requestCode, @NonNull String[] permissions,
                                                @NonNull int[] grantResults, PermissionGrant permissionGrant) {

        if (activity == null) {
            return;
        }

        if (requestCode == CODE_MULTI_PERMISSION) {
            requestMultiResult(activity, permissions, grantResults, permissionGrant);
            return;
        }

        if (requestCode < 0 || requestCode >= REQUEST_PERMISSION_LIST.size()) {
            Toast.makeText(activity, "illegal requestCode:" + requestCode, Toast.LENGTH_SHORT).show();
            return;
        }
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            int checkOp = 0;
            AppOpsManager appOpsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
            checkOp = appOpsManager.checkOp(AppOpsManager.OPSTR_WRITE_CONTACTS,
                    android.os.Process.myUid(), activity.getPackageName());
            if (checkOp == AppOpsManager.MODE_IGNORED) {
                openSettingActivity(activity, getPermissionsHint(activity, requestCode));
            }

            //TODO success, do something, can use callback
            permissionGrant.onPermissionGranted(requestCode);

        } else {
            //TODO hint user this permission function
            openSettingActivity(activity, getPermissionsHint(activity, requestCode));
        }
    }

    private static void requestMultiResult(Activity activity, String[] permissions, int[] grantResults, PermissionGrant permissionGrant) {
        if (activity == null) {
            return;
        }
        //TODO
        Map<String, Integer> perms = new HashMap<>();

        ArrayList<String> notGranted = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            perms.put(permissions[i], grantResults[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permissions[i]);
            }
        }

        if (notGranted.size() == 0) {
            //Toast.makeText(activity, "all permission success" + notGranted, Toast.LENGTH_SHORT).show();
            permissionGrant.onPermissionGranted(CODE_MULTI_PERMISSION);
        } else {
            openSettingActivity(activity, getMutilPermissionsHint(activity, notGranted));
        }
    }

    private static void openSettingActivity(final Activity activity, String message) {

        showMessageOKCancel(activity, message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
    }

    private static String getPermissionsHint(Context context, int requestCode) {
        return "Please open this permission";
    }

    private static String getMutilPermissionsHint(Context context, List<String> notGranted) {
        return "Those permission need granted!";
    }

    public static String toString(final Object[] array, final char token, final boolean include_space) {
        final StringBuilder builder = new StringBuilder();
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            final String id_string = String.valueOf(array[i]);
            if (id_string != null) {
                if (i > 0) {
                    builder.append(include_space ? token + " " : token);
                }
                builder.append(id_string);
            }
        }
        return builder.toString();
    }

    /**
     * @param activity
     * @param isShouldRationale true: return no granted and shouldShowRequestPermissionRationale permissions, false:return no granted and !shouldShowRequestPermissionRationale
     * @return
     */
    public static Pair<ArrayList<String>, ArrayList<String>> getNoGrantedPermission(Activity activity, String[] permissions, boolean isShouldRationale) {

        ArrayList<String> noGrantedPermissions = new ArrayList<>(Arrays.asList(permissions));
        ArrayList<String> shouldRationalePermissions = new ArrayList<>();

        for (int i = 0; i < noGrantedPermissions.size(); i++) {
            String requestPermission = noGrantedPermissions.get(i);

            //TODO checkSelfPermission
            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);//checkSelfPermission返回值0表示具有该权限
            } catch (RuntimeException e) {
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {//没有该权限
                if (isShouldRationale && ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    shouldRationalePermissions.add(requestPermission);
                }
            } else {
                noGrantedPermissions.remove(i);
            }
        }

        return new Pair<>(noGrantedPermissions, shouldRationalePermissions);
    }

    public static Pair<ArrayList<String>, ArrayList<String>> getNoGrantedPermission(Activity activity, boolean isShouldRationale) {

        ArrayList<String> noGrantedPermissions = new ArrayList<>();
        ArrayList<String> shouldRationalePermissions = new ArrayList<>();

        for (int i = 0; i < REQUEST_PERMISSION_LIST.size(); i++) {
            String requestPermission = REQUEST_PERMISSION_LIST.get(i);

            //TODO checkSelfPermission
            int checkSelfPermission = -1;
            try {
                checkSelfPermission = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (RuntimeException e) {
            }

            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                noGrantedPermissions.add(requestPermission);

                if (isShouldRationale && ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    shouldRationalePermissions.add(requestPermission);
                }
            }
        }
        return new Pair<>(noGrantedPermissions, shouldRationalePermissions);
    }
}
