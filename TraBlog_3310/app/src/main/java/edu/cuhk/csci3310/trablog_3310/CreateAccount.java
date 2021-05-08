package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccount extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText userName;
    private Button signUpButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        email = findViewById(R.id.sign_up_email);
        password = findViewById(R.id.sign_up_password);
        userName = findViewById(R.id.sign_up_username);
        signUpButton = findViewById(R.id.sign_up_btn);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(email.getText().toString(), password.getText().toString(), userName.getText().toString());
            }
        });
    }

    public void signUp(String email, String password, String username) {
        Intent intent = new Intent(getApplicationContext(), BlogList.class);
        startActivity(intent);
    }
}
