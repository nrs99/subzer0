package edu.lehigh.cse216.slj222;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;
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
        holder.cText.setText(d.comment);
        holder.postedBy.setText(d.displayName);
        holder.profilePic.setImageBitmap(urlImage.getBitmapFromURL(d.photoURL));

        SharedPreferences sharedPref = context.getSharedPreferences("Shared", Context.MODE_PRIVATE);
        String userId = sharedPref.getString("userId", "0");


        if (d.userId.equals(userId)) {
            holder.editButton.setVisibility(View.VISIBLE);
        } else {
            holder.editButton.setVisibility(View.INVISIBLE);
        }

        holder.editButton.setOnClickListener(b -> {
            Intent intent = new Intent(b.getContext(), EditActivity.class);
            intent.putExtra("comment", d.comment);
            intent.putExtra("cId", d.commentId);
            context.startActivity(intent);
            if (context instanceof MessageComments) {
                Log.d("slj222", "We got here dude!");
                ((MessageComments) context).getComments();
            }
        });

        holder.profilePic.setOnClickListener(b -> {
            Intent intent = new Intent(b.getContext(), Profile.class);
            // Add extra for profiledID + logged in ID
            intent.putExtra("profiledUser", d.userId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return myData.size();
    }

}
