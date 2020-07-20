package com.flyby_riders.Retrofit;


import com.flyby_riders.Constants.Constant_url;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitClient(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant_url.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;

    }

}
