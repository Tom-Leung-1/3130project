package edu.cuhk.csci3310.trablog_3310.ui.home;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.cuhk.csci3310.trablog_3310.Blog;
import edu.cuhk.csci3310.trablog_3310.BlogDetail;
import edu.cuhk.csci3310.trablog_3310.BlogList;
import edu.cuhk.csci3310.trablog_3310.LoginCredentials;
import edu.cuhk.csci3310.trablog_3310.R;
import edu.cuhk.csci3310.trablog_3310.ReplyPage;
import edu.cuhk.csci3310.trablog_3310.RetrofitInterface;
import edu.cuhk.csci3310.trablog_3310.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private BlogListAdapter mAdapter;
    private BlogListAdapter.RecyclerViewClickListener mlistener;
//    private HomeViewModel homeViewModel;
    private LinkedList<String> titleList = new java.util.LinkedList<>();
    private LinkedList<String> descList = new java.util.LinkedList<>();
    private LinkedList<Integer> idList = new java.util.LinkedList<>();
    private RetrofitInterface retrofitInterface;
    private Retrofit retrofit;
    private String BASE_URL = "http://10.0.2.2:3001/";

    private Integer id;
    private String username ;
    private String email;

    AnimationDrawable gradientAnimation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        getCredentials();
        return root;
    }
    public void getCredentials() {
        id = ((BlogList) getActivity()).getUserID();
        username = ((BlogList) getActivity()).getUsername();
        email = ((BlogList) getActivity()).getEmail();
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NestedScrollView myLayout = (NestedScrollView) getView().findViewById(R.id.home_fragment);
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

        mRecyclerView = getView().findViewById(R.id.recyclerview);
        mlistener = new BlogListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) { //test
                Intent intent = new Intent(getActivity(), BlogDetail.class);
                intent.putExtra("postID", idList.get(position));
                intent.putExtra("userID", ((BlogList) getActivity()).getUserID());
                intent.putExtra("id", id);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        };
        // get Data from backend
        Call<ArrayList<Blog>> call = retrofitInterface.getAllBlogs();
        Fragment fragment = this;
        call.enqueue(new Callback<ArrayList<Blog>>() { // async method: will call onResponse once the response is return, but before that the program ones other code
            @Override
            public void onResponse(Call<ArrayList<Blog>> call, Response<ArrayList<Blog>> response) {
                if (response.code() == 200) {
                    for (Blog blog : response.body()) {
                        idList.add(blog.getId());
                        titleList.add(blog.getTitle());
                        descList.add(blog.getDescription());
                        mAdapter = new BlogListAdapter(fragment, titleList, descList, idList, mlistener);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
                else if (response.code() == 400){
                    Toast.makeText(getActivity(), "Blogs error", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "Blogs error", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Blog>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

//        for (int i = 0; i < 50; i++){
//            idList.add("1");idList.add("2");
//            titleList.add("ABC");titleList.add("DEF");
//            descList.add("123");descList.add("456");
//        }
    }

}