package org.telegram.elari.ui.activity;


import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.util.Log;

import org.elarikg.messenger.R;
import org.telegram.elari.C;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;


public class QRLoginActivity  extends AppCompatActivity {
    protected int currentAccount = -1;
    TextView btnPhonNumberLogin;

    void initViews() {
        btnPhonNumberLogin = findViewById(R.id.btn_phone_number_login);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_login);
        initViews();
        exportLoginTokenAndGenerateQR();

    }

    private void exportLoginTokenAndGenerateQR() {
        TLRPC.TL_auth_exportLoginToken req = new TLRPC.TL_auth_exportLoginToken();
        req.api_id = C.apiId;
        req.api_hash = C.apiHash;
        req.except_ids = new ArrayList<>(); // можно оставить пустым
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, (response, error) -> {


            if (error != null) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Ошибка при получении токена", Toast.LENGTH_SHORT).show()
                );
                return;
            }
            if (response instanceof TLRPC.TL_auth_loginToken) {
                TLRPC.TL_auth_loginToken token = (TLRPC.TL_auth_loginToken) response;
                byte[] tokenBytes = token.token;
                String base64Token = Base64.encodeToString(tokenBytes, Base64.NO_WRAP);
                String loginUrl = "tg://login?token=" + base64Token;
            }

        });
    }


    private void startLoadingAnim(View vToShow, View vToHide) {
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.loading_anim);
        animation.setRepeatMode(Animation.RESTART);
        vToShow.startAnimation(animation);
        vToShow.setVisibility(View.VISIBLE);
        vToHide.setVisibility(View.INVISIBLE);
    }

    private void stopLoadingAnim(View vToShow, View vToHide) {
        vToHide.clearAnimation();
        vToShow.setVisibility(View.VISIBLE);
        vToHide.setVisibility(View.INVISIBLE);
    }




}


