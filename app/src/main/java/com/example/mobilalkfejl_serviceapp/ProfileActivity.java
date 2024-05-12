package com.example.mobilalkfejl_serviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private FirestoreRepository db;
    private FirebaseAuth mAuth;

    private FirebaseFirestore mFireStore;
    private CollectionReference userCollection;
    private CollectionReference bookingCollection;

    private ArrayList<Booking> mBookings;
    private ArrayAdapter<Booking> adapter;
    private User currentUser;

    ListView list;

    TextView usernameTV;
    TextView emailTV;
    TextView phoneTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if(secret_key != 45){
            Log.d(LOG_TAG,"Wrong secret key!");
            finish();
        }
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        userCollection = mFireStore.collection("Users");
        bookingCollection = mFireStore.collection("Bookings");

        usernameTV = findViewById(R.id.username);
        emailTV = findViewById(R.id.email);
        phoneTV = findViewById(R.id.phone);


        if(mAuth.getCurrentUser() != null){
            userCollection.whereEqualTo("id", mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            currentUser = document.toObject(User.class);
                            Log.d(LOG_TAG, "Sikeres user lekeres!");
                            usernameTV.setText(String.valueOf(currentUser.getUsername()));
                            emailTV.setText(String.valueOf("Email: " + currentUser.getEmail()));
                            phoneTV.setText(String.valueOf("Telefonszam: " + currentUser.getPhone()));
                        }
                    } else {
                        Log.d(LOG_TAG, "Error getting documents: ", task.getException());
                    }
                }
            });

        }



        list = findViewById(R.id.bookingListView);
        db = FirestoreRepository.getInstance();
        mBookings = new ArrayList<>();

        adapter = new BookingsListAdapter(this, mBookings);
        list.setAdapter(adapter);


        queryData(mAuth.getCurrentUser().getUid());

    }

    private void queryData(String id){
//        bookingCollection.whereEqualTo("user_id", id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Booking b = document.toObject(Booking.class);
//                        Log.d(LOG_TAG, "Sikeres booking lekeres!");
//                        Log.d(LOG_TAG, b.getDate().toString());
//                        mBookings.add(b);
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
        db.getBookingListByUserId(id, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    Booking booking = document.toObject(Booking.class);
                    mBookings.add(booking);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void getCurrentUser(String id){
        db.getUserById(id, queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                currentUser = queryDocumentSnapshots.getDocuments().get(0).toObject(User.class);
            }
        });
    }

    public void toMain(View view) {
        finish();
    }
}