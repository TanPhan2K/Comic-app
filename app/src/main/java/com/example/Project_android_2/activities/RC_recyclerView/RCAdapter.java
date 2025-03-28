package com.example.Project_android_2.activities.RC_recyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.activities.RC_recyclerView.category_model;
import com.example.Project_android_2.activities.RecyclerViewInterface;

import java.util.ArrayList;

public class RCAdapter extends RecyclerView.Adapter<RCAdapter.RCViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<category_model> modelArrayList;
    public RCAdapter(Context context, ArrayList<category_model> modelArrayList,RecyclerViewInterface recyclerViewInterface){
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rc_item,parent,false);
        return new RCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, @SuppressLint("RecyclerView") int position) {
        category_model rcModel = modelArrayList.get(position);
        holder.rc_title.setText(rcModel.getTITLE());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewInterface.onItemClick(modelArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class RCViewHolder extends RecyclerView.ViewHolder {
        TextView rc_title;
        CardView cardview;
        public RCViewHolder(@NonNull View itemView){
            super(itemView);
            rc_title = itemView.findViewById(R.id.rc_title);
            cardview = itemView.findViewById(R.id.card_of_topic_comic);
        }
    }
}