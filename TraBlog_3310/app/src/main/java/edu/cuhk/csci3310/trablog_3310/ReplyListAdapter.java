package edu.cuhk.csci3310.trablog_3310;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import edu.cuhk.csci3310.trablog_3310.ui.home.BlogListAdapter;

public class ReplyListAdapter extends RecyclerView.Adapter<ReplyListAdapter.ReplyViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private final LinkedList<String> mUserList;
    private final LinkedList<String> mCommentList;

    class ReplyViewHolder extends RecyclerView.ViewHolder {
        TextView userView, commentView;
        final ReplyListAdapter mAdapter;

        public ReplyViewHolder(View itemView, ReplyListAdapter adapter) {
            super(itemView);
            userView = itemView.findViewById(R.id.user);
            commentView = itemView.findViewById(R.id.comment);
            this.mAdapter = adapter;
        }
    }
    public ReplyListAdapter(Context context, LinkedList<String> userList, LinkedList<String> commentList) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mUserList = userList;
        this.mCommentList = commentList;
    }
    @NonNull
    @Override
    public ReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.reply_list_item, parent, false);
        return new ReplyListAdapter.ReplyViewHolder(mItemView, this);
    }
    @Override
    public void onBindViewHolder(@NonNull ReplyListAdapter.ReplyViewHolder holder, int position) {
        String user = mUserList.get(position);
        final String comment = mCommentList.get(position);
        holder.userView.setText(user);
        holder.commentView.setText(comment);
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}
