package org.telegram.elari.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;

import org.elarikg.messenger.R;

public class DeviceNotBoundDialog  extends Dialog {
    private TextView btnCancel;


    private void initView(){
        btnCancel      = findViewById(R.id.btn_cancel);
    }

    public DeviceNotBoundDialog(@NonNull Activity activity) {
        super(activity);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(android.R.color.transparent)));
            getWindow().setGravity(Gravity.BOTTOM);
        }catch (Error e){}

        setContentView(R.layout.device_notbound_dialog);
        initView();
        setCanceledOnTouchOutside(true);
        btnCancel.setOnClickListener(v->{
            dismiss();
        });
    }

    @Override public void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }
}
