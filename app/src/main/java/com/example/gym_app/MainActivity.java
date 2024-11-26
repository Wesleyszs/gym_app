package com.example.gym_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gym_app.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        Button buttonProfissional = findViewById(R.id.button_profissional);
        Button buttonAluno = findViewById(R.id.button_aluno);

        buttonProfissional.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        buttonAluno.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginClienteActivity.class);
            startActivity(intent);
        });
    }
}
