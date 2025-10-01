package org.telegram.elari;

import static org.telegram.messenger.LocaleController.ensureImperialSystemInit;
import static org.telegram.messenger.LocaleController.getString;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.Toast;

import com.google.android.exoplayer2.util.Log;
import com.google.zxing.qrcode.decoder.Version;

import org.elarikg.messenger.R;
import org.telegram.elari.elariapi.ElariApiClient;
import org.telegram.elari.elariapi.pojo.ChatAccess;
import org.telegram.elari.elariapi.pojo.NewChatRequest;
import org.telegram.elari.elariapi.pojo.NewChatResponse;
import org.telegram.elari.ui.dialogs.RequestAccessDialog;
import org.telegram.elari.ui.dialogs.ToastDialog;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.DialogsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElariUtils {
    private static final float BITMAP_SCALE = 0.1f;
    private static final float BLUR_RADIUS = 8.5f;
    public static final String FIRST_KEY_TELEGRAM = "__";
    public static final String TOPIC_KIDRGAM = "___";

    public static int getAccess(long dialogId){
        int accesStatys = C.ACCESS_WAIT;
        if(DialogsActivity.accessList != null) {
            for (ChatAccess ca : DialogsActivity.accessList) {
                if (Math.abs(ca.getId()) == Math.abs(dialogId)) {
                    accesStatys = ca.getAccess();
                    break;
                }
            }
        }
        return accesStatys;
    }

    public static boolean isNeedAvatarBlur(){
        return false;
    }

    public static Bitmap blur(Context ctx, Bitmap image) {
        if(image == null){
            return null;
        }
        Bitmap outputBitmap = image;
        try {
            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(ctx);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return outputBitmap;
    }


    public static String hexmd5(String st, byte[] first) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        int size = st.getBytes().length + first.length;
        byte[] res = new byte[size];

        System.arraycopy(first, 0, res, 0, first.length);
        System.arraycopy(st.getBytes(), 0, res, first.length, st.getBytes().length);

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(res);
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }

    public static byte[] bihexmd5(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return digest;
    }


    public static void saveJsonToCache(Context context, String json, String fileName) {
        try {
            File cacheFile = new File(context.getCacheDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(cacheFile);
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readJsonFromCache(Context context, String fileName) {
        StringBuilder json = new StringBuilder();
        try {
            File cacheFile = new File(context.getCacheDir(), fileName);
            FileInputStream inputStream = new FileInputStream(cacheFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public interface iChatRequest{
        void requestWasSended();
    }
    public static boolean sendChatRequest(BaseFragment a, Context c, long dialogId, TLRPC.Chat chat, TLRPC.User user){
        return sendChatRequest(a, c, dialogId, chat, user, null);
    }
    public static boolean sendChatRequest(BaseFragment a, Context c, long dialogId, TLRPC.Chat chat, TLRPC.User user, iChatRequest l){
        boolean res = false;
        int accessElement = ElariUtils.getAccess(dialogId);
        if(accessElement != C.ACCESS_ALLOWED){
            res = true;
            String name = "";
            if(chat!=null){name = chat.title;}
            if(user!=null){
                if(user.first_name != null) {name = user.first_name;}
                if(user.last_name != null) {name += " " + user.last_name;}
            }
            BitmapDrawable photo = null;
            File path = null;

            if((chat!=null)&&(chat.photo!=null)){
                photo = chat.photo.strippedBitmap;
                path = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(chat.photo.photo_small, true);
            }
            if((user!=null)&&(user.photo!=null)){
                photo = user.photo.strippedBitmap;
                path = FileLoader.getInstance(UserConfig.selectedAccount).getPathToAttach(user.photo.photo_small, true);
            }


            new RequestAccessDialog(a.getParentActivity(), name, photo, path, dialogId,  new RequestAccessDialog.iBtnClick() {
            //new RequestAccessDialog(a, name, photo, dialogId,  new RequestAccessDialog.iBtnClick() {
                @Override public void onBtnSendRequestClick() {
                    new SharedPreferencesHelper(c).setCustomValue(SharedPreferencesHelper.SP_PREFIX_REQUEST_WAS_SEND + "" + dialogId, true);
                    final NewChatRequest body = new NewChatRequest();
                    if(user!=null) {
                        body.setManId("+" + ElariGlobalVar.currentUser.phone);
                        body.setDevType(C.DEVICE_TYPE);
                        String name = "";
                        if(user.first_name != null) {name += user.first_name; }
                        if(user.last_name != null) {name += " " + user.last_name; }
                        body.setName(name);
Log.e("ArtTest", "sendUserRequest "  + user.id);
                        body.setId(user.id);
                        body.setType("ChatTypePrivate");
                        body.setLang(Locale.getDefault().getLanguage().toUpperCase());
                        body.setFio(user.first_name + " " + user.last_name);
                        body.setPhone(user.phone);
                        //body.setLink(Servicer.getActiveUserName(user));
                        if(user.username!=null){body.setLink(user.username);}
                        body.setAccountId(ElariGlobalVar.currentUser.id);
                        TLRPC.TL_users_getFullUser req = new TLRPC.TL_users_getFullUser();
                        req.id = MessagesController.getInstance(UserConfig.selectedAccount).getInputUser(user);
                        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, (response, error) -> {
                            if (error == null) {
                                TLRPC.TL_users_userFull res = (TLRPC.TL_users_userFull) response;
                                TLRPC.UserFull userFull = res.full_user;
                                String about = userFull.about;
                                body.setDescription(about);
                            }else{
                                Log.e("Arttest", "Error  " + error.text );
                            }
                            sendRequestToServer(a.getParentActivity(),c,body, l);
                        });

                    }else if(chat != null){
                        body.setManId("+" + ElariGlobalVar.currentUser.phone);
                        body.setDevType(C.DEVICE_TYPE);
                        body.setName(chat.title);
Log.e("ArtTest", "sendChatRequest "  + chat.id + " | " + addDirtyToChatID(chat.id));
                        body.setId(addDirtyToChatID(chat.id));
                        body.setType("ChatTypeBasicGroup");
                        body.setLang(Locale.getDefault().getLanguage().toUpperCase());
                        if(chat.username!=null){body.setLink(chat.username);}
                        body.setAccountId(ElariGlobalVar.currentUser.id);

                        //Добавляем расширенную информацию
                        TLObject request;
                        if (ChatObject.isChannel(chat)) {
                            TLRPC.TL_channels_getFullChannel req = new TLRPC.TL_channels_getFullChannel();
                            req.channel = MessagesController.getInputChannel(chat);
                            request = req;
                        } else {
                            TLRPC.TL_messages_getFullChat req = new TLRPC.TL_messages_getFullChat();
                            req.chat_id = dialogId;
                            request = req;
                        }
                        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(request, (response, error) -> {
                            if (error == null) {
                                TLRPC.TL_messages_chatFull res = (TLRPC.TL_messages_chatFull) response;
                                String about = res.full_chat.about;
                                int participantsCount = res.full_chat.participants_count;
                                body.setDescription(about);
                                body.setMemberCount(participantsCount);
                            }else{
                                Log.e("Arttest", "Error  " + error.text );
                            }
                            sendRequestToServer(a.getParentActivity(),c,body, l);
                        });

                        //Входим в группу
                        if (ChatObject.isChannel(chat) && !(chat instanceof TLRPC.TL_channelForbidden)) {
                            a.getMessagesController().addUserToChat(chat.id, a.getUserConfig().getCurrentUser(), 0, null, null, () -> {});
                        }
                    }
                }
            }).show();
        }
        return res;
    }

    public static void sendRequestToServer(Activity a, Context c, NewChatRequest body, iChatRequest l){
        Call<NewChatResponse> call = ElariApiClient.getInstance().getApiService().sendChatAddingRequest(body);
        call.enqueue(new Callback<NewChatResponse>() {
            @Override
            public void onResponse(Call<NewChatResponse> call, Response<NewChatResponse> response) {
                if (response.body() != null) {
                    if(response.body().getError() != 0){
                        ToastDialog.getInstance(a).show(new ToastDialog.ToastMessage(a, "" + response.body().getError() + " - " + response.body().getError()));
                    }else{
                        try {
                            new SharedPreferencesHelper(c).setCustomValue(SharedPreferencesHelper.SP_PREFIX_ACCES_ELEMENT + "" + body.getId(), true);
                            if(l!=null){l.requestWasSended();}
                            //Toast.makeText(c, getString(R.string.request_was_send), Toast.LENGTH_LONG).show();
                            ToastDialog.getInstance(a).show(new ToastDialog.ToastMessage(a, getString(R.string.request_was_send)));
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<NewChatResponse> call, Throwable t) {
                //Toast.makeText(c, getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
                ToastDialog.getInstance(a).show(new ToastDialog.ToastMessage(a, getString(R.string.unknown_error)));
            }
        });
    }

    public static long addDirtyToChatID(long id){
        /*if (!String.valueOf(id).startsWith("-100")) {
            String idStr = String.valueOf(id);
            //String processedIdStr = "-100" + idStr.substring(1);
            String processedIdStr = "-100" + idStr;
            try {
                return Long.parseLong(processedIdStr);
            } catch (NumberFormatException e) {
                return id;
            }
        }*/
        return id;
    }
    public static long clearDirtyChatID(long id){
        /*if (id < 0) {
            String idStr = String.valueOf(id);
            if (idStr.startsWith("-100")) {
                try {
                    String revertedIdStr = idStr.substring(4);
                    return Long.parseLong(revertedIdStr);
                } catch (NumberFormatException e) {
                    return id;
                }
            }
        }*/
        return id;
    }
}
