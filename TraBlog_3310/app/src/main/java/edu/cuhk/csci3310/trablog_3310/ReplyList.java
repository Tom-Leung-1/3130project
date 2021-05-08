package edu.cuhk.csci3310.trablog_3310;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import edu.cuhk.csci3310.trablog_3310.ui.home.BlogListAdapter;

public class ReplyList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ReplyListAdapter mAdapter;
    private LinkedList<String> userList = new java.util.LinkedList<>();
    private LinkedList<String> commentList = new java.util.LinkedList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_list);
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
