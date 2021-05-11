package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReplyPage extends AppCompatActivity {
    private Integer userID;
    private Integer postID;
    private EditText reply;
    private Button submit;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.168.1.129:3001/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        postID = intent.getIntExtra("postID", 0);
        userID = intent.getIntExtra("userID", 0);
        setContentView(R.layout.activity_reply_page);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        reply = findViewById(R.id.reply);
        submit = findViewById(R.id.submit_reply);
        submit.setOnClickListener(v -> submitPost());
    }

    private void submitPost() {
        HashMap<String, String> map = new HashMap<>();
        map.put("content", reply.getText().toString());
        map.put("post_id", postID.toString());
        map.put("user_id", userID.toString());
        Call<Void> call = retrofitInterface.createComment(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(ReplyPage.this, "Reply is successfully submitted", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(ReplyPage.this, "Server Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReplyPage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // map.put("password", passwordEditText.getText().toString());
        // Call<LoginCredentials> call = retrofitInterface.createComment(map);
    }
}
