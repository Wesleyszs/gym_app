package com.example.gym_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gym_app.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeClienteActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private DatabaseHelper databaseHelper;
    private TextView statusConsultaTextView, horaConsultaTextView, dataConsultaTextView, profissionalConsultaTextView;
    private CheckBox checkBoxSeg, checkBoxTer, checkBoxQua, checkBoxQui, checkBoxSex, checkBoxSab, checkBoxDom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);

        sharedPreferences = getSharedPreferences("SemanaPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkBoxSeg = findViewById(R.id.checkBoxSeg);
        checkBoxTer = findViewById(R.id.checkBoxTer);
        checkBoxQua = findViewById(R.id.checkBoxQua);
        checkBoxQui = findViewById(R.id.checkBoxQui);
        checkBoxSex = findViewById(R.id.checkBoxSex);
        checkBoxSab = findViewById(R.id.checkBoxSab);
        checkBoxDom = findViewById(R.id.checkBoxDom);

        statusConsultaTextView = findViewById(R.id.status_consulta);
        horaConsultaTextView = findViewById(R.id.hora_consulta);
        dataConsultaTextView = findViewById(R.id.data_consulta);
        profissionalConsultaTextView = findViewById(R.id.profissional_consulta);

        databaseHelper = new DatabaseHelper(this);

        loadCheckBoxStates();

        checkBoxSeg.setOnClickListener(v -> saveCheckBoxState("seg", checkBoxSeg.isChecked()));
        checkBoxTer.setOnClickListener(v -> saveCheckBoxState("ter", checkBoxTer.isChecked()));
        checkBoxQua.setOnClickListener(v -> saveCheckBoxState("qua", checkBoxQua.isChecked()));
        checkBoxQui.setOnClickListener(v -> saveCheckBoxState("qui", checkBoxQui.isChecked()));
        checkBoxSex.setOnClickListener(v -> saveCheckBoxState("sex", checkBoxSex.isChecked()));
        checkBoxSab.setOnClickListener(v -> saveCheckBoxState("sab", checkBoxSab.isChecked()));
        checkBoxDom.setOnClickListener(v -> saveCheckBoxState("dom", checkBoxDom.isChecked()));

        Button buttonSchedule = findViewById(R.id.button_schedule);
        Button buttonWorkout = findViewById(R.id.button_workout);
        Button buttonResetReport = findViewById(R.id.button_reset_report);
        Button cancelarConsultaButton = findViewById(R.id.cancelar_consulta);

        // Button to navigate to MarcacaoActivity
        buttonSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(HomeClienteActivity.this, MarcacaoActivity.class);
            startActivity(intent);
        });

        // Placeholder button for workouts
        buttonWorkout.setOnClickListener(v -> {
            Toast.makeText(HomeClienteActivity.this, "Em desenvolvimento...", Toast.LENGTH_SHORT).show();
        });

        // Button to reset weekly checkboxes
        buttonResetReport.setOnClickListener(v -> {
            resetCheckBoxStates();
            Toast.makeText(HomeClienteActivity.this, "Relatório semanal resetado", Toast.LENGTH_SHORT).show();
        });

        // Button to cancel the consultation
        cancelarConsultaButton.setOnClickListener(v -> cancelarConsulta());

        // Check if a consultation info was passed from MarcacaoActivity
        String consultaInfo = getIntent().getStringExtra("consulta_info");
        if (consultaInfo != null) {
            statusConsultaTextView.setText("Consulta marcada com sucesso!");
            profissionalConsultaTextView.setText("Profissional: " + consultaInfo.split(" com ")[1]);
            horaConsultaTextView.setText("Hora: " + consultaInfo.split(" às ")[1].split(" com ")[0]);
            dataConsultaTextView.setText("Data: " + consultaInfo.split(" às ")[0]);
        } else {
            // Load existing consultation details for today
            String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
            showConsultaInfo(todayDate);
        }
    }

    private void saveCheckBoxState(String key, boolean isChecked) {
        editor.putBoolean(key, isChecked);
        editor.apply();
    }

    private void loadCheckBoxStates() {
        checkBoxSeg.setChecked(sharedPreferences.getBoolean("seg", false));
        checkBoxTer.setChecked(sharedPreferences.getBoolean("ter", false));
        checkBoxQua.setChecked(sharedPreferences.getBoolean("qua", false));
        checkBoxQui.setChecked(sharedPreferences.getBoolean("qui", false));
        checkBoxSex.setChecked(sharedPreferences.getBoolean("sex", false));
        checkBoxSab.setChecked(sharedPreferences.getBoolean("sab", false));
        checkBoxDom.setChecked(sharedPreferences.getBoolean("dom", false));
    }

    private void resetCheckBoxStates() {
        checkBoxSeg.setChecked(false);
        checkBoxTer.setChecked(false);
        checkBoxQua.setChecked(false);
        checkBoxQui.setChecked(false);
        checkBoxSex.setChecked(false);
        checkBoxSab.setChecked(false);
        checkBoxDom.setChecked(false);
        editor.clear().apply();
    }

    // Method to show the consultation info
    private void showConsultaInfo(String date) {
        Cursor cursor = databaseHelper.getConsultaDetalhes(date);
        if (cursor.moveToFirst()) {
            int statusIndex = cursor.getColumnIndex("status");
            int horaIndex = cursor.getColumnIndex("hora");
            int profissionalIndex = cursor.getColumnIndex("profissional");

            if (statusIndex != -1 && horaIndex != -1 && profissionalIndex != -1) {
                String status = cursor.getString(statusIndex);
                String hora = cursor.getString(horaIndex);
                String profissional = cursor.getString(profissionalIndex);

                statusConsultaTextView.setText("Status: " + status);
                horaConsultaTextView.setText("Hora: " + hora);
                dataConsultaTextView.setText("Data: " + date);
                profissionalConsultaTextView.setText("Profissional: " + profissional);
            } else {
                statusConsultaTextView.setText("Status: Livre");
                horaConsultaTextView.setText("Hora: --:--");
                dataConsultaTextView.setText("Data: " + date);
                profissionalConsultaTextView.setText("Profissional: --");
            }
        } else {
            statusConsultaTextView.setText("Status: Livre");
            horaConsultaTextView.setText("Hora: --:--");
            dataConsultaTextView.setText("Data: " + date);
            profissionalConsultaTextView.setText("Profissional: --");
        }
    }

    // Method to cancel a consultation
    private void cancelarConsulta() {
        String data = dataConsultaTextView.getText().toString().split(": ")[1];
        databaseHelper.updateConsulta(data, "", "", "", "livre");
        Toast.makeText(HomeClienteActivity.this, "Consulta cancelada com sucesso.", Toast.LENGTH_SHORT).show();
        showConsultaInfo(data);
    }
}
