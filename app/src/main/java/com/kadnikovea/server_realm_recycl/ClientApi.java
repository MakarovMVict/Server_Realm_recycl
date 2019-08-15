package com.kadnikovea.server_realm_recycl;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi {
    public static String BASE_URL="https://5d54007a-276c-4278-8e57-8b78e39d5a31.mock.pstmn.io";
    private static Retrofit retrofit;
    public static Retrofit getClient(){
        if(retrofit==null){

            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
