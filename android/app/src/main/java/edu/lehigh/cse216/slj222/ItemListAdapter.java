package edu.lehigh.cse216.slj222;

import android.util.Log;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Message> myData;


    ItemListAdapter(Context context, ArrayList<Message> data) {
        this.myData = data;
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        //TextView mIndex;
        TextView mText;
        ImageButton thumbup;
        ImageButton thumbdown;
        Button postButton;
        EditText textToSend;
        TextView likeCount;
        TextView dislikeCount;

        ViewHolder(View itemView) {
            super(itemView);
            this.mText = (TextView) itemView.findViewById(R.id.listItemText);         //need to change textview
            thumbup = itemView.findViewById(R.id.like_button);
            thumbdown = itemView.findViewById(R.id.dislike_button);
            postButton = itemView.findViewById(R.id.post_button);
            textToSend = itemView.findViewById(R.id.textView);
            likeCount = itemView.findViewById(R.id.likeCount);
            dislikeCount = itemView.findViewById(R.id.dislikeCount);
        }
    }


    @Override
    public int getItemCount() {
        return myData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Message d = myData.get(position);
        viewHolder.mText.setText(d.message);
        viewHolder.likeCount.setText(String.valueOf(d.likes));
        viewHolder.dislikeCount.setText(String.valueOf(d.dislikes));


        viewHolder.thumbup.setOnClickListener(b -> {
            //Change the image if clicked
            if (b.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_unlikebutton).getConstantState()) {
                b.setBackgroundResource(R.drawable.ic_likebutton);
                if (viewHolder.thumbdown.getBackground().getConstantState()== viewHolder.thumbdown.getResources().getDrawable(R.drawable.ic_thumbdown).getConstantState()) {
                    // Decrease dislikes by 1
                    viewHolder.thumbdown.setBackgroundResource(R.drawable.ic_no_thumbdown);
                    int oldDislikes = Integer.parseInt(viewHolder.dislikeCount.getText().toString());
                    viewHolder.dislikeCount.setText(String.valueOf(oldDislikes - 1));
                }
                // Increase likes by 1
                int oldLikes = Integer.parseInt(viewHolder.likeCount.getText().toString());
                viewHolder.likeCount.setText(String.valueOf(oldLikes + 1));
            } else {
                b.setBackgroundResource(R.drawable.ic_unlikebutton);
                // Decrease likes by 1
                int oldLikes = Integer.parseInt(viewHolder.likeCount.getText().toString());
                viewHolder.likeCount.setText(String.valueOf(oldLikes - 1));
            }
        });
        viewHolder.thumbdown.setOnClickListener(b -> {
            // Change the image if clicked
            if (b.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_no_thumbdown).getConstantState()) {
                b.setBackgroundResource(R.drawable.ic_thumbdown);
                if (viewHolder.thumbup.getBackground().getConstantState()== viewHolder.thumbup.getResources().getDrawable(R.drawable.ic_likebutton).getConstantState()) {
                    // Decrease likes by 1
                    viewHolder.thumbup.setBackgroundResource(R.drawable.ic_unlikebutton);
                    int oldLikes = Integer.parseInt(viewHolder.likeCount.getText().toString());
                    viewHolder.likeCount.setText(String.valueOf(oldLikes - 1));
                }
                // Increase dislikes by 1
                int oldDislikes = Integer.parseInt(viewHolder.dislikeCount.getText().toString());
                viewHolder.dislikeCount.setText(String.valueOf(oldDislikes + 1));
            } else {
                b.setBackgroundResource(R.drawable.ic_no_thumbdown);
                // Decrease dislikes by 1
                int oldDislikes = Integer.parseInt(viewHolder.dislikeCount.getText().toString());
                viewHolder.dislikeCount.setText(String.valueOf(oldDislikes - 1));
            }
        });

    }

    interface ClickListener {
        void onClick(Message d);
    }

    private ClickListener mClickListener;

    ClickListener getClickListener() {
        return mClickListener;
    }

    void setClickListener(ClickListener c) {
        mClickListener = c;
    }
}