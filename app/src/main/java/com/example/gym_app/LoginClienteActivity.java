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

public class LoginClienteActivity extends AppCompatActivity {

    private EditText email, password;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cliente);

        db = new DatabaseHelper(this);

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        TextView textViewRegister = findViewById(R.id.textViewRegister);

        buttonLogin.setOnClickListener(v -> {
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                Log.d("LoginClienteActivity", "Campos vazios");
            } else if (!db.checkEmailExistsCliente(userEmail)) {
                Toast.makeText(this, "E-mail não registrado", Toast.LENGTH_SHORT).show();
                Log.d("LoginClienteActivity", "E-mail não registrado: " + userEmail);
            } else if (db.checkCliente(userEmail, userPassword)) {
                Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();
                Log.d("LoginClienteActivity", "Login bem-sucedido: " + userEmail);
                // Redirecionar para a tela inicial dos clientes
                Intent intent = new Intent(LoginClienteActivity.this, HomeClienteActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                Log.d("LoginClienteActivity", "Senha incorreta para: " + userEmail);
            }
        });

        textViewRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginClienteActivity.this, RegisterClienteActivity.class);
            startActivity(intent);
        });
    }
}
