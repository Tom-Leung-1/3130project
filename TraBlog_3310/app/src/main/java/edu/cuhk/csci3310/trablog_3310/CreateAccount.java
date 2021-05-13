package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import edu.cuhk.csci3310.trablog_3310.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAccount extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText userName;
    private Button signUpButton;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3001/";
    AnimationDrawable gradientAnimation;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ConstraintLayout myLayout = (ConstraintLayout) findViewById(R.id.create_account);
        gradientAnimation = (AnimationDrawable) myLayout.getBackground();
        gradientAnimation.setEnterFadeDuration(2000);
        gradientAnimation.setExitFadeDuration(5000);
        gradientAnimation.start();

        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        userName = findViewById(R.id.sign_up_username);
        signUpButton = findViewById(R.id.sign_up_btn);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().length() < 8) {
                    Toast.makeText(CreateAccount.this, "Your password is too short! Please make sure that you password length is at least 8.", Toast.LENGTH_LONG).show();
                }
                else {
                    handleSignUp();
                }
            }
        });
    }

    private void handleSignUp() {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email.getText().toString());
        map.put("password", password.getText().toString());
        map.put("username", userName.getText().toString());
        Call<LoginCredentials> call = retrofitInterface.executeSignUp(map);
        call.enqueue(new Callback<LoginCredentials>() {
            @Override
            public void onResponse(Call<LoginCredentials> call, Response<LoginCredentials> response) {
                if (response.code() == 200) {
                    Toast.makeText(CreateAccount.this, "Account is created successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BlogList.class);
                    LoginCredentials login = response.body();
                    intent.putExtra("id", login.getMyID());
                    intent.putExtra("username", login.getUsername());
                    intent.putExtra("email", login.getEmail());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(CreateAccount.this, "Username/Email has been used by another account. Please choose another one.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginCredentials> call, Throwable t) {
                Toast.makeText(CreateAccount.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signUp(String email, String password, String username) {
        Intent intent = new Intent(getApplicationContext(), BlogList.class);
        startActivity(intent);
    }
}
