package com.example.mobilalkfejl_serviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MenuActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int SECRET_KEY = 45;
    ListView list;


    private FirestoreRepository db;
    private FirebaseFirestore mFirestore;

    private CollectionReference mServices;

    private ArrayList<ServiceItem> mServiceItems;
    private ArrayAdapter<ServiceItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if(secret_key != 45){
            Log.d(LOG_TAG,"Wrong secret key!");
            finish();
        }

        list = findViewById(R.id.serviceListView);
        mFirestore = FirebaseFirestore.getInstance();
        mServices = mFirestore.collection("ServiceOptions");
        db = FirestoreRepository.getInstance();
        mServiceItems = new ArrayList<>();

        adapter = new ServiceListItemAdapter(this, mServiceItems);
        list.setAdapter(adapter);



        queryData();
    }

    private void queryData(){

        db.getServiceList(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    ServiceItem item = document.toObject(ServiceItem.class);
                    mServiceItems.add(item);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void startProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}