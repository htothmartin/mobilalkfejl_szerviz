package com.example.mobilalkfejl_serviceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final int SECRET_KEY = 45;

    EditText userNameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;
    EditText phoneET;

    private FirebaseAuth mAuth;
    private FirestoreRepository db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirestoreRepository.getInstance();

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if(secret_key != 45){
            finish();
        }

        userNameET = findViewById(R.id.editTextUsername);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);
        passwordAgainET = findViewById(R.id.editTextPasswordAgain);
        phoneET = findViewById(R.id.editTextPhone);

    }

    public void register(View view){
        String username = userNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();
        String phone = phoneET.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String userId = mAuth.getCurrentUser().getUid();
                    User newUser = new User(userId, username, email, phone);

                    db.createUser(newUser, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(LOG_TAG, "Uj user sikeresen hozzaasva!");
                            } else {
                                Log.w(LOG_TAG, "Uj user hozzaadasa sikertelen!");
                            }
                        }
                    });
                    Log.d(LOG_TAG,"User created!");
                    startMenu();

                } else {
                    Log.d(LOG_TAG, "User not created!");
                    Log.d(LOG_TAG, "Failed login!");
                    Toast.makeText(RegisterActivity.this, "Sikertelen regisztracio: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void startMenu(){
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }
}