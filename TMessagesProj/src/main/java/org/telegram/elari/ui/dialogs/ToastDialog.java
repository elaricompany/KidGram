package org.telegram.elari.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.elarikg.messenger.R;

import java.util.ArrayList;

public class ToastDialog extends Dialog {
    static ArrayList<ToastMessage> listOfMessage = new ArrayList<ToastMessage>();
    static ToastDialog dialog;
    private static final int TIME_TO_SHOEW = 2000;
    TextView text;
    public static ToastDialog getInstance(Context context){
        if(dialog == null){
            dialog = new ToastDialog(context);
        }else{
            dialog.dismiss();
        }
        return dialog;
    }

    void initView(){
        text = findViewById(R.id.text);
    }

    public ToastDialog(@NonNull Context context) {
        super(context);
        //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.toast_dialog);
        initView();
        //getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        getWindow().setDimAmount(0.0f);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void show(ToastMessage msg){
        //listOfMessage.add(msg);
        //startDialog();
        try {
            msg.a.runOnUiThread(() -> {
                dialog.text.setText(msg.s);
                dialog.text.setOnClickListener( v->{
                    Log.e("ArtTest", "onCLick");
                    dialog.dismiss();
                });
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    dialog.dismiss();
                }, TIME_TO_SHOEW);
                dialog.show();
            });
        }catch (Exception e){
            Toast.makeText(msg.a, msg.s, Toast.LENGTH_SHORT).show();
        }
    }

    void startDialog(){


//Log.e("ArtTest", "listOfMessage " + listOfMessage.size() + " | ");
//            if(listOfMessage.size() >0){
//                text.setText(listOfMessage.get(0).s);
//                final Handler handler = new Handler();
//                handler.postDelayed(() -> {
//Log.e("ArtTest", "listOfMessage " + listOfMessage.size() + " dismis ");
//                    dialog.dismiss();
//                    if(listOfMessage.size()>0){
//                        listOfMessage.remove(0);
//                        startDialog();
//                    }
//                }, TIME_TO_SHOEW);
//Log.e("ArtTest", "listOfMessage " + listOfMessage.size() + " show ");
//                dialog.show();
//            }

    }

    public static class ToastMessage {
        Activity a;
        String s;

        public ToastMessage(Activity a, String s){
            this.a = a;
            this.s = s;
        }
    }
}
