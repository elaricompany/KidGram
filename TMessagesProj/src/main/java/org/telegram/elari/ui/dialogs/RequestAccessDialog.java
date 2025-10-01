package org.telegram.elari.ui.dialogs;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.util.Log;

import org.elarikg.messenger.R;
import org.telegram.elari.ElariUtils;
import org.telegram.elari.SharedPreferencesHelper;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;

import java.io.File;

public class RequestAccessDialog extends Dialog {
    private TextView btnCancel;
    private TextView btnSendRequest;
    private TextView text2;
    private ImageView avatar;

    iBtnClick l;
    private void initView(){
        btnCancel      = findViewById(R.id.btn_cancel);
        btnSendRequest = findViewById(R.id.btn_send_request);
        avatar         = findViewById(R.id.avatar);
        text2          = findViewById(R.id.text_2);
    }

    public interface iBtnClick{
        void onBtnSendRequestClick();
    }
    public RequestAccessDialog(@NonNull Activity activity, String name, BitmapDrawable strippedBitmap, File fullAvatar, long dialogId, iBtnClick lisener) {
        super(activity);
        this.l = lisener;

        boolean ifRequestWasSend = false;
        ifRequestWasSend = new SharedPreferencesHelper(activity).getBooleanCustomValue(SharedPreferencesHelper.SP_PREFIX_REQUEST_WAS_SEND + "" + dialogId, false);

        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
            getWindow().setGravity(Gravity.BOTTOM);
        }catch (Error e){}

        setContentView(R.layout.request_access_dialog);
        initView();
        setCanceledOnTouchOutside(true);
        btnSendRequest.setOnClickListener(v->{
            if(l!=null) {l.onBtnSendRequestClick();}
            dismiss();
        });
        btnCancel.setOnClickListener(v->{
            dismiss();
        });
        CharSequence str = "";
        str = AndroidUtilities.replaceTags(activity.getString(ifRequestWasSend ? R.string.request_access_dialog_text_2_2 : R.string.request_access_dialog_text_2, name));
        text2.setText(str);

        if((strippedBitmap==null)&&(fullAvatar==null)){
            Glide.with(activity.getApplicationContext())
                    .load(R.drawable.ic_lock_2)
                    .error(R.drawable.ic_lock_2)
                    .into(avatar);
        }else{
            if (ElariUtils.isNeedAvatarBlur()) {
                if (strippedBitmap != null) {
                    Glide.with(activity.getApplicationContext())
                            .load(strippedBitmap.getBitmap())
                            .error(R.drawable.ic_lock_2)
                            .circleCrop()
                            .into(avatar);
                }
            } else {
                if (fullAvatar != null) {
                    Glide.with(activity.getApplicationContext())
                            .load(fullAvatar.getAbsolutePath())
                            .error(R.drawable.ic_lock_2)
                            .circleCrop()
                            .into(avatar);
                }
            }
        }
    }

    @Override public void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
