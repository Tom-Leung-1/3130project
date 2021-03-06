package edu.cuhk.csci3310.trablog_3310;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    String imagePath = "none";
    EditText titleInput;
    EditText contentInput;
    TextView latlngView;

    private String username;
    private Integer id;
    private String email;

    private String finalTitle;
    private String finalDescription;
    private boolean isOpen = false;
    private boolean isOpenImage = false;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String LOCAL_BASE_URL = "https://api.yautz.com/";
    //private String LOCAL_BASE_URL = "http://192.168.1.129:3001/";
    //private String LOCAL_BASE_URL = "http://10.0.2.2:3001/";
    //private String LOCAL_BASE_URL = "http://192.168.1.104:3001/";

    String iid = "none";

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
        imageView.setVisibility(View.VISIBLE);

        // check data.getClipData() == null

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                selectedImage = data.getData();
                Pattern pattern = Pattern.compile("google", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(selectedImage.toString());
                boolean matchFound = matcher.find();
                if(matchFound) {
                    Toast.makeText(CreatePost.this, "We do not support image upload from Goole Drive. Please reselect the image.", Toast.LENGTH_SHORT).show();
                    return;
                }// Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null);

                if (cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    Log.d("debug", String.valueOf(indexImage));
                    //int indexImage = cursor.getColumnIndexOrThrow( imageProjection[0] );
                    cursor.moveToFirst();

                    imagePath = cursor.getString(indexImage);
                    cursor.moveToFirst();
                    RealPathUtil rpu = new RealPathUtil();


                    String filePath = rpu.getRealPath(getApplicationContext(), selectedImage);
                    Log.d("debug", String.valueOf(filePath));

                    //part_image = cursor.getString(0);
                    imagePath = filePath;
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageView.setImageBitmap(bitmap);                                                       // Set the ImageView with the bitmap of the image
                    isOpenImage = true;
                }
            }
        }

        // check data.getClipData().getItemCount() , >= 0

    }



    // Upload the image to the remote database

    public void uploadImage() {
        //imagePath = "/storage/emulated/0/DCIM/Camera/IMG_20210512_231820.jpg";
        File imageFile = new File(imagePath);
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("file", imageFile.getName(), reqBody);


        API api = RetrofitClient.getInstance().getAPI();
        Call<ResponseBody> upload = api.uploadImage(partImage);
        Log.d("img", "image");

        upload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(CreatePost.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                    try {
                        Log.d("imgtry","iid is assigned");
                        iid = response.body().string().split(":")[1].split("\"")[1];
                        Log.d("imgtry",iid);

                    } catch (IOException e) {
                        Log.d("img","iid failed");

                        e.printStackTrace();
                    }
                }
                submitPost();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                submitPost();
                Toast.makeText(CreatePost.this, "Imange upload failed!", Toast.LENGTH_SHORT).show();
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
        gradientAnimation.setEnterFadeDuration(2000);
        gradientAnimation.setExitFadeDuration(5000);
        gradientAnimation.start();

        Intent intent = getIntent(); // get the intent message which is the position
        id = intent.getIntExtra("id", 0);
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");
        // mapFragment = new MapsFragment();
        /*
            Bundle bundle = new Bundle();
            bundle.putString("position", arr[0]);
            bundle.putString("show", arr[1]);
            mapFragment.setArguments(bundle);
         */
        // transaction.replace(R.id.map_container, mapFragment, "map");
        // transaction.commit();

         titleInput = (EditText)findViewById(R.id.titleInput);
         contentInput = (EditText)findViewById(R.id.contentInput);
         latlngView = (TextView)findViewById(R.id.latlng);
         imageView = (ImageView) findViewById(R.id.photo);
         Button selPhoto = (Button) findViewById(R.id.select_photo);
         Button send = (Button) findViewById(R.id.submitBtn);
         Button location = (Button) findViewById(R.id.locationBtn);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(ContextCompat.checkSelfPermission(CreatePost.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)) {
                    requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                else {
                    if(!isOpen)
                        openMap();
                    else
                        closeMap();
                }
            }
        });

        selPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isOpenImage)
                    pick(v);
                else
                    notPick();
            }
        } );

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalTitle = titleInput.getText().toString().trim();
                finalDescription = contentInput.getText().toString().trim();
                if (finalTitle.length() == 0 || finalDescription.length() == 0) {
                    Toast.makeText(CreatePost.this, "Title and Description cannot be empty! Please fill in the details.", Toast.LENGTH_LONG).show();
                }
                else {
                    if(imagePath != "none" && isOpenImage)
                        uploadImage();
                    else
                        submitPost();
                }
            }
        } );

    }
    public void notPick() {
        isOpenImage = false;
        imageView.setVisibility(View.GONE);
    }

    public void closeMap() {
        FrameLayout map = findViewById(R.id.map_container);
        if (map.getVisibility() == View.VISIBLE) {
            isOpen = false;
            map.setVisibility(View.GONE);
            //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //mapFragment = new MapsFragment();
        }
    }

    public void openMap() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            FrameLayout map = findViewById(R.id.map_container);
            if (map.getVisibility() != View.VISIBLE) {
                isOpen = true;
                map.setVisibility(View.VISIBLE);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                mapFragment = new MapsFragment();
                transaction.replace(R.id.map_container, mapFragment, "map");
                transaction.commit();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 1:
                openMap();
                break;
        }
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

        map.put("title", finalTitle);
        map.put("description", finalDescription);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && isOpen) {
            map.put("lat", latlngView.getText().toString().split(";")[0]);
            map.put("lng", latlngView.getText().toString().split(";")[1]);
        }
        if(iid != "none") {
            map.put("img", iid);
            Log.d("img", "iid != noen");
            Log.d("img3inside if", iid);

        }
        map.put("user_id", String.valueOf(id));
        Log.d("img3", iid);
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