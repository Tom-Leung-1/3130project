package edu.cuhk.csci3310.trablog_3310;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class CreatePost extends AppCompatActivity {
    Fragment mapFragment;
    Double latlng[] = new Double[2];
    String content, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent(); // get the intent message which is the position

        mapFragment = new MapsFragment();
        /*
            Bundle bundle = new Bundle();
            bundle.putString("position", arr[0]);
            bundle.putString("show", arr[1]);
            mapFragment.setArguments(bundle);
         */
        transaction.replace(R.id.map_container, mapFragment, "map");
        transaction.commit();

        EditText titleInput = (EditText)findViewById(R.id.titleInput);
        EditText contentInput = (EditText)findViewById(R.id.contentInput);
        TextView latlngView = (TextView)findViewById(R.id.latlng);

        latlngView.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

              }
              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                latlng[0] = Double.parseDouble(latlngView.getText().toString().split(";")[0]);
                latlng[1] = Double.parseDouble(latlngView.getText().toString().split(";")[1]);
                Log.d("latlng",latlngView.getText().toString().split(";")[0] + latlngView.getText().toString().split(";")[1]);
              }

              @Override
              public void afterTextChanged(Editable s) {

              }
        });

        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = titleInput.getText().toString();
                Log.d("title", title);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        contentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                content = contentInput.getText().toString();
                Log.d("title", content);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}