package edu.lehigh.cse216.slj222;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private Context context;
    private ArrayList<Comment> myData;

    public CommentAdapter(Context context, ArrayList<Comment> data) {
        this.myData = data;
        this.context = context;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView cText;
        ImageButton profilePic;
        TextView postedBy;

        public CommentViewHolder(View itemView) {
            super(itemView);
            cText = itemView.findViewById(R.id.commentText);
            profilePic = itemView.findViewById(R.id.profilePic);
            postedBy = itemView.findViewById(R.id.posted_by);
        }
    }

    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment, parent, false);
        return new CommentAdapter.CommentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final Comment d = myData.get(position);
        holder.cText.setText(d.comment);
        holder.profilePic.setBackgroundResource(R.drawable.blank_profile); // Replace later with Google pic
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

}
