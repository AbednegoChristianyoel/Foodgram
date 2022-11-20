package com.example.foodgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodgram.Fragment.BottomNav;
import com.example.foodgram.Model.Notification;
import com.example.foodgram.Model.Post;
import com.example.foodgram.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PostDetailActivity extends AppCompatActivity {


    private ImageView postimage, like, back, comment, image_profile, settingPost, bookmark;
    private TextView judul, description, bahanres, carares, jenis, likes, comments, nama;
    private String postid;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postid = preferences.getString("postid", "none");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        judul = findViewById(R.id.judul);
        postimage = findViewById(R.id.image);
        description = findViewById(R.id.description);
        bahanres = findViewById(R.id.bahanres);
        carares = findViewById(R.id.carares);
        jenis = findViewById(R.id.jenis);
        likes = findViewById(R.id.likes);
        like = findViewById(R.id.like);
        back = findViewById(R.id.back);
        comments = findViewById(R.id.comments);
        comment = findViewById(R.id.comment);
        image_profile = findViewById(R.id.image_profile);
        settingPost = findViewById(R.id.settingPost);
        nama = findViewById(R.id.nama);
        bookmark = findViewById(R.id.bookmark);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(back.getAlpha() == 1){
                    onBackPressed();
                }
            }
        });

        showData();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showInfoPublisher(ImageView Image_profile, TextView nama, String userid ){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                Glide.with(getApplicationContext()).load(user.getImageurl()).into(Image_profile);
                nama.setText(user.getNama());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getApplicationContext() == null) {
                    return;
                }

                Post post = snapshot.getValue(Post.class);
                judul.setText(post.getJudul());
                Glide.with(getApplicationContext()).load(post.getPostimage()).into(postimage);
                description.setText(post.getDescription().replace("\\n", "\n"));
                bahanres.setText(post.getBahanres().replace("\\n", "\n"));
                carares.setText(post.getCarares().replace("\\n", "\n"));
                jenis.setText(post.getJenismakanan());

                showInfoPublisher(image_profile, nama, post.getPublisher());

                image_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), BottomNav.class);
                        intent.putExtra("publisherid", post.getPublisher());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }
                });

                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (like.getTag().equals("like") && like.getAlpha() > 0) {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                                    .child(firebaseUser.getUid()).setValue(true);
                            addNotifications(post.getPublisher(), post.getPostid());

                        } else if (like.getTag().equals("liked") && like.getAlpha() > 0) {
                            FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                                    .child(firebaseUser.getUid()).removeValue();
//                            deleteNotifications(post.getPublisher(), post.getPostid());
                        }
                    }
                });

                comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(comment.getAlpha() > 0) {
                            Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                            intent.putExtra("postid", post.getPostid());
                            intent.putExtra("publisherid", post.getPublisher());
                            startActivity(intent);
                        }
                    }
                });

                nrLikes(likes, post.getPostid());
                isLikes(post.getPostid(), like);
                getComments(post.getPostid(), comments);
                isBookmark(post.getPostid(), bookmark);

                bookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bookmark.getTag().equals("unbookmarked")) {
                            FirebaseDatabase.getInstance().getReference().child("Bookmark").child(firebaseUser.getUid())
                                    .child(post.getPostid()).setValue(true);
                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Bookmark").child(firebaseUser.getUid())
                                    .child(post.getPostid()).removeValue();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isBookmark(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Bookmark")
                .child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postid).exists()) {
                    imageView.setImageResource(R.drawable.bookmark_fill);
                    imageView.setTag("bookmarked");
                } else {
                    imageView.setImageResource(R.drawable.bookmark);
                    imageView.setTag("unbookmarked");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void nrLikes(final TextView likes, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likes.setText(snapshot.getChildrenCount() + " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLikes(String postid, ImageView imageView) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.heartfill);
                    imageView.setTag("liked");

                } else {
                    imageView.setImageResource(R.drawable.heart);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void deleteNotifications(String notifto, String postid){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(notifto);
//
//        reference.orderByChild("key").equalTo(firebaseUser.getUid() + postid).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    String test = dataSnapshot.getRef().getKey();
//
//                    FirebaseDatabase.getInstance().getReference().child("Notifications").child(notifto)
//                            .child(test).removeValue();
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void addNotifications(String notifto, String postid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(notifto);

        String notifid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "liked your post");
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);
        hashMap.put("notifid", notifid);
        hashMap.put("key", firebaseUser.getUid() + postid);

        reference.orderByChild("key").equalTo(firebaseUser.getUid() + postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    snapshot.getRef().removeValue();
                } else {
                    reference.child(notifid).setValue(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getComments(String postid, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Comments").child(postid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comments.setText(snapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}