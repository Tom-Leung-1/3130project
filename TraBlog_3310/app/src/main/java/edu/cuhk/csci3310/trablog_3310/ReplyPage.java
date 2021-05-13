package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    //private String BASE_URL = "http://10.0.2.2:3001/";
    private String BASE_URL = "https://api.yautz.com/";

    private Integer id;
    private String username ;
    private String email;

    private String finalReply;

    AnimationDrawable gradientAnimation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        postID = intent.getIntExtra("postID", 0);
        userID = intent.getIntExtra("userID", 0);
        id = intent.getIntExtra("id", 0);
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        setContentView(R.layout.activity_reply_page);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ConstraintLayout myLayout = (ConstraintLayout) findViewById(R.id.reply_page);
        gradientAnimation = (AnimationDrawable) myLayout.getBackground();
        gradientAnimation.setEnterFadeDuration(2000);
        gradientAnimation.setExitFadeDuration(5000);
        gradientAnimation.start();

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
                finalReply = reply.getText().toString().trim();
                if (finalReply.length() == 0) {
                    Toast.makeText(ReplyPage.this, "Reply cannot be empty! Please fill in the details.", Toast.LENGTH_LONG).show();
                }
                else {
                    submitPost();
                }
            }
        });
    }

    private void submitPost() {
        HashMap<String, String> map = new HashMap<>();
        map.put("content", finalReply);
        map.put("post_id", postID.toString());
        map.put("user_id", userID.toString());
        Call<Void> call = retrofitInterface.createComment(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(ReplyPage.this, "Reply is successfully submitted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BlogDetail.class);
                    intent.putExtra("postID", postID);
                    intent.putExtra("userID", userID);
                    intent.putExtra("id", id);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    startActivity(intent);
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
