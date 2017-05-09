package com.team60.ournews.util;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.team60.ournews.module.connection.ApiStore;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class RetrofitUtil {

    /**
     * http://192.168.0.104:8080/
     * http://112.74.52.72:8080/OurNews/
     */

//    public static final String BASE_URL = "http://192.168.31.57:8080/";
    public static final String BASE_URL = "http://112.74.52.72:8080/OurNews/";

    private static ApiStore apiStore;

    /**
     * 使用单例模式获取ApiStore对象
     * @return ApiStore
     */
    public static ApiStore newInstance() {
        if (apiStore == null) {
            synchronized (RetrofitUtil.class) {
                if (apiStore == null) {
                    apiStore = new Retrofit.Builder()
                            //添加请求统一前缀URL
                            .baseUrl(BASE_URL)
                            //添加自己的OkHttp配置
                            .client(OkHttpUtil.getOkHttpClient())
                            //设置使用Gson解析JSON数据
                            .addConverterFactory(GsonConverterFactory.create())
                            //设置支持RxJava
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build()
                            .create(ApiStore.class);
                }
            }
        }
        return apiStore;
    }
}
