package com.example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication100.R;
import com.example.myapplication100.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {

    ArrayList<ToDoModel> tasks;
    Context context;
    LayoutInflater inflater;

    public TaskAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setTasks(ArrayList<ToDoModel> todoList) {
        this.tasks = todoList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.task_list_custom_layout, parent, false);
        return new TaskHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        ToDoModel task = tasks.get(position);
        holder.title.setText(task.getTaskName());
        holder.desc.setText(task.getTaskDescription());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {

        TextView title, desc;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.taskName);
            desc = itemView.findViewById(R.id.taskDescription);
        }
    }
}
