package edu.cuhk.csci3310.trablog_3310.ui.account;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import edu.cuhk.csci3310.trablog_3310.AccountSetting;
import edu.cuhk.csci3310.trablog_3310.BlogList;
import edu.cuhk.csci3310.trablog_3310.CreateAccount;
import edu.cuhk.csci3310.trablog_3310.R;
import edu.cuhk.csci3310.trablog_3310.RetrofitInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountFragment extends Fragment {
    private EditText oldPW;
    private EditText newPW;
    private Button submit;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "https://api.yautz.com/";

    //private String BASE_URL = "http://10.0.2.2:3001/";
    //private String BASE_URL = "http://192.168.1.104:3001/";

    private Integer id;
    private String username ;
    private String email;

    AnimationDrawable gradientAnimation;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account_setting, container, false);
        getCredentials();
        return root;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NestedScrollView myLayout = (NestedScrollView) getView().findViewById(R.id.change_account);
        gradientAnimation = (AnimationDrawable) myLayout.getBackground();
        gradientAnimation.setEnterFadeDuration(2000);
        gradientAnimation.setExitFadeDuration(5000);
        gradientAnimation.start();
        oldPW = (EditText) getView().findViewById(R.id.old_password);
        newPW = (EditText) getView().findViewById(R.id.new_password);
        submit = (Button) getView().findViewById(R.id.change_pw_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (newPW.getText().toString().length() < 8) {
                    Toast.makeText(getActivity(), "Your password is too short! Please make sure that you password length is at least 8.", Toast.LENGTH_LONG).show();
                }
                else {
                    changePassword();
                }
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
        map.put("id", String.valueOf(id));
        map.put("old_password", oldPW.getText().toString());
        map.put("new_password", newPW.getText().toString());
        Call<Void> call = retrofitInterface.changeUserPassword(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(getActivity(), "Password has been changed!", Toast.LENGTH_LONG).show();

                }
                else if (response.code() == 400){
                    Toast.makeText(getActivity(), "The old Password is wrong! Please refill the credentials.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "Email/password is incorrect. Please refill the credentials", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getCredentials() {
        id = ((BlogList) getActivity()).getUserID();
        username = ((BlogList) getActivity()).getUsername();
        email = ((BlogList) getActivity()).getEmail();
    }
}