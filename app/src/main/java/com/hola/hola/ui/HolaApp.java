package com.hola.hola.ui;

import android.app.Application;

import com.hola.hola.BuildConfig;
import com.hola.hola.R;
import com.hola.hola.REST.HolaRESTClient;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HolaApp extends Application {
  private static final boolean premium = BuildConfig.IS_PREMIUM;



  @Override public void onCreate() {
    super.onCreate();
    OkHttpClient httpClient = HolaRESTClient.init();
    Picasso picasso = new Picasso.Builder(getApplicationContext())
            .downloader(new OkHttp3Downloader(httpClient))
            .build();
    Picasso.setSingletonInstance(picasso);
//    Locale.setDefault(Locale.ENGLISH);
  }

  public static boolean isPremium(){
    return premium;
  }

  public static void requestCsrfUpdate(OnTokenUpdatedListener listener) {
    HolaRESTClient.get().getCSRF().enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        HolaRESTClient.setCsrfToken(response.body());
        if(listener != null) listener.onUpdated();
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
      }
    });
  }
  public interface OnTokenUpdatedListener{
    void onUpdated();
  }
}
