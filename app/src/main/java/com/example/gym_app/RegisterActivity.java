package com.example.gym_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gym_app.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password, confirmPassword;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        confirmPassword = findViewById(R.id.editTextConfirmPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewBackToLogin = findViewById(R.id.textViewBackToLogin);

        buttonRegister.setOnClickListener(v -> {
            String userName = name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String userConfirmPassword = confirmPassword.getText().toString().trim();

            if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity", "Campos vazios");
            } else if (db.checkEmailExistsProfissional(userEmail)) {
                Toast.makeText(this, "E-mail já registrado", Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity", "E-mail já registrado: " + userEmail);
            } else if (!userPassword.equals(userConfirmPassword)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity", "Senhas não coincidem para: " + userEmail);
            } else {
                boolean isInserted = db.addProfissional(userName, userEmail, userPassword);
                if (isInserted) {
                    Toast.makeText(this, "Registro bem-sucedido", Toast.LENGTH_SHORT).show();
                    Log.d("RegisterActivity", "Registro bem-sucedido para: " + userEmail);
                    // Redirecionar para a tela de login
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Falha no registro", Toast.LENGTH_SHORT).show();
                    Log.d("RegisterActivity", "Falha no registro para: " + userEmail);
                }
            }
        });

        textViewBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
