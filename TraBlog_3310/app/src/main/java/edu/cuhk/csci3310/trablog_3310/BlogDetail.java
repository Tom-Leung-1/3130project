package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BlogDetail extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ReplyListAdapter mAdapter;
    private LinkedList<String> userList = new java.util.LinkedList<>();
    private LinkedList<String> commentList = new java.util.LinkedList<>();
    private TextView blogTitle;
    private TextView blogDesc;
    private WebView imageWebView;
    private Button user;
    private FrameLayout mapView;
    private RetrofitInterface retrofitInterface;
    private Retrofit retrofit;
    private String BASE_URL = "http://10.0.2.2:3001/";
    //private String BASE_URL = "http://192.168.1.104:3001/";
    private Integer postID;
    private Integer userID;

    private Integer id;
    private String username ;
    private String email;

    private Double lat;
    private Double lng;
    AnimationDrawable gradientAnimation;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        postID = intent.getIntExtra("postID", 0);
        userID = intent.getIntExtra("userID", 0);
        id = intent.getIntExtra("id", 0);
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        email = intent.getStringExtra("email");

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_blog_detail);
        mapView = findViewById(R.id.map_viewonly_container);
        NestedScrollView myLayout = (NestedScrollView) findViewById(R.id.blog_details_scroll);
        gradientAnimation = (AnimationDrawable) myLayout.getBackground();
        gradientAnimation.setEnterFadeDuration(10);
        gradientAnimation.setExitFadeDuration(5000);
        gradientAnimation.start();
        blogTitle = findViewById(R.id.blog_detail_title);
        blogDesc = findViewById(R.id.blog_detail_description);
        user = findViewById(R.id.blog_detail_user);
        imageWebView = findViewById(R.id.imageWebView);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        FloatingActionButton fab = findViewById(R.id.comment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReplyPage.class);
                intent.putExtra("postID", postID);
                intent.putExtra("userID", userID);
                intent.putExtra("id", id);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        mRecyclerView = findViewById(R.id.blog_recyclerview);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();




        Call<Blog> call = retrofitInterface.getOneBlog(postID);
        AppCompatActivity act = this;
        call.enqueue(new Callback<Blog>() { // async method: will call onResponse once the response is return, but before that the program ones other code
            @Override
            public void onResponse(Call<Blog> call, Response<Blog> response) {
                if (response.code() == 200) {
                    Blog blog = response.body();
                    blogTitle.setText(blog.getTitle());
                    blogDesc.setText(blog.getDescription());
                    user.setText(blog.getUsername());
                   // if(!Objects.isNull(blog.getImgFileName())){
                        Log.d("img", "download");

                    imageWebView.getSettings().setJavaScriptEnabled(true);
                        //imageWebView.loadUrl("https://api.yautz.com/uploads/" + blog.getImgFileName());
                    imageWebView.getSettings().setLoadWithOverviewMode(true);
                    imageWebView.getSettings().setUseWideViewPort(true);
                    imageWebView.loadUrl("https://api.yautz.com/uploads/file-1620835253318.jpg");

                    // imageWebView.loadUrl("http://10.0.2.2:3001/" + blog.getImgFileName());
                       // imageWebView.loadUrl("C:\\Users\\fung\\Desktop\\3310backend_tom\\travalfree\\uploads\\" + "file-1620915495127.jpg");

                   // }

                    if (!Objects.isNull(blog.getLat())) {
                        lat = blog.getLat();
                        lng = blog.getLng();
                        mapView.setVisibility(View.VISIBLE);
                        MapsViewOnlyFragment mapFragment = new MapsViewOnlyFragment();
                        Bundle bundle = new Bundle();
                        bundle.putDouble("lat", lat);
                        bundle.putDouble("lng", lng);
                        mapFragment.setArguments(bundle);
                        transaction.replace(R.id.map_viewonly_container, mapFragment, "map");
                        transaction.commit();
                    }
                }
                else if (response.code() == 400){
                    Toast.makeText(act, "Blog error", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(act, "Blogs error", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Blog> call, Throwable t) {
                Toast.makeText(act, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Call<ArrayList<Reply>> replyCall = retrofitInterface.getComments(postID);
        replyCall.enqueue(new Callback<ArrayList<Reply>>() { // async method: will call onResponse once the response is return, but before that the program ones other code
            @Override
            public void onResponse(Call<ArrayList<Reply>> call, Response<ArrayList<Reply>> response) {
                if (response.code() == 200) {
                    for (Reply reply : response.body()) {
                        userList.add(reply.getUser());
                        commentList.add(reply.getContent());
                    }
                    mAdapter = new ReplyListAdapter(BlogDetail.this, userList, commentList);
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(BlogDetail.this));
                }
                else if (response.code() == 400){
                    Toast.makeText(act, "Blog error", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(act, "Blogs error", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Reply>> call, Throwable t) {
                Toast.makeText(act, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), BlogList.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }

}
