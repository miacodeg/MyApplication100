package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.adapters.TaskAdapter;
import com.example.data.AppData;
import com.example.myapplication100.R;
import com.example.myapplication100.ToDoModel;
import com.example.myapplication100.thirdActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity {

    RecyclerView taskRecyclerView;
    TaskAdapter adapter;
    AppData data;
    ArrayList<ToDoModel> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        adapter = new TaskAdapter(this);
        data = new AppData(this);

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setAdapter(adapter);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(TaskListActivity.this));

        getAllTasks();
    }

    private void getAllTasks() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query;
        if(data.isAdmin()) {
            query = db.collection("tasks-list").whereEqualTo("created_by", data.getUserId());
        } else {
            query = db.collection("tasks-list").whereEqualTo("assigned_to", data.getEmail());
        }

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(TaskListActivity.this, "CANNOT GET TASK. TRY AGAIN.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(task.getResult().isEmpty()) {
                    Toast.makeText(TaskListActivity.this, "NO TASK AVAILABLE", Toast.LENGTH_SHORT).show();
                    return;
                }

                for(DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    Map<String, Object> list = snapshot.getData();
                    String id = list.get("id").toString();
                    String title = list.get("title").toString();
                    String desc = list.get("description").toString();
                    ToDoModel model = new ToDoModel(id, title, desc);
                    tasks.add(model);
                }
                adapter.setTasks(tasks);
            }
        });
    }
}