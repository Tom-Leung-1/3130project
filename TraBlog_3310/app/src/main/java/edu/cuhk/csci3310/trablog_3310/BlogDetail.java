package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;

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
    private TextView user;
    private RetrofitInterface retrofitInterface;
    private Retrofit retrofit;
    private String BASE_URL = "http://192.168.1.129:3001/";
    private Integer id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        blogTitle = findViewById(R.id.blog_detail_title);
        blogDesc = findViewById(R.id.blog_detail_description);
        user = findViewById(R.id.blog_detail_user);
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
                startActivity(intent);
            }
        });
        mRecyclerView = findViewById(R.id.blog_recyclerview);
        for (int i = 0; i < 50; i++){
            userList.add("ABC");userList.add("DEF");
            commentList.add("123");commentList.add("456");
        }
        mAdapter = new ReplyListAdapter(this, userList, commentList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Intent intent = getIntent(); // get the intent message which is the position
        /*
            Bundle bundle = new Bundle();
            bundle.putString("position", arr[0]);
            bundle.putString("show", arr[1]);
            mapFragment.setArguments(bundle);
        */
        MapsViewOnlyFragment mapFragment = new MapsViewOnlyFragment();
        transaction.replace(R.id.map_viewonly_container, mapFragment, "map");
        transaction.commit();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Call<Blog> call = retrofitInterface.getOneBlog(id);
        AppCompatActivity act = this;
        call.enqueue(new Callback<Blog>() { // async method: will call onResponse once the response is return, but before that the program ones other code
            @Override
            public void onResponse(Call<Blog> call, Response<Blog> response) {
                if (response.code() == 200) {
                    Blog blog = response.body();
                    blogTitle.setText(blog.getTitle());
                    blogDesc.setText(blog.getDescription());
                    user.setText(blog.getUser());
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




    }

}
