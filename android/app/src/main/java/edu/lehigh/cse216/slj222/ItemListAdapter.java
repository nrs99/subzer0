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
    //private LayoutInflater mLayoutInflater;


    ItemListAdapter(Context context, ArrayList<Message> data) {
        this.myData = data;
        this.context = context;
        //mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        //TextView mIndex;
        TextView mText;
        ImageButton thumbup;
        ImageButton thumbdown;
        Button postButton;
        EditText textToSend;

        ViewHolder(View itemView) {
            super(itemView);
            //this.mIndex = (TextView) itemView.findViewById(R.id.listTitle);
            this.mText = (TextView) itemView.findViewById(R.id.listItemText);         //need to change textview
            thumbup = itemView.findViewById(R.id.like_button);
            thumbdown = itemView.findViewById(R.id.dislike_button);
            postButton = itemView.findViewById(R.id.post_button);
            textToSend = itemView.findViewById(R.id.textView);
        }
    }


    @Override
    public int getItemCount() {
        return myData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
        View view = mLayoutInflater.inflate(R.layout.list_item, null);
        return new ViewHolder(view);
        */
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Message d = myData.get(position);
        viewHolder.mText.setText(d.message);

        viewHolder.thumbup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b)
            {
                //if the post is already liked
                if(b.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_unlikebutton).getConstantState())
                {
                    b.setBackgroundResource(R.drawable.ic_likebutton);
                }
                else{
                    b.setBackgroundResource(R.drawable.ic_unlikebutton);
                }
            }
        });
        viewHolder.thumbdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View b)
            {
                //if the post is already liked
                if(b.getBackground().getConstantState() == context.getResources().getDrawable(R.drawable.ic_no_thumbdown).getConstantState())
                {
                    b.setBackgroundResource(R.drawable.ic_thumbdown);
                }
                else{
                    b.setBackgroundResource(R.drawable.ic_no_thumbdown);
                }
            }
        });

    }

    interface ClickListener{
        void onClick(Message d);
    }

    private ClickListener mClickListener;
    ClickListener getClickListener() {return mClickListener;}
    void setClickListener(ClickListener c) { mClickListener = c;}
}