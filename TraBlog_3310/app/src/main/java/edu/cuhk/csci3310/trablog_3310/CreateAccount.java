package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private String BASE_URL = "http://192.168.1.129:3001/";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
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
//                signUp(email.getText().toString(), password.getText().toString(), userName.getText().toString());
                handleSignUp();
            }
        });
    }

    private void handleSignUp() {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email.getText().toString());
        map.put("password", password.getText().toString());
        map.put("username", userName.getText().toString());
        Call<Void> call = retrofitInterface.executeSignUp(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(CreateAccount.this, "Account is created successfully", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(CreateAccount.this, "Username/Email has been used by another account. Please choose another one.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CreateAccount.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void signUp(String email, String password, String username) {
        Intent intent = new Intent(getApplicationContext(), BlogList.class);
        startActivity(intent);
    }
}
