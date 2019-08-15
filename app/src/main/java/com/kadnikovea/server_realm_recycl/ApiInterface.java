package com.kadnikovea.server_realm_recycl;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("/names")
    Call<List<MyBook_Retof>> getName();
    /*@GET("/namesPost")
    Call<MyBook_Retof> getById(("id") int id);*/

    @POST("/namesUser")
    Call<MyBook_Retof> createName(@Body MyBook_Retof myBook_retof);

}
