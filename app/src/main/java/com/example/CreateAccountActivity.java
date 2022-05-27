package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        AppData data = new AppData(CreateAccountActivity.this);

        EditText username = findViewById(R.id.username);
        EditText email = findViewById(R.id.emailaddress);
        EditText password = findViewById(R.id.password);

        MaterialButton createbtn = (MaterialButton) findViewById(R.id.loginbtn);

        createbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("") || password.getText().toString().equals("") || email.getText().toString().equals("")){
                    Toast.makeText(CreateAccountActivity.this,"All fields must be filled.",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(CreateAccountActivity.this,"Please wait...",Toast.LENGTH_SHORT).show();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.createUserWithEmailAndPassword( email.getText().toString().toLowerCase().trim(),  password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(CreateAccountActivity.this,"REGISTRATION FAILED !!!",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String uid = task.getResult().getUser().getUid().toString();
                        Map<String, String> user = new HashMap<>();
                        user.put("id", uid);
                        user.put("name", username.getText().toString());
                        user.put("email",  email.getText().toString());
                        user.put("user_type", "technician");



                        // store user to database
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(uid).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(CreateAccountActivity.this,"REGISTRATION SUCCESSFUL",Toast.LENGTH_SHORT).show();
                                data.SaveUser(uid, username.getText().toString(), email.getText().toString(), "technician");
                                opendisplay(false);
                            }
                        });

                    }
                });

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

    public void openlogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}