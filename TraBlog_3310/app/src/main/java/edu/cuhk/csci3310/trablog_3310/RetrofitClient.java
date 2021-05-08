package edu.cuhk.csci3310.trablog_3310;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public class RetrofitClient {
    private  static final String BASE_URL = "http://remotedevs.org:8080/api/";                          // Base URL
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public API getAPI() {
        return retrofit.create(API.class);
    }
}

interface API {

    @GET("fileUpload/")
    Call<List<Image>> getImages();                                                                      // GET request to get all images

    @GET("fileUpload/files/{filename}")                                                                 // GET request to get an image by its name
    @Streaming
    Call<ResponseBody> getImageByName(@Path("filename") String name);

    @Multipart                                                                                          // POST request to upload an image from storage
    @POST("fileUpload/")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);
}