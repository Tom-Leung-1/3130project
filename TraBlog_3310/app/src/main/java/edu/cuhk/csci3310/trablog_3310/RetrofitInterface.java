package edu.cuhk.csci3310.trablog_3310;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {
    @POST("/user")
    Call<LoginCredentials> executeLogin(@Body HashMap<String, String> map);

    @POST("/user/create")
    Call<Void> executeSignUp(@Body HashMap<String, String> map);
}
