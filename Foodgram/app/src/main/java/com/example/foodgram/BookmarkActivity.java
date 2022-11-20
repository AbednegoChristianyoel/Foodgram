package com.example.foodgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.foodgram.Adapter.MyPhotosAdapter;
import com.example.foodgram.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    RecyclerView recycler_view;

    MyPhotosAdapter myPhotosAdapter;
    List<Post> postList;
    List<String> bookmarkList;

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userid = firebaseUser.getUid();

        recycler_view = findViewById(R.id.recycler_view);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recycler_view.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        bookmarkList = new ArrayList<>();
        myPhotosAdapter = new MyPhotosAdapter(getApplicationContext(), postList);
        recycler_view.setAdapter(myPhotosAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Markah");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.backicon_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        showBookmarkPhoto();
    }

    private void showBookmarkPhoto(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookmark").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookmarkList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    bookmarkList.add(dataSnapshot.getKey());
                }
                readBookmark();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readBookmark(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);
                    for (String id : bookmarkList){
                        if (post.getPostid().equals(id)){
                            postList.add(post);
                        }
                    }
                    myPhotosAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
