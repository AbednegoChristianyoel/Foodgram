package com.example.foodgram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodgram.Fragment.BottomNav;
import com.example.foodgram.Fragment.HomeFragment;
import com.example.foodgram.Model.Comments;
import com.example.foodgram.Model.Post;
import com.example.foodgram.Model.User;
import com.example.foodgram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    public Context mContext;
    public List<Comments> mComments;

    FirebaseUser firebaseUser;

    public CommentsAdapter(Context mContext, List<Comments> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comments_item, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Comments comments = mComments.get(position);

        getUserInfo(holder.image_profile, holder.fullname, comments.getPublisher());
        holder.comments.setText(comments.getComment());
        holder.fullname.setText(comments.getPublisher());

        holder.comments.setOnClickListener(view ->  {

                Intent intent = new Intent(mContext, BottomNav.class);
                intent.putExtra("publisherid", comments.getPublisher());
                mContext.startActivity(intent);
        });

        holder.image_profile.setOnClickListener(view ->  {

                Intent intent = new Intent(mContext, BottomNav.class);
                intent.putExtra("publisherid", comments.getPublisher());
                mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_profile;
        TextView fullname, comments;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            fullname = itemView.findViewById(R.id.fullname);
            comments = itemView.findViewById(R.id.comments);
        }
    }

    private void getUserInfo(final ImageView image_profile, TextView fullname, String publisherid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(image_profile);
                fullname.setText(user.getNama());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
