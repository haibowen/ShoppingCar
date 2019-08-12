package com.ekwing.newshoppingcardemo.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 *
 * 简易的网络访问
 */

public class Httputils {
        public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            client.newCall(request).enqueue(callback);
        }


}
