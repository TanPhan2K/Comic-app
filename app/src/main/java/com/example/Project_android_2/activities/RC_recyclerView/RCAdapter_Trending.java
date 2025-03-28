package com.example.Project_android_2.activities.RC_recyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.models.chapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RCAdapter_Trending extends RecyclerView.Adapter<RCAdapter_Trending.RCViewholder_story>  {
    Context context;
    ArrayList<comic_chapter_model> mListStory;
    private OnItemClickListener listener;
    public RCAdapter_Trending(Context context,ArrayList<comic_chapter_model> mlistStory){
        this.context = context;
        this.mListStory = mlistStory;
    }
    public interface OnItemClickListener {
        void onItemClick(comic_chapter_model position);
    }

    // Set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RCViewholder_story onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rc_item_img_txt,parent,false);
        return new RCViewholder_story(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RCViewholder_story holder, int position) {
        comic_chapter_model mstory = mListStory.get(position);
        holder.rc_textview.setText(mstory.getName_comic() + " \n");
        Picasso.get().load(mstory.getImage_comic()).into(holder.rc_image);
        holder.rc_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    listener.onItemClick(mstory);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListStory.size();
    }
    public class RCViewholder_story extends RecyclerView.ViewHolder {
        TextView rc_textview;
        ImageView rc_image;
        public RCViewholder_story(@NonNull View itemView) {
            super(itemView);
            rc_textview = itemView.findViewById(R.id.rc_title_story);
            rc_image = itemView.findViewById(R.id.img_comic);
        }
    }
}