package org.telegram.elari.ui.activity;

import static org.telegram.messenger.LocaleController.getString;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.elarikg.messenger.R;
import org.telegram.elari.C;
import org.telegram.elari.ElariGlobalVar;
import org.telegram.elari.SharedPreferencesHelper;
import org.telegram.elari.elariapi.ElariApiClient;
import org.telegram.elari.elariapi.pojo.CheckBoundRequest;
import org.telegram.elari.elariapi.pojo.GetBoundCodeRequest;
import org.telegram.elari.elariapi.pojo.GetBoundCodeResponse;
import org.telegram.elari.elariapi.pojo.GetBoundingCodeResponse;
import org.telegram.elari.elariapi.pojo.NewChatRequest;
import org.telegram.elari.elariapi.pojo.NewChatResponse;
import org.telegram.elari.ui.dialogs.DeviceNotBoundDialog;
import org.telegram.elari.ui.dialogs.RequestAccessDialog;
import org.telegram.elari.ui.dialogs.ToastDialog;
import org.telegram.messenger.GcmPushListenerService;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.LogoutActivity;
import org.telegram.ui.PremiumPreviewFragment;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceNotBoundActivity extends AppCompatActivity {
    private TextView btnRefreshCode;
    private ProgressBar loading;
    private TextView instruction;
    private TextView code, codeBlink;
    private TextView btnNext;
    private TextView btnLogout;

    boolean isFirstCheckBound = true;
    private String userPhone;
    private Long userId;

    TextView digit1,digit2,digit3,digit4,digit5,digit6;

    void initViews() {
        //bottomNavigationView = v.findViewById(R.id.bottom_navigation2);
        loading        = findViewById(R.id.loading);
        instruction    = findViewById(R.id.instruction_for_parents);
        code           = findViewById(R.id.code);
        codeBlink      = findViewById(R.id.code_blink);
        btnNext        = findViewById(R.id.btn_next);
        btnRefreshCode = findViewById(R.id.refreshcode);
        btnLogout      = findViewById(R.id.btn_logout);
        digit1         = findViewById(R.id.digit_1);
        digit2         = findViewById(R.id.digit_2);
        digit3         = findViewById(R.id.digit_3);
        digit4         = findViewById(R.id.digit_4);
        digit5         = findViewById(R.id.digit_5);
        digit6         = findViewById(R.id.digit_6);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((bindingReciver), new IntentFilter(GcmPushListenerService.BINDING_STATE_CHANGE));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_not_bound);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars()| WindowInsetsCompat.Type.displayCutout());
            v.setPadding(bars.left,bars.top,bars.right,bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        userPhone = getIntent().getStringExtra("userPhone");
        if(!userPhone.contains("+")){ userPhone = "+" + userPhone;}
        userId = getIntent().getLongExtra("userPhone", 0);

        initViews();

        loading.setVisibility(View.GONE);
        checkBound();
        instruction.setPaintFlags(instruction.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        instruction.setOnClickListener(v->{
            String URL_TO_INSTRUCTION = getResources().getString(R.string.url_to_instruction);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_TO_INSTRUCTION));
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v->{
            BaseFragment lastFragment = LaunchActivity.getSafeLastFragment();
            if (lastFragment == null) return;
            MessagesController.getInstance(UserConfig.selectedAccount).performLogout(1);
            finish();
        });
        btnNext.setOnClickListener(v->{checkBound(true);});
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        code.setTextSize(width > 800 ? 32 : 27);
        btnRefreshCode.setOnClickListener(v->{getBoundCode();});
    }

    private final BroadcastReceiver bindingReciver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            com.google.android.exoplayer2.util.Log.e("ArtTest", "NotBound Activite Reciver call" );
            checkBound();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bindingReciver != null) {
            LocalBroadcastManager.getInstance(getBaseContext()).unregisterReceiver(bindingReciver);
        }
    }

    private void getBoundCode(){
        beforeLoadData();

        //TdApi.User me = (TdApi.User) object;
        final GetBoundCodeRequest body = new GetBoundCodeRequest();
        body.setManId(userPhone);
        body.setDevType(C.DEVICE_TYPE);
        body.setAccountId(userId);
        body.setApiKey("debug");
        Call<GetBoundingCodeResponse> call = ElariApiClient.getInstance().getApiService().getBoundCode(body);
        call.enqueue(new Callback<GetBoundingCodeResponse>() {
            @Override public void onResponse(Call<GetBoundingCodeResponse> call, Response<GetBoundingCodeResponse> response) {
                runOnUiThread(()->{
                    try {
                        if((response.body()!=null)&&(response.body().getCode()!=null)) {
                            afterLoadData();
                            String codeStr = response.body().getCode();
                            code.setText(""+ codeStr);
                            if(codeStr.length() >= 6){
                                digit1.setText(String.valueOf(codeStr.charAt(0)));
                                digit2.setText(String.valueOf(codeStr.charAt(1)));
                                digit3.setText(String.valueOf(codeStr.charAt(2)));
                                digit4.setText(String.valueOf(codeStr.charAt(3)));
                                digit5.setText(String.valueOf(codeStr.charAt(4)));
                                digit6.setText(String.valueOf(codeStr.charAt(5)));
                            }else{
                                errorLoadData();
                            }
                        }else if(response.errorBody()!=null){
                            errorLoadData();
                        }
                    }catch (Exception e){
                        errorLoadData();
                    }
                });
            }

            @Override public void onFailure(Call<GetBoundingCodeResponse> call, Throwable t) {
                errorLoadData();
            }
        });
    }
    private void checkBound(){
        checkBound(false);
    }
    private void checkBound(boolean isNeedShowAlert){
        final CheckBoundRequest body = new CheckBoundRequest();
        //body.setManId(PhoneFormat.getInstance().format("+" + user2.phone));
        body.setManId(userPhone);
        body.setDevType(C.DEVICE_TYPE);
        body.setAccountId(userId);
        Call<GetBoundCodeResponse> call = ElariApiClient.getInstance().getApiService().checkBound(body);
        call.enqueue(new Callback<GetBoundCodeResponse>() {
            @Override public void onResponse(Call<GetBoundCodeResponse> call, retrofit2.Response<GetBoundCodeResponse> response) {
                runOnUiThread(()->{loading.setVisibility(View.GONE);});

                if (response.body() != null) {
//Log.e("ArtTest", "response " +  response.body().getError() + " | " + response.body().getDevid() + " | " + response.body().toString());
                    if (response.body().getError() == 1){
                        //Если не аккаунт привязан к SF
                        runOnUiThread(()->{
                            if(response.body().getDevid() > 0){
                                //if(!isFirstCheckBound){
                                    //new LoginFragmentsOkDialog(getContext(), getResources().getString(R.string.cant_bound_kg), null).show();
                                //}
                                if(isNeedShowAlert) {new DeviceNotBoundDialog(DeviceNotBoundActivity.this).show();}
                                getBoundCode();
                            }else{
                                //navController.navigate(R.id.unknownerror_fragment);
                                errorLoadData();
                            }
                        });
                    }else if (response.body().getError() == 2){
                        //navController.navigate(R.id.nosubscription_fragment);
                        //Если подписки нет, то сейчасэто не важно
                        //Log.e("ArtTest", "finish DEVICE not Bound");
                        BaseFragment lastFragment = LaunchActivity.getSafeLastFragment();
                        if (lastFragment != null){lastFragment.presentFragment(new DialogsActivity(null), true);}
                        finish();
                        //bottomNavigationView.setVisibility(View.VISIBLE);
                        //navController.navigate(R.id.main_chat_fragment);
                    }else{
                        //bottomNavigationView.setVisibility(View.VISIBLE);
                      //  navController.navigate(R.id.main_chat_fragment);
//                        Log.e("ArtTest", "finish DEVICE not Bound");
                        BaseFragment lastFragment = LaunchActivity.getSafeLastFragment();
                        if (lastFragment != null){lastFragment.presentFragment(new DialogsActivity(null), true);}
                        finish();
                    }
                }else{
                    //navController.navigate(R.id.unknownerror_fragment);
                    errorLoadData();
                }
                isFirstCheckBound = false;
            }

            @Override public void onFailure(Call<GetBoundCodeResponse> call, Throwable t) {
               // navController.navigate(R.id.unknownerror_fragment);
            }
        });
    }

    private void beforeLoadData(){
        startLoadingAnim(codeBlink, code);
        startLoadingAnim(digit1);digit1.setText("?");
        startLoadingAnim(digit2);digit2.setText("?");
        startLoadingAnim(digit3);digit3.setText("?");
        startLoadingAnim(digit4);digit4.setText("?");
        startLoadingAnim(digit5);digit5.setText("?");
        startLoadingAnim(digit6);digit6.setText("?");
    }
    private void afterLoadData(){
        stopLoadingAnim(code, codeBlink);
        stopLoadingAnim(digit1);
        stopLoadingAnim(digit2);
        stopLoadingAnim(digit3);
        stopLoadingAnim(digit4);
        stopLoadingAnim(digit5);
        stopLoadingAnim(digit6);
    }
    private void errorLoadData(){
        stopLoadingAnim(code, codeBlink);
        code.setText("?????");
        digit1.setText("?");
        digit2.setText("?");
        digit3.setText("?");
        digit4.setText("?");
        digit5.setText("?");
        digit6.setText("?");
    }

    private void startLoadingAnim(View vToShow, View vToHide) {
//        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.loading_anim);
//        animation.setRepeatMode(Animation.RESTART);
//        vToShow.startAnimation(animation);
//        vToShow.setVisibility(View.VISIBLE);
//        if(vToHide!=null){vToHide.setVisibility(View.INVISIBLE);}
    }
    private void startLoadingAnim(View v){
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.loading_anim);
        animation.setRepeatMode(Animation.RESTART);
        v.startAnimation(animation);
    }

    private void stopLoadingAnim(View vToShow, View vToHide) {
//        if(vToHide!=null){vToHide.clearAnimation();}
//        vToShow.setVisibility(View.VISIBLE);
//        if(vToHide!=null){vToHide.setVisibility(View.INVISIBLE);}
    }

    private void stopLoadingAnim(View v) {
        if(v!=null){v.clearAnimation();}
    }
}


