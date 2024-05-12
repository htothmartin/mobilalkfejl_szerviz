package com.example.mobilalkfejl_serviceapp;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {
    private android.os.Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            logout(null);
        }
    };

    public void logout(View view) {
        handler.removeCallbacks(runnable);
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.postDelayed(runnable, 5 * 60 * 1000);
    }
}
