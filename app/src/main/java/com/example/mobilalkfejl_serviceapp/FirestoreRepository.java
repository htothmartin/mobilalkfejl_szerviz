package com.example.mobilalkfejl_serviceapp;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class FirestoreRepository {

    private FirebaseFirestore db;
    private static FirestoreRepository instance;

    private FirestoreRepository(){
        this.db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirestoreRepository getInstance() {
        if (instance == null) {
            instance = new FirestoreRepository();
        }
        return instance;
    }

    public void createUser(User user, OnCompleteListener<Void> listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Task<Void> task = db.collection("Users").document(user.getId()).set(user);
            handler.post(() -> task.addOnCompleteListener(listener));
        });
    }

    public void getServiceList(OnSuccessListener<QuerySnapshot> listener){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Task<QuerySnapshot> task = db.collection("ServiceOptions").orderBy("name").get();
            handler.post(() -> task.addOnSuccessListener(listener));
        });
    }

    public void getBookingListByUserId(String id, OnSuccessListener<QuerySnapshot> listener){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Task<QuerySnapshot> task = db.collection("Bookings").whereEqualTo("user_id", id).get();
            handler.post(() -> task.addOnSuccessListener(listener));
        });
    }

    public void deleteBookingById(String id, OnCompleteListener<Void> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Bookings").document(id)
                .delete()
                .addOnCompleteListener(listener);
    }

    public void getAvailableTime(Date date, OnSuccessListener<QuerySnapshot> listener){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        // Normalizáld a dátumot
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date normalizedDate = calendar.getTime();
        Timestamp timestamp = new Timestamp(normalizedDate);
        Log.d(MainActivity.class.getName(), normalizedDate.toString());

        Calendar nextDay = Calendar.getInstance();
        nextDay.setTime(date);
        nextDay.add(Calendar.DATE, 1);
        Date nextDayDate = nextDay.getTime();
        Timestamp nextDayTimestamp = new Timestamp(nextDayDate);
        Log.d(MainActivity.class.getName(), nextDayDate.toString());

        executor.execute(() -> {
            Task<QuerySnapshot> task = db.collection("Bookings")
                    .whereGreaterThan("date", timestamp)
                    .whereLessThan("date", nextDayTimestamp)
                    .get();
            handler.post(() -> task.addOnSuccessListener(listener));
        });
    }





    public void getServiceById(String id, OnSuccessListener<QuerySnapshot> listener){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Task<QuerySnapshot> task = db.collection("ServiceOptions").whereEqualTo("id", id).get();
            handler.post(() -> task.addOnSuccessListener(listener));
        });
    }

    public void createBooking(Booking booking, OnCompleteListener<DocumentReference> listener){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Task<DocumentReference> task = db.collection("Bookings").add(booking);
            handler.post(() -> task.addOnCompleteListener(task2 -> {
                if (task.isSuccessful()) {
                    String id = task2.getResult().getId();
                    booking.setId(id);
                    db.collection("Bookings").document(id).set(booking);
                    listener.onComplete(task2);
                }
            }));
        });
    }

    public void getUserById(String id, OnSuccessListener<QuerySnapshot> listener){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Task<QuerySnapshot> task = db.collection("Users").whereEqualTo("id", id).get();
            handler.post(() -> task
                    .addOnSuccessListener(listener)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(MenuActivity.class.getName(), e.getMessage());
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.d(MenuActivity.class.getName(), "Error getting documents: ", task.getException());
                            }
                        }
                    }));
        });

    }




}
