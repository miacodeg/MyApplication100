package com.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.data.AppData;
import com.example.myapplication100.R;
import com.example.myapplication100.thirdActivity;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    AppData data;
    TextView welcome;

    CardView cardViewElectronics, cardViewMechanics, cardViewTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        data = new AppData(this);
        welcome = findViewById(R.id.signin);
        welcome.setText("Welcome "+data.getUsername());

        cardViewElectronics = findViewById(R.id.cardView1);
        cardViewMechanics = findViewById(R.id.cardView2);
        cardViewTrailer = findViewById(R.id.cardView3);

        cardViewElectronics.setOnClickListener(this);
        cardViewMechanics.setOnClickListener(this);
        cardViewTrailer.setOnClickListener(this);
    }

    public void addtask(String taskName) {
        Intent intent = new Intent(WelcomeActivity.this, thirdActivity.class);
        intent.putExtra("taskName", taskName);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.cardView1) {
            addtask("Electronics");
        }
        if(id == R.id.cardView2) {
            addtask("Mechanics");
        }
        if(id == R.id.cardView3) {
            addtask("Trailer");
        }
    }
}