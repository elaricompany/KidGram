package org.telegram.elari.elariapi;

import org.telegram.elari.C;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ElariApiClient {

    private static ElariApiClient elariApiClient;
    private final Retrofit retrofit;
    private HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    private ElariApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        try {
            TLSSocketFactory tlsSocketFactory = new TLSSocketFactory();
            if (tlsSocketFactory.getTrustManager() != null) {
                okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(tlsSocketFactory, tlsSocketFactory.getTrustManager())
                        .addInterceptor(logging.setLevel((C.IS_DEBUG) ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                        //.addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build();
            }
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(C.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ElariApiClient getInstance() {
        if (elariApiClient == null) {
            elariApiClient = new ElariApiClient();
        }
        return elariApiClient;
    }

    public ElariApiInterface getApiService() {
        return retrofit.create(ElariApiInterface.class);
    }
}
