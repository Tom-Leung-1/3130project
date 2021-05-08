package edu.cuhk.csci3310.trablog_3310;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class CreatePost extends AppCompatActivity {
    Fragment mapFragment;
    Double latlng[] = new Double[2];
    String content, title;
    private static final int PICK_IMAGE_REQUEST = 44;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    ImageView imageView;
    Uri selectedImage;
    String part_image;
    EditText titleInput;
    EditText contentInput;
    TextView latlngView;


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
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(photoPickerIntent, PICK_IMAGE_REQUEST);
    }

    // Method to get the absolute path of the selected image from its URI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debug", String.valueOf(data.getClipData().getItemCount()));
        Log.d("debug", String.valueOf("@@@@"));

        // check data.getClipData() == null

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                selectedImage = data.getData();                                                         // Get the image file URI
                String[] imageProjection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, imageProjection, null, null, null);
                if(cursor != null) {
                    cursor.moveToFirst();
                    int indexImage = cursor.getColumnIndex(imageProjection[0]);
                    Log.d("debug", String.valueOf(cursor.getCount()));

                    part_image = cursor.getString(indexImage);
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
        File imageFile = new File(part_image);                                                          // Create a file using the absolute path of the image
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
                uploadImage(v);
            }
        } );

        //                title = titleInput.getText().toString();
        //                content = contentInput.getText().toString();
        //latlngView.getText().toString().split(";")[0] + latlngView.getText().toString().split(";")[1]


    }

}