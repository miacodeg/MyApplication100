package com.example.myapplication100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.MainActivity;
import com.example.TaskListActivity;
import com.example.data.AppData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class thirdActivity extends AppCompatActivity {

    EditText ed1,ed2,ed3, ed4, ed5;
    Button b1,b2;
    AppData data;
    String taskType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new AppData(this);

        ed1 = findViewById(R.id.assign);
        ed2 = findViewById(R.id.tittle);
        ed3 = findViewById(R.id.desc);
        ed4 = findViewById(R.id.SD);
        ed5 = findViewById(R.id.CD);

        b1 = findViewById(R.id.bt1);
        b2 = findViewById(R.id.bt2);

        taskType = getIntent().getStringExtra("taskName");
        if(taskType == null) {
            taskType = "";
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> newTask = new HashMap<>();
                newTask.put("assigned_to", ed1.getText().toString());
                newTask.put("title", ed2.getText().toString());
                newTask.put("description", ed3.getText().toString());
                newTask.put("start_date", ed4.getText().toString());
                newTask.put("complete_date", ed5.getText().toString());
                newTask.put("status", "pending");
                newTask.put("created_by", data.getUserId());
                newTask.put("task_name", taskType);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String id = db.collection("tasks-list").document().getId();
                newTask.put("id", id);

                db.collection("tasks-list").document(id).set(newTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(thirdActivity.this, "FAILED TO ADD TASK", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(thirdActivity.this, "TASK ADDED SUCCESSFULY", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            class view {
            }

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TaskListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!data.isAdmin()) {
            Toast.makeText(thirdActivity.this, "FOR ADMIN USE ONLY", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}