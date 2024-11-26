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

public class RegisterClienteActivity extends AppCompatActivity {

    private EditText name, email, password, confirmPassword;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cliente);

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
                Log.d("RegisterClienteActivity", "Campos vazios");
            } else if (db.checkEmailExistsCliente(userEmail)) {
                Toast.makeText(this, "E-mail já registrado", Toast.LENGTH_SHORT).show();
                Log.d("RegisterClienteActivity", "E-mail já registrado: " + userEmail);
            } else if (!userPassword.equals(userConfirmPassword)) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                Log.d("RegisterClienteActivity", "Senhas não coincidem para: " + userEmail);
            } else {
                boolean isInserted = db.addCliente(userName, userEmail, userPassword);
                if (isInserted) {
                    Toast.makeText(this, "Registro bem-sucedido", Toast.LENGTH_SHORT).show();
                    Log.d("RegisterClienteActivity", "Registro bem-sucedido para: " + userEmail);
                    // Redirecionar para a tela de login
                    Intent intent = new Intent(RegisterClienteActivity.this, LoginClienteActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Falha no registro", Toast.LENGTH_SHORT).show();
                    Log.d("RegisterClienteActivity", "Falha no registro para: " + userEmail);
                }
            }
        });

        textViewBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterClienteActivity.this, LoginClienteActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
