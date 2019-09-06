package com.hola.hola.REST;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HolaRESTClient {

    private static String ENDPOINT = "https://holadashboard.net";

    private static HolaApi holaApi;

    private static String csrfToken;

    private static OkHttpClient okHttpClient;

    public static String getCsrfToken() {
        return csrfToken;
    }

    public static void setCsrfToken(String csrfToken) {
        HolaRESTClient.csrfToken = csrfToken;
    }

    public static OkHttpClient init() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).cookieJar(new
                JavaNetCookieJar(cookieManager)).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        holaApi = new Retrofit.Builder().baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
                .create(HolaApi.class);
        return okHttpClient;
    }

    public static HolaApi get() {
        if (holaApi != null)
            return holaApi;
        else
            throw new RuntimeException("Retrofit client not initialized");
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient != null)
            return okHttpClient;
        else throw new RuntimeException("OkHttp client not initialized");
    }

    public static String getEndpoint() {
        return ENDPOINT;
    }

    public static String getDocumentUrl(int documentId){
        return getEndpoint() + "/document/get/" + documentId;
    }
}
