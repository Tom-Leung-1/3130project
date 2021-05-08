package edu.cuhk.csci3310.trablog_3310;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;

public class BlogDetail extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ReplyListAdapter mAdapter;
    private LinkedList<String> userList = new java.util.LinkedList<>();
    private LinkedList<String> commentList = new java.util.LinkedList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        FloatingActionButton fab = findViewById(R.id.comment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReplyPage.class);
                startActivity(intent);
            }
        });
        mRecyclerView = findViewById(R.id.blog_recyclerview);
        for (int i = 0; i < 50; i++){
            userList.add("ABC");userList.add("DEF");
            commentList.add("123");commentList.add("456");
        }
        mAdapter = new ReplyListAdapter(this, userList, commentList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
