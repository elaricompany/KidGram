package org.telegram.elari.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import org.elarikg.messenger.R;
import org.telegram.elari.SharedPreferencesHelper;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CompatDrawable;

public class BlockChatDialog extends Dialog {
    private TextView btnCancel;
    private TextView btnSendRequest;
    private TextView text2;
    private ImageView avatar;
    private ConstraintLayout background;

    iBlockChatDialog l;
    private void initView(){
        btnCancel      = findViewById(R.id.btn_cancel);
        btnSendRequest = findViewById(R.id.btn_send_request);
        avatar         = findViewById(R.id.avatar);
        text2          = findViewById(R.id.text_2);
        background     = findViewById(R.id.background);
    }

    public interface iBlockChatDialog{
        void onBtnSendRequestClick();
        void onBtnCancelClick();
    }
    public BlockChatDialog(@NonNull Activity activity, String name, BitmapDrawable strippedBitmap, long dialogId, iBlockChatDialog lisener) {
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

        setContentView(R.layout.block_chat_dialog);
        initView();
        boolean isDark =  Theme.isCurrentThemeDark();
        if (isDark) {
            background.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.content_blur_dark));
        } else {
            background.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.content_blur_light));
        }

        setCanceledOnTouchOutside(true);
        btnSendRequest.setOnClickListener(v->{
            if(l!=null) {
                l.onBtnSendRequestClick();
            }
            dismiss();
        });
        btnCancel.setOnClickListener(v->{
            if(l!=null) {l.onBtnCancelClick();}
            dismiss();
        });
        CharSequence str = "";
        str = AndroidUtilities.replaceTags(activity.getString(ifRequestWasSend ? R.string.request_access_dialog_text_2_2 : R.string.request_access_dialog_text_2, name));
        text2.setText(str);

        if(strippedBitmap!=null) {
            Glide.with(activity.getApplicationContext())
                    .load(strippedBitmap.getBitmap())
                    .circleCrop()
                    .into(avatar);
        }
    }

    @Override public void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onBackPressed() {l.onBtnCancelClick();}
}
