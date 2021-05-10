package edu.cuhk.csci3310.trablog_3310.ui.home;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import edu.cuhk.csci3310.trablog_3310.R;
import java.util.LinkedList;
import java.util.List;

public class BlogListAdapter extends Adapter<BlogListAdapter.BlogViewHolder>{
    private RecyclerViewClickListener listener;
    private Fragment fragment;
    private LayoutInflater mInflater;
    private final LinkedList<String> mTitleList;
    private final LinkedList<String> mDescList;
    private final LinkedList<Integer> mIdList;

    class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleView, descriptionView;
        Integer id;
        final BlogListAdapter mAdapter;

        public BlogViewHolder(View itemView, BlogListAdapter adapter) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            descriptionView = itemView.findViewById(R.id.description);
            descriptionView.setOnClickListener(this);
            titleView.setOnClickListener(this);
            this.mAdapter = adapter;
        }
        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
    public BlogListAdapter(Fragment fragment, LinkedList<String> titleList, LinkedList<String> descList, LinkedList<Integer> idList, RecyclerViewClickListener listener) {
        this.fragment = fragment;
        mInflater = LayoutInflater.from(fragment.getActivity());
        this.mIdList = idList;
        this.mTitleList = titleList;
        this.mDescList = descList;
        this.listener = listener;
    }
    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }
    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.blog_list_item, parent, false);
        return new BlogViewHolder(mItemView, this);
    }
    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        String title = mTitleList.get(position);
        final String desc = mDescList.get(position);
        holder.titleView.setText(title);
        holder.descriptionView.setText(desc);
        holder.id = mIdList.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mTitleList.size();
    }
}
