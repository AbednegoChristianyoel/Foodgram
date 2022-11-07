package com.example.foodgram.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodgram.Adapter.CarouselAdapter;
import com.example.foodgram.Adapter.MyPhotosAdapter;
import com.example.foodgram.Adapter.UserAdapter;
import com.example.foodgram.Model.Post;
import com.example.foodgram.Model.User;
import com.example.foodgram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ExploreFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List<Post> mPostAppetizer, mPostMainCourse, mPostDessert, mPostMinuman;

    ViewPager2 appetizerView, mainCourseView, dessertView, minumanView;
    private CarouselAdapter AdapterAppetizer, AdapterMainCourse, AdapterDessert, AdapterMinuman;


    EditText search_bar;
    LinearLayout explore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_bar = view.findViewById(R.id.search_bar);
        explore = view.findViewById(R.id.explore);

        readUsers();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mUsers = new ArrayList<>();
        mPostAppetizer = new ArrayList<>();
        mPostMainCourse = new ArrayList<>();
        mPostDessert = new ArrayList<>();
        mPostMinuman = new ArrayList<>();

        AdapterAppetizer = new CarouselAdapter(mPostAppetizer);
        AdapterMainCourse = new CarouselAdapter(mPostMainCourse);
        AdapterDessert = new CarouselAdapter(mPostDessert);
        AdapterMinuman = new CarouselAdapter(mPostMinuman);
        userAdapter = new UserAdapter(getContext(), mUsers);

        recyclerView.setAdapter(userAdapter);

        appetizerView = view.findViewById(R.id.appetizerView);
        mainCourseView = view.findViewById(R.id.mainCourseView);
        dessertView = view.findViewById(R.id.dessertView);
        minumanView = view.findViewById(R.id.minumanView);

        settingCarousel(AdapterAppetizer, appetizerView);
        settingCarousel(AdapterMainCourse, mainCourseView);
        settingCarousel(AdapterDessert, dessertView);
        settingCarousel(AdapterMinuman, minumanView);

        carouselViewer("Appetizer", AdapterAppetizer, mPostAppetizer);
        carouselViewer("Main Course", AdapterMainCourse, mPostMainCourse);
        carouselViewer("Dessert", AdapterDessert, mPostDessert);
        carouselViewer("Minuman", AdapterMinuman, mPostMinuman);

        return view;
    }

    private void searchUsers(String s) {
        explore.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (search_bar.getText().toString().equals("")) {
                    recyclerView.setVisibility(View.GONE);
                    explore.setVisibility(View.VISIBLE);
                }
                mUsers.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                    mUsers.add(user);
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (search_bar.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        User user = snapshot1.getValue(User.class);
                        mUsers.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void carouselViewer(String jenismakanan, CarouselAdapter adaptermakanan, List<Post> listmakanan) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = reference.orderByChild("jenismakanan").equalTo(jenismakanan);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listmakanan.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    listmakanan.add(post);
                }

                adaptermakanan.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void settingCarousel(CarouselAdapter adaptermakanan, ViewPager2 carouselmakanan) {
        carouselmakanan.setAdapter(adaptermakanan);

        carouselmakanan.setClipToPadding(false);
        carouselmakanan.setClipChildren(false);
        carouselmakanan.setOffscreenPageLimit(3);
        carouselmakanan.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.2f);
            }
        });

        carouselmakanan.setPageTransformer(compositePageTransformer);
    }
}