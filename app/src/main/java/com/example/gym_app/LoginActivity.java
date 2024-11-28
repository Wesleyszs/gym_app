package com.example.gym_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gym_app.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        dbHelper = new DatabaseHelper(this);
    }

    public void onLogin(View view) {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
        } else {
            boolean loginValidoProfissional = dbHelper.checkProfissional(email, password);
            boolean loginValidoCliente = dbHelper.checkCliente(email, password);

            if (loginValidoProfissional) {
                navigateToMonitoramento();
            } else if (loginValidoCliente) {
                navigateToCliente();
            } else {
                Toast.makeText(this, "Login ou senha inválidos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void navigateToMonitoramento() {
        Intent intent = new Intent(LoginActivity.this, MonitoramentoActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToCliente() {
        Intent intent = new Intent(LoginActivity.this, HomeClienteActivity.class);
        startActivity(intent);
        finish();
    }

    public void onRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
