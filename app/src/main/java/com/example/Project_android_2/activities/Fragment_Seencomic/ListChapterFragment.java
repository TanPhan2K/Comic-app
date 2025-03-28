package com.example.Project_android_2.activities.Fragment_Seencomic;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Project_android_2.R;
import com.example.Project_android_2.activities.RC_recyclerView.RCAdapter_Chap;
import com.example.Project_android_2.activities.RC_recyclerView.RCModel_Chap;
import com.example.Project_android_2.activities.ViewComic;
import com.example.Project_android_2.models.chapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListChapterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListChapterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private StorageReference storageReference;
    ArrayList<RCModel_Chap> modelArrayList;
    private RCAdapter_Chap rcAdapter;
    private DatabaseReference databaseReference;

    public ListChapterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListChapterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListChapterFragment newInstance(String param1, String param2) {
        ListChapterFragment fragment = new ListChapterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_chapter, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.rc_chapter);
        TextView textView = rootView.findViewById(R.id.count_chpater);
        FrameLayout progressBar = rootView.findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        String idComic = getArguments().getString(ARG_PARAM1);


        modelArrayList = new ArrayList<>();
        RCAdapter_Chap adapter = new RCAdapter_Chap(getContext(), modelArrayList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("chapter");
        Query query = databaseReference.orderByChild("ID_COMIC").equalTo(idComic);
        progressBar.setVisibility(View.VISIBLE);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = 0;
                    for (DataSnapshot chapterSnapshot : snapshot.getChildren()) {
                        String id = chapterSnapshot.getKey();
                        String content = chapterSnapshot.child("CONTENT").getValue(String.class);
                        String title = chapterSnapshot.child("TITLE").getValue(String.class);
                        Long chapterIndex = chapterSnapshot.child("CHAPTER_INDEX").getValue(Long.class);
                        String index = chapterIndex + "";
                        String create_At = chapterSnapshot.child("create_At").getValue(String.class);
                        chapter Chapter = new chapter(id, title, index, content, idComic, create_At);
                        RCModel_Chap rc = new RCModel_Chap(Chapter);
                        modelArrayList.add(rc);
                        count++;
                    }
                    textView.setText("Số chương (" + count + ")");
                }

                int size = (int) snapshot.getChildrenCount();
                Collections.sort(modelArrayList);
                rcAdapter = new RCAdapter_Chap(getContext(), modelArrayList);
                recyclerView.setAdapter(rcAdapter);
                rcAdapter.notifyDataSetChanged();
                rcAdapter.setOnItemClickListener(new RCAdapter_Chap.OnItemClickListener() {
                    @Override
                    public void onItemClick(chapter position) {
                        Intent intent = new Intent(getContext(), ViewComic.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("ID", String.valueOf(position.getId()));
                        bundle.putString("CHAPTER_INDEX", position.getIndex());
                        bundle.putString("ID_COMIC", position.getId_comic());
                        bundle.putString("TITLE", position.getTitle());
                        bundle.putInt("SIZE", size);
                        intent.putExtras(bundle);
                        //intent.putExtras(bundle1);
                        startActivity(intent);
                    }
                });
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


        return rootView;
    }

}