package edu.lehigh.cse216.slj222;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        Button editButton;

        public CommentViewHolder(View itemView) {
            super(itemView);
            cText = itemView.findViewById(R.id.commentText);
            profilePic = itemView.findViewById(R.id.profilePic);
            postedBy = itemView.findViewById(R.id.posted_by);
            editButton = itemView.findViewById(R.id.editButton);
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
        holder.cText.setText(d.userId); // TODO: Fix this once Vinny fixes that
        holder.profilePic.setBackgroundResource(R.drawable.blank_profile); // Replace later with Google pic

        SharedPreferences sharedPref = context.getSharedPreferences("Shared", Context.MODE_PRIVATE);
        String userId = sharedPref.getString("userId", "0");


        if (d.comment.equals(userId)) { // TODO: Change this when Vinny fixes
            holder.editButton.setVisibility(View.VISIBLE);
        } else {
            holder.editButton.setVisibility(View.INVISIBLE);
        }

        holder.editButton.setOnClickListener(b -> {
            // TODO: Create an editing activity, take user there, then HTTP put
            Intent intent = new Intent(b.getContext(), EditActivity.class);
            intent.putExtra("comment", d.userId); // TODO: Change on Vinny fix
            intent.putExtra("cId", d.commentId);
            context.startActivity(intent);
            if (context instanceof MessageComments) {
                ((MessageComments) context).getComments();
            }
        });

        holder.profilePic.setOnClickListener(b -> {
            Intent intent = new Intent(b.getContext(), Profile.class);
            // Add extra for profiledID + logged in ID
            intent.putExtra("profiledUser", d.comment); // TODO: Change when Vinny fixes
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

}
