package edu.cuhk.csci3310.trablog_3310.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import edu.cuhk.csci3310.trablog_3310.R;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private BlogListAdapter mAdapter;
    private BlogListAdapter.RecyclerViewClickListener mlistener;
    private HomeViewModel homeViewModel;
    private LinkedList<String> titleList = new java.util.LinkedList<>();
    private LinkedList<String> descList = new java.util.LinkedList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        return root;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = getView().findViewById(R.id.recyclerview);
        mlistener = new BlogListAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) { //test
                Toast.makeText(getActivity(),"Hello ",Toast.LENGTH_SHORT).show();
            }
        };
        //test
        for (int i = 0; i < 50; i++){
            titleList.add("ABC");titleList.add("DEF");
            descList.add("123");descList.add("456");
        }
        mAdapter = new BlogListAdapter(this, titleList, descList, mlistener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

}