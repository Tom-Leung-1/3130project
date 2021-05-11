package edu.cuhk.csci3310.trablog_3310;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @POST("/user")
    Call<LoginCredentials> executeLogin(@Body HashMap<String, String> map);

    @POST("/user/create")
    Call<LoginCredentials> executeSignUp(@Body HashMap<String, String> map);

    @POST("/post/create")
    Call<Void> executeSubmitPost(@Body HashMap<String, String> map);

    @GET("/post/allblogs")
    Call<ArrayList<Blog>> getAllBlogs();

    @GET("/post/oneblog")
    Call<Blog> getOneBlog(@Query("id") Integer id);

    @POST("/comment/postComment")
    Call<Void> createComment(@Body HashMap<String, String> map);

    @GET("/comment/getpostcomment")
    Call<ArrayList<Reply>> getComments(@Query("post_id") Integer id);

}
