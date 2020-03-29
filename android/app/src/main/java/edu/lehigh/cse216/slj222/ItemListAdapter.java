package edu.lehigh.cse216.slj222;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Message> myData;


    ItemListAdapter(Context context, ArrayList<Message> data) {
        this.myData = data;
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        ImageButton thumbup;
        ImageButton thumbdown;
        ImageButton profilePic;
        Button postButton;
        EditText textToSend;
        TextView likeCount;
        TextView dislikeCount;
        TextView commentCount;
        TextView postedBy;

        ViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.listItemText);
            thumbup = itemView.findViewById(R.id.like_button);
            thumbdown = itemView.findViewById(R.id.dislike_button);
            postButton = itemView.findViewById(R.id.post_button);
            textToSend = itemView.findViewById(R.id.textView);
            likeCount = itemView.findViewById(R.id.likeCount);
            profilePic = itemView.findViewById(R.id.profilePic);
            dislikeCount = itemView.findViewById(R.id.dislikeCount);
            commentCount = itemView.findViewById(R.id.commentCount);
            postedBy = itemView.findViewById(R.id.posted_by);
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
        viewHolder.profilePic.setBackgroundResource(R.drawable.blank_profile); // Replace later with Google pic
        int likeStatus = d.myLike;
        if (likeStatus == 1) {
            viewHolder.thumbup.setBackgroundResource(R.drawable.ic_likebutton);
            viewHolder.thumbdown.setBackgroundResource(R.drawable.ic_no_thumbdown);
        } else if (likeStatus == -1) {
            viewHolder.thumbup.setBackgroundResource(R.drawable.ic_unlikebutton);
            viewHolder.thumbdown.setBackgroundResource(R.drawable.ic_thumbdown);
        } else {
            viewHolder.thumbup.setBackgroundResource(R.drawable.ic_unlikebutton);
            viewHolder.thumbdown.setBackgroundResource(R.drawable.ic_no_thumbdown);
        }

        viewHolder.thumbup.setOnClickListener(b -> {
            //Change the image if clicked
            if (b.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_unlikebutton).getConstantState()) {
                b.setBackgroundResource(R.drawable.ic_likebutton);
                d.myLike = 1;
                likeMessage(d.msgId); // Send the HTTP request
                if (viewHolder.thumbdown.getBackground().getConstantState() == viewHolder.thumbdown.getResources().getDrawable(R.drawable.ic_thumbdown).getConstantState()) {
                    // Decrease dislikes by 1
                    d.dislikes--;
                    viewHolder.thumbdown.setBackgroundResource(R.drawable.ic_no_thumbdown);
                    int oldDislikes = Integer.parseInt(viewHolder.dislikeCount.getText().toString());
                    viewHolder.dislikeCount.setText(String.valueOf(oldDislikes - 1));
                }
                // Increase likes by 1
                d.likes++;
                int oldLikes = Integer.parseInt(viewHolder.likeCount.getText().toString());
                viewHolder.likeCount.setText(String.valueOf(oldLikes + 1));
            } else {
                d.myLike = 0;
                b.setBackgroundResource(R.drawable.ic_unlikebutton);
                // Decrease likes by 1
                d.likes--;
                int oldLikes = Integer.parseInt(viewHolder.likeCount.getText().toString());
                viewHolder.likeCount.setText(String.valueOf(oldLikes - 1));
            }
        });

        viewHolder.thumbdown.setOnClickListener(b -> {
            // Change the image if clicked
            if (b.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_no_thumbdown).getConstantState()) {
                b.setBackgroundResource(R.drawable.ic_thumbdown);
                d.myLike = -1;
                dislikeMessage(d.msgId); // Send the HTTP request
                if (viewHolder.thumbup.getBackground().getConstantState() == viewHolder.thumbup.getResources().getDrawable(R.drawable.ic_likebutton).getConstantState()) {
                    // Decrease likes by 1
                    d.likes--;
                    viewHolder.thumbup.setBackgroundResource(R.drawable.ic_unlikebutton);
                    int oldLikes = Integer.parseInt(viewHolder.likeCount.getText().toString());
                    viewHolder.likeCount.setText(String.valueOf(oldLikes - 1));
                }
                // Increase dislikes by 1
                d.dislikes++;
                int oldDislikes = Integer.parseInt(viewHolder.dislikeCount.getText().toString());
                viewHolder.dislikeCount.setText(String.valueOf(oldDislikes + 1));
            } else {
                d.myLike = 0;
                b.setBackgroundResource(R.drawable.ic_no_thumbdown);
                // Decrease dislikes by 1
                d.dislikes--;
                int oldDislikes = Integer.parseInt(viewHolder.dislikeCount.getText().toString());
                viewHolder.dislikeCount.setText(String.valueOf(oldDislikes - 1));
            }
        });

        viewHolder.commentCount.setOnClickListener(b -> {
            // Go to individual message activity
            Intent intent = new Intent(b.getContext(), MessageComments.class);
            // Add extra for account + whichever message
            intent.putExtra("msgid", d.msgId);
            context.startActivity(intent);
        });

        viewHolder.profilePic.setOnClickListener(b -> {
            // Go to individual profile
            Intent intent = new Intent(b.getContext(), Profile.class);
            // Add extra for profiledID + logged in ID
            intent.putExtra("profiledUser", d.userId);
            context.startActivity(intent);
        });

        viewHolder.postedBy.setOnClickListener(b -> {
            // Go to individual profile
            Intent intent = new Intent(b.getContext(), Profile.class);
            // Add extra for profiledID + logged in ID
            intent.putExtra("profiledUser", 0);
            context.startActivity(intent);
        });

    }

    /**
     * Send an HTTP PUT request to like message with given ID
     *
     * @param msgId
     */
    public void likeMessage(int msgId) {
        JSONObject request = new JSONObject();

        String url = "http://subzer0.herokuapp.com/messages/" + msgId + "/like";

        JsonObjectRequest getReq = new JsonObjectRequest(Request.Method.PUT, url, request,
                response -> {
                    try {
                        response.getString("mStatus");  //if its working or not
                    } catch (final JSONException e) {
                        Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
                    }
                },
                error -> {
                    // if there's an error
                    Log.d("slj222", "error:" + error.getMessage());
                    error.printStackTrace();
                }) {
        };
        VolleySingleton.getInstance(context).addToRequestQueue(getReq);
    }

    /**
     * Send an HTTP PUT request to dislike message with given ID
     *
     * @param msgId
     */

    public void dislikeMessage(int msgId) {
        JSONObject request = new JSONObject();

        String url = "http://subzer0.herokuapp.com/messages/" + msgId + "/dislike";

        JsonObjectRequest getReq = new JsonObjectRequest(Request.Method.PUT, url, request,
                response -> {
                    try {
                        response.getString("mStatus");  //if its working or not
                    } catch (final JSONException e) {
                        Log.d("slj222", "Error parsing JSON file: " + e.getMessage());
                    }
                },
                error -> {
                    // if there's an error
                    Log.d("slj222", "error:" + error.getMessage());
                    error.printStackTrace();
                }) {
        };
        VolleySingleton.getInstance(context).addToRequestQueue(getReq);

    }

}