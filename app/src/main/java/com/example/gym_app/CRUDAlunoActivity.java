package com.example.gym_app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gym_app.database.DatabaseHelper;

public class CRUDAlunoActivity extends AppCompatActivity {

    private EditText editTextSearch, editTextAlunoName, editTextAlunoEmail, editTextAlunoPassword;
    private LinearLayout layoutAlunoInfo;
    private DatabaseHelper db;

    private int alunoId = -1; // Variável para armazenar o ID do aluno selecionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudaluno);

        db = new DatabaseHelper(this);

        editTextSearch = findViewById(R.id.editTextSearch);
        editTextAlunoName = findViewById(R.id.editTextAlunoName);
        editTextAlunoEmail = findViewById(R.id.editTextAlunoEmail);
        editTextAlunoPassword = findViewById(R.id.editTextAlunoPassword);
        layoutAlunoInfo = findViewById(R.id.layoutAlunoInfo);

        Button buttonSearch = findViewById(R.id.buttonSearch);
        Button buttonUpdate = findViewById(R.id.buttonUpdate);
        Button buttonDelete = findViewById(R.id.buttonDelete);

        // Botão para pesquisar o aluno
        buttonSearch.setOnClickListener(v -> {
            String name = editTextSearch.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Digite um nome para pesquisar", Toast.LENGTH_SHORT).show();
            } else {
                searchAluno(name);
            }
        });

        // Botão para atualizar os dados do aluno
        buttonUpdate.setOnClickListener(v -> {
            String name = editTextAlunoName.getText().toString().trim();
            String email = editTextAlunoEmail.getText().toString().trim();
            String password = editTextAlunoPassword.getText().toString().trim();

            if (alunoId == -1) {
                Toast.makeText(this, "Nenhum aluno selecionado para atualização", Toast.LENGTH_SHORT).show();
            } else if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                boolean updated = db.updateCliente(alunoId, name, email, password);
                if (updated) {
                    Toast.makeText(this, "Dados atualizados com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erro ao atualizar os dados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botão para deletar o aluno
        buttonDelete.setOnClickListener(v -> {
            if (alunoId == -1) {
                Toast.makeText(this, "Nenhum aluno selecionado para exclusão", Toast.LENGTH_SHORT).show();
            } else {
                boolean deleted = db.deleteCliente(alunoId);
                if (deleted) {
                    Toast.makeText(this, "Aluno deletado com sucesso", Toast.LENGTH_SHORT).show();
                    layoutAlunoInfo.setVisibility(View.GONE);
                    clearAlunoFields();
                    alunoId = -1;
                } else {
                    Toast.makeText(this, "Erro ao deletar o aluno", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchAluno(String name) {
        Cursor cursor = db.getClienteByName(name);
        if (cursor != null && cursor.moveToFirst()) {
            alunoId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String alunoName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String alunoEmail = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String alunoPassword = cursor.getString(cursor.getColumnIndexOrThrow("password"));

            editTextAlunoName.setText(alunoName);
            editTextAlunoEmail.setText(alunoEmail);
            editTextAlunoPassword.setText(alunoPassword);
            layoutAlunoInfo.setVisibility(View.VISIBLE);

            cursor.close();
        } else {
            Toast.makeText(this, "Aluno não encontrado", Toast.LENGTH_SHORT).show();
            layoutAlunoInfo.setVisibility(View.GONE);
            clearAlunoFields();
        }
    }

    private void clearAlunoFields() {
        editTextAlunoName.setText("");
        editTextAlunoEmail.setText("");
        editTextAlunoPassword.setText("");
    }
}