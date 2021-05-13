package edu.cuhk.csci3310.trablog_3310.ui.login;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

// import edu.cuhk.csci3310.trablog_3310.API;
import edu.cuhk.csci3310.trablog_3310.BlogList;
import edu.cuhk.csci3310.trablog_3310.CreateAccount;
import edu.cuhk.csci3310.trablog_3310.LoginCredentials;
import edu.cuhk.csci3310.trablog_3310.R;
import edu.cuhk.csci3310.trablog_3310.RetrofitInterface;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView title;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3001/";
    //private String BASE_URL = "http://192.168.1.104:3001/";

    AnimationDrawable gradientAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        title = findViewById(R.id.title);
        YoYo.with(Techniques.RollIn).duration(2000).playOn(title);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ConstraintLayout myLayout = (ConstraintLayout) findViewById(R.id.container);
        gradientAnimation = (AnimationDrawable) myLayout.getBackground();
        gradientAnimation.setEnterFadeDuration(10);
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


        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button signUpButton = findViewById(R.id.signUp);
//        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(intent);
            }
        });
    }

    public void handleLogin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("email", emailEditText.getText().toString());
        map.put("password", passwordEditText.getText().toString());
        Call<LoginCredentials> call = retrofitInterface.executeLogin(map);
        call.enqueue(new Callback<LoginCredentials>() {
            @Override
            public void onResponse(Call<LoginCredentials> call, Response<LoginCredentials> response) {
                if (response.code() == 200) {
                    LoginCredentials loginCredentials = response.body();
                    Toast.makeText(LoginActivity.this, "Hello " + loginCredentials.getUsername() + "! Welcome back to TraBlog!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BlogList.class);
                    intent.putExtra("username", loginCredentials.getUsername());
                    intent.putExtra("email", loginCredentials.getEmail());
                    intent.putExtra("id", loginCredentials.getMyID());
                    startActivity(intent);
                }
                else if (response.code() == 400){
                    Toast.makeText(LoginActivity.this, "Email/password is incorrect. Please refill the credentials", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Email/password is incorrect. Please refill the credentials", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginCredentials> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void validate(String email, String password) {
        // Need changes
        if (email.equals("abc") && password.equals("123")) {
            Intent intent = new Intent(getApplicationContext(), BlogList.class);
            startActivity(intent);
        }
    }

//        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
//            @Override
//            public void onChanged(@Nullable LoginFormState loginFormState) {
//                if (loginFormState == null) {
//                    return;
//                }
//                loginButton.setEnabled(loginFormState.isDataValid());
//                if (loginFormState.getUsernameError() != null) {
//                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
//                }
//                if (loginFormState.getPasswordError() != null) {
//                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
//                }
//            }
//        });

//        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
//            @Override
//            public void onChanged(@Nullable LoginResult loginResult) {
//                if (loginResult == null) {
//                    return;
//                }
//                loadingProgressBar.setVisibility(View.GONE);
//                if (loginResult.getError() != null) {
//                    showLoginFailed(loginResult.getError());
//                }
//                if (loginResult.getSuccess() != null) {
//                    updateUiWithUser(loginResult.getSuccess());
//                }
//                setResult(Activity.RESULT_OK);
//
//                //Complete and destroy login activity once successful
//                finish();
//            }
//        });

//        TextWatcher afterTextChangedListener = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // ignore
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // ignore
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
//                        passwordEditText.getText().toString());
//            }
//        };
//        usernameEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });


//    private void updateUiWithUser(LoggedInUserView model) {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
//        //
//        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
//    }
//
//    private void showLoginFailed(@StringRes Integer errorString) {
//        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
//    }
}