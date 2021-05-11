package edu.cuhk.csci3310.trablog_3310;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import edu.cuhk.csci3310.trablog_3310.ui.login.LoginActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CreatePost extends AppCompatActivity {
    Fragment mapFragment;
    Double latlng[] = new Double[2];
    String content, title;
    private static final int PICK_IMAGE_REQUEST = 44;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    ImageView imageView;
    Uri selectedImage;
    String imagePath;
    EditText titleInput;
    EditText contentInput;
    TextView latlngView;

    private String username;
    private Integer id;
    private String email;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    //private String LOCAL_BASE_URL = "https://api.yautz.com/";
    private String LOCAL_BASE_URL = "http://192.168.1.129:3001/";


    AnimationDrawable gradientAnimation;

    // Method for starting the activity for selecting image from phone storage
    public void pick(View view) {
        int permission = ActivityCompat.checkSelfPermission(CreatePost.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    CreatePost.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Open Gallery"), PICK_IMAGE_REQUEST);
    }

    // Method to get the absolute path of the selected image from its URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("debug", String.valueOf(data.getClipData().getItemCount()));
        Log.d("debug", String.valueOf("@@@@"));

        // check data.getClipData() == null

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                selectedImage = data.getData();                                                         // Get the image file URI

                String[] imageProjection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    //int indexImage = cursor.getColumnIndexOrThrow( imageProjection[0] );
                    cursor.moveToFirst();

                    imagePath = cursor.getString(indexImage);
                    cursor.moveToFirst();
                    RealPathUtil rpu = new RealPathUtil();

                    try {
                        String filePath = rpu.getRealPath(getApplicationContext(), selectedImage);
                        Log.d("debug", String.valueOf(filePath));

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    //part_image = cursor.getString(0);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);                                                       // Set the ImageView with the bitmap of the image
                }
            }
        }

        // check data.getClipData().getItemCount() , >= 0

    }



    // Upload the image to the remote database

    public void uploadImage(View view) {
        File imageFile = new File("123");                                                          // Create a file using the absolute path of the image
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("file", imageFile.getName(), reqBody);
        API api = RetrofitClient.getInstance().getAPI();
        Call<ResponseBody> upload = api.uploadImage(partImage);
        upload.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(CreatePost.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreatePost.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ScrollView myLayout = (ScrollView) findViewById(R.id.create_post);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        gradientAnimation = (AnimationDrawable) myLayout.getBackground();
        gradientAnimation.setEnterFadeDuration(10);
        gradientAnimation.setExitFadeDuration(5000);
        gradientAnimation.start();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Intent intent = getIntent(); // get the intent message which is the position
        id = intent.getIntExtra("id", 0);
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        mapFragment = new MapsFragment();
        /*
            Bundle bundle = new Bundle();
            bundle.putString("position", arr[0]);
            bundle.putString("show", arr[1]);
            mapFragment.setArguments(bundle);
         */
        transaction.replace(R.id.map_container, mapFragment, "map");
        transaction.commit();

         titleInput = (EditText)findViewById(R.id.titleInput);
         contentInput = (EditText)findViewById(R.id.contentInput);
         latlngView = (TextView)findViewById(R.id.latlng);
        imageView = (ImageView) findViewById(R.id.photo);
        Button selPhoto = (Button) findViewById(R.id.select_photo);
        Button send = (Button) findViewById(R.id.submitBtn);

        selPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick(v);
            }
        } );

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // uploadImage(v);
                submitPost();
            }
        } );

        //                title = titleInput.getText().toString();
        //                content = contentInput.getText().toString();
        //latlngView.getText().toString().split(";")[0] + latlngView.getText().toString().split(";")[1]


    }

    private void submitPost() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(LOCAL_BASE_URL) // changed
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("title", titleInput.getText().toString());
        map.put("description", contentInput.getText().toString());
        map.put("lat", latlngView.getText().toString().split(";")[0]);
        map.put("long", latlngView.getText().toString().split(";")[1]);

        Call<Void> call = retrofitInterface.executeSubmitPost(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(CreatePost.this, "Post submitted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BlogList.class);
                    intent.putExtra("username", username);
                    intent.putExtra("email", email);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                else if (response.code() == 400){
                    Toast.makeText(CreatePost.this, "Email/password is incorrect. Please refill the credentials", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(CreatePost.this, "Email/password is incorrect. Please refill the credentials", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CreatePost.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}