package com.example.Project_android_2.activities.RC_recyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.models.chapter;


import java.util.ArrayList;
import java.util.Collections;

public class RCAdapter_Chap extends RecyclerView.Adapter<RCAdapter_Chap.RCViewHolder>{
    Context context;
    ArrayList<RCModel_Chap> modelArrayList;

    private OnItemClickListener listener;
    public RCAdapter_Chap(Context context, ArrayList<RCModel_Chap> modelArrayList){
        this.context = context;
        this.modelArrayList = modelArrayList;
    }
    // Interface cho việc xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(chapter position);
    }

    // Set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public RCAdapter_Chap.RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rc_item_comic_chap,parent,false);
        return new RCAdapter_Chap.RCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RCAdapter_Chap.RCViewHolder holder, int position) {
        //Collections.sort(modelArrayList);
        RCModel_Chap rcModel = modelArrayList.get(position);
        holder.rc_chap.setText(String.valueOf(position + 1)+"  "+rcModel.getChap().getTitle());
        holder.create_At.setText("       ("+rcModel.getChap().getCreate_At()+")");

        Log.d("test","test"+holder.rc_chap.getText().toString());

        holder.rc_chap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    listener.onItemClick(rcModel.getChap());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class RCViewHolder extends RecyclerView.ViewHolder {
        TextView rc_chap,create_At;

        public RCViewHolder(@NonNull View itemView){
            super(itemView);
            rc_chap = itemView.findViewById(R.id.textViewItemChap);
            create_At = itemView.findViewById(R.id.create_At);
        }

    }

    public ArrayList<String> getChapList() {
        ArrayList<String> chapList = new ArrayList<>();
        for (RCModel_Chap rcModel : modelArrayList) {
            chapList.add("Chap: " + rcModel.getChap().getIndex());
        }
        return chapList;
    }
}