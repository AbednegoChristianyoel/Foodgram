package com.example.foodgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodgram.Model.Notification;
import com.example.foodgram.Model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PostDetail extends AppCompatActivity {


    private ImageView postimage, like, test;
    private TextView judul, description, bahanres, carares, jenis, likes;
    private String postid;
    private Map<String, String> add;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postid = preferences.getString("postid","none");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        judul = findViewById(R.id.judul);
        postimage = findViewById(R.id.image);
        description = findViewById(R.id.description);
        bahanres = findViewById(R.id.bahanres);
        carares = findViewById(R.id.carares);
        jenis = findViewById(R.id.jenis);
        likes = findViewById(R.id.likes);
        like = findViewById(R.id.like);
        test = findViewById(R.id.test);



        showData();

    }

    private void showData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getApplicationContext() == null){
                    return;
                }

                Post post = snapshot.getValue(Post.class);
                Notification notification = snapshot.getValue(Notification.class);

                judul.setText(post.getJudul());
                Glide.with(getApplicationContext()).load(post.getPostimage()).into(postimage);
                description.setText(post.getDescription().replace("\\n", "\n"));
                bahanres.setText(post.getBahanres().replace("\\n", "\n"));
                carares.setText(post.getCarares().replace("\\n", "\n"));
                jenis.setText(post.getJenismakanan());

                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(like.getTag().equals("like")){
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                                    .child(firebaseUser.getUid()).setValue(true);
                            addNotifications(post.getPublisher(), post.getPostid());
                            add = ServerValue.TIMESTAMP;

                        }else if(like.getTag().equals("liked")){
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                                    .child(firebaseUser.getUid()).removeValue();
                            //deleteNotifications(post.getPostid(), firebaseUser.getUid(), post.getPublisher());
                        }
                    }
                });

                test.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //deleteNotifications(post.getPostid(), firebaseUser.getUid(), post.getPublisher());
                    }
                });


                nrLikes(likes, post.getPostid());
                isLikes(post.getPostid(), like);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void nrLikes(final TextView likes, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLikes(String postid, ImageView imageView){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.heartfill);
                    imageView.setTag("liked");

                }else{
                    imageView.setImageResource(R.drawable.heart);
                    imageView.setTag("like");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addNotifications(String notifto, String postid){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(notifto);

        String notifid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("userid",firebaseUser.getUid());
        hashMap.put("text", "liked your post");
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);
        hashMap.put("notifid", notifid);
        hashMap.put("test", firebaseUser.getUid() + postid);

        reference.orderByChild("test").equalTo(firebaseUser.getUid() + postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
//                    snapshot.getRef().removeValue();
                }else{
                    reference.child(notifid).setValue(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


//    private void deleteNotifications(final String postid, String userid, String publisherid){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    if(snapshot.child("userid").getValue().equals(publisherid)){
//                        if(snapshot.child("postid").getValue().equals(postid)){
//                            snapshot.getRef().removeValue();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

}