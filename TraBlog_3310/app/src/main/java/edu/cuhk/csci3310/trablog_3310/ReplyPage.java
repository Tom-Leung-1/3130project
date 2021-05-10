package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
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
        Integer id = intent.getIntExtra("id", 0);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", reply.getText().toString());
        // map.put("password", passwordEditText.getText().toString());
        // Call<LoginCredentials> call = retrofitInterface.createComment(map);
    }
}
