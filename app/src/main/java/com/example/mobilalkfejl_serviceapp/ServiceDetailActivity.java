package com.example.mobilalkfejl_serviceapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ServiceDetailActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static final int SECRET_KEY = 45;



    TextView nameTV;
    TextView priceTV;
    TextView decriptionTV;
    TextView timeTV;
    TextView selectedDate;
    Spinner timeSpinner;

    private Date selectedDate2;

    private FirebaseAuth mAuth;
    private FirestoreRepository db;

    private NotificationHandler mNotificationHandler;

    private ServiceItem serviceItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_detail);

        serviceItem = (ServiceItem) getIntent().getSerializableExtra("SERVICE");
        mAuth = FirebaseAuth.getInstance();
        db = FirestoreRepository.getInstance();
        mNotificationHandler = new NotificationHandler(this);


        nameTV = findViewById(R.id.service_name);
        priceTV = findViewById(R.id.service_price);
        decriptionTV = findViewById(R.id.service_description);
        timeTV = findViewById(R.id.service_time);
        selectedDate = findViewById(R.id.selected_date);
        timeSpinner = findViewById(R.id.time_spinner);

        nameTV.setText(String.valueOf(serviceItem.getName()));
        decriptionTV.setText(String.valueOf(serviceItem.getDescription()));
        timeTV.setText(String.valueOf("Idő: " + serviceItem.getTime() + " perc"));
        priceTV.setText(String.valueOf("Ár: " + serviceItem.getPrice() + " Ft"));

    }


    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view2, year, month, dayOfMonth) -> {
                    List<String> timeSlots = new ArrayList<>(Arrays.asList("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00"));

                    String selected = year + " " + (month + 1) + " " + dayOfMonth;
                    Log.d(LOG_TAG, "Selected date: " + selected);

                    this.selectedDate.setText(String.valueOf("Választott dátum: " + selected) );
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth, 0,0,0);
                    selectedDate2 = calendar.getTime();

                    ArrayList<Booking> bookedToThisDate = new ArrayList<>();
                    db.getAvailableTime(selectedDate2, new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Booking b = document.toObject(Booking.class);
                                if(Objects.equals(b.getService_id(), serviceItem.getId()))
                                    bookedToThisDate.add(b);
                            }
                            Log.d(LOG_TAG, String.valueOf(bookedToThisDate.size()));
                            for(Booking b : bookedToThisDate){
                                timeSlots.remove(b.getTime());
                            }
                            Log.d(LOG_TAG, timeSlots.toString());

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ServiceDetailActivity.this, android.R.layout.simple_spinner_item, timeSlots);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            timeSpinner.setAdapter(adapter);
                            if (!timeSlots.isEmpty()) {
                                timeSpinner.setSelection(0);  // Az első elem lesz az alapértelmezett kiválasztott elem
                            }
                        }
                    });
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void book(View view) {
        if(selectedDate2 == null && timeSpinner.getSelectedItem() == null){
            Toast.makeText(ServiceDetailActivity.this, "Kérem válasszon időpontot!", Toast.LENGTH_LONG).show();
        } else {
            Log.d(LOG_TAG, selectedDate2.toString());
            Booking newBooking = new Booking("", mAuth.getCurrentUser().getUid(), serviceItem.getId(), selectedDate2, timeSpinner.getSelectedItem().toString(), serviceItem.getName());
            Toast.makeText(ServiceDetailActivity.this, "Sikeres foglalás!", Toast.LENGTH_LONG).show();
            mNotificationHandler.send(newBooking.getService_name());
            Log.d(LOG_TAG, newBooking.toString());
            db.createBooking(newBooking, new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ServiceDetailActivity.this, "Sikeres foglalás!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(ServiceDetailActivity.this, "Sikertelen foglalás!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void startProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }
}