package com.example.mobilalkfejl_serviceapp;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BookingsListAdapter extends ArrayAdapter<Booking> {
    private static final int SECRET_KEY = 45;
    private static final String LOG_TAG = MainActivity.class.getName();

    private FirestoreRepository db = FirestoreRepository.getInstance();


    public BookingsListAdapter(Context context, ArrayList<Booking> bookingItems){
        super(context, 0, bookingItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Booking booking = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bookinglist_layout, parent, false);
        }

        TextView serviceName = convertView.findViewById(R.id.service_name);

        db.getServiceById(booking.getService_id(), queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ServiceItem serviceItem = document.toObject(ServiceItem.class);
                serviceName.setText(serviceItem.getName());
            }
        });

        TextView dateTextView = convertView.findViewById(R.id.date);
        Button deleteButton = convertView.findViewById(R.id.delete_button);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String formattedDate = dateFormat.format(booking.getDate());
        dateTextView.setText(String.valueOf(formattedDate + " " + booking.getTime()));
        deleteButton.setOnClickListener(v -> {
            db.deleteBookingById(booking.getId(), task -> {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "Sikeres torles.");
                    remove(booking);
                    notifyDataSetChanged();
                } else {
                    Log.d(LOG_TAG, "Sikertelen torles.");
                }
            });
        });

        return convertView;
    }
}