package com.example.Project_android_2.activities.RC_recyclerView;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.activities.RC_recyclerView.author_comic_model;
import com.example.Project_android_2.activities.RC_recyclerView.comic_model;
import com.example.Project_android_2.models.chapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RCAdapter_type_two extends RecyclerView.Adapter<RCAdapter_type_two.RCViewHolder_type_two> {

    Context context;
    ArrayList<author_comic_model> author_comic_model;
    private OnItemClickListener listener;
    public RCAdapter_type_two (Context context,ArrayList<author_comic_model> author_comic_model){
        this.context = context;
        this.author_comic_model = author_comic_model;
    }
    public interface OnItemClickListener {
        void onItemClick(author_comic_model position);
    }

    // Set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public RCViewHolder_type_two onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rc_item_type_two,parent,false);
        return new RCViewHolder_type_two(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder_type_two holder, int position) {
        author_comic_model rcmodel = author_comic_model.get(position);
        holder.Name_comic.setText(rcmodel.getName_comic());
        holder.Name_author.setText(rcmodel.getName_author());
        Picasso.get().load(rcmodel.getImage_comic()).into(holder.imgview);
        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    listener.onItemClick(rcmodel);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return author_comic_model.size();
    }
    public class RCViewHolder_type_two extends RecyclerView.ViewHolder{
        TextView Name_comic;
        TextView Name_author;
        ImageView imgview;
        public RCViewHolder_type_two (View itemView){
            super(itemView);
            Name_comic = itemView.findViewById(R.id.txt_name_comic);
            Name_author =itemView.findViewById(R.id.txt_name_author);
            imgview = itemView.findViewById(R.id.img_comic_two);
        }
    }
}