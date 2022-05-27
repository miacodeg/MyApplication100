package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.AppData;
import com.example.myapplication100.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        //admin and admin

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"All fields must be filled.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this,"Please wait...",Toast.LENGTH_SHORT).show();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.signInWithEmailAndPassword( username.getText().toString().toLowerCase().trim(),  password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String uid = task.getResult().getUser().getUid().toString();

                        // get user from database
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    return;
                                }
                                if(!task.getResult().exists()) {
                                    Toast.makeText(MainActivity.this,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    return;
                                }
                                Map<String, Object> user = task.getResult().getData();
                                String name = user.get("name").toString();
                                String email = user.get("email").toString();
                                String userType = user.get("user_type").toString();
                                AppData data = new AppData(MainActivity.this);
                                data.SaveUser(uid, name, email, userType);
                                Toast.makeText(MainActivity.this,"LOGIN SUCCESSFUL: "+name,Toast.LENGTH_SHORT).show();
                                opendisplay(userType == "admin");
                            }
                        });

                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {
            AppData data = new AppData(this);
            opendisplay(data.isAdmin());
        }
    }

    public void forgotpasswordclick(View view) {
        if(username.getText().toString().equals("")){
            Toast.makeText(MainActivity.this,"Enter your email address.",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(MainActivity.this,"Please wait...",Toast.LENGTH_SHORT).show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(username.getText().toString().toLowerCase().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this,"Password reset instructions sent to your email.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void opendisplay(boolean isAdmin) {
        if(isAdmin) {
            Intent intent = new Intent(this, WelcomeActivity.class);
            startActivity(intent);
            return;
        }
        // change to technician view
        Intent intent = new Intent(this, TaskListActivity.class);
        startActivity(intent);
    }

    public void createAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }


}
