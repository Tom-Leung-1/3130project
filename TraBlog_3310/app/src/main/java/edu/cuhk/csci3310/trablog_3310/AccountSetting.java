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

public class AccountSetting extends AppCompatActivity {
    private EditText oldPW;
    private EditText newPW;
    private Button submit;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3001/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account_setting);
        oldPW = findViewById(R.id.old_password);
        newPW = findViewById(R.id.new_password);
        submit = findViewById(R.id.change_pw_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public void changePassword() {
        HashMap<String, String> map = new HashMap<>();
        map.put("old_password", oldPW.getText().toString());
        map.put("new_password", newPW.getText().toString());
        Call<Void> call = retrofitInterface.changeUserPassword(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(AccountSetting.this, "Password has been changed", Toast.LENGTH_LONG).show();

                }
                else if (response.code() == 400){
                    Toast.makeText(AccountSetting.this, "Email/password is incorrect. Please refill the credentials", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(AccountSetting.this, "Email/password is incorrect. Please refill the credentials", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AccountSetting.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
