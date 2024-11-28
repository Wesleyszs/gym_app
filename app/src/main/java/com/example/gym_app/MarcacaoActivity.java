package com.example.gym_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gym_app.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MarcacaoActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView statusConsultaTextView;
    private TimePicker timePicker;
    private Spinner spinnerProfissionais;
    private DatabaseHelper databaseHelper;
    private String selectedDate; // Data selecionada no calendário
    private Locale locale = new Locale("pt", "BR"); // Locale para português do Brasil
    private String loggedInCliente; // Armazena o cliente logado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcacao);

        // Configuração do Locale para português
        Locale.setDefault(locale);

        // Inicializar componentes do layout
        calendarView = findViewById(R.id.calendarView);
        statusConsultaTextView = findViewById(R.id.textViewTitle);
        timePicker = findViewById(R.id.timePicker);
        spinnerProfissionais = findViewById(R.id.spinner_profissionais);
        Button marcarConsultaButton = findViewById(R.id.button_marcar_consulta);
        Button desmarcarConsultaButton = findViewById(R.id.button_desmarcar_consulta);

        // Inicializar o banco de dados
        databaseHelper = new DatabaseHelper(this);

        // Simulação do cliente logado (substitua com lógica real de login)
        loggedInCliente = "ClienteExemplo";

        // Preencher o Spinner com a lista de profissionais
        List<String> profissionais = databaseHelper.getProfissionais();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profissionais);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfissionais.setAdapter(adapter);

        // Configurar o evento de clique no calendário
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Obter a data selecionada
                selectedDate = getFormattedDate(year, month, dayOfMonth);

                // Consultar o status da data no banco
                String status = databaseHelper.getConsultaStatus(selectedDate);

                // Atualizar o status na interface
                updateConsultaStatus(status);

                // Mostrar um Toast com o dia da semana e o status
                Toast.makeText(MarcacaoActivity.this,
                        selectedDate + " - Status: " + status,
                        Toast.LENGTH_LONG).show();
            }
        });

        // Configurar o botão de marcar consulta
        marcarConsultaButton.setOnClickListener(v -> {
            if (selectedDate != null) {
                String currentStatus = databaseHelper.getConsultaStatus(selectedDate);
                if (!"livre".equals(currentStatus)) {
                    Toast.makeText(MarcacaoActivity.this,
                            "A data selecionada não está disponível para marcação.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Obter a hora selecionada
                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();
                    String time = String.format(locale, "%02d:%02d", hour, minute);

                    // Obter o profissional selecionado
                    String profissional = (String) spinnerProfissionais.getSelectedItem();

                    // Atualizar o banco de dados com a nova consulta
                    String consultaInfo = selectedDate + " às " + time + " com " + profissional;
                    databaseHelper.updateConsulta(selectedDate, time, profissional, loggedInCliente, "marcada");

                    Toast.makeText(MarcacaoActivity.this,
                            "Consulta marcada com sucesso para " + consultaInfo,
                            Toast.LENGTH_SHORT).show();
                    updateConsultaStatus("marcada");
                }
            } else {
                Toast.makeText(MarcacaoActivity.this,
                        "Por favor, selecione uma data no calendário.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar o botão de desmarcar consulta
        desmarcarConsultaButton.setOnClickListener(v -> {
            if (selectedDate != null) {
                // Obter a hora e o profissional selecionado para desmarcar
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String time = String.format(locale, "%02d:%02d", hour, minute);
                String profissional = (String) spinnerProfissionais.getSelectedItem();

                // Atualizar o banco de dados para cancelar a consulta
                desmarcarConsulta(selectedDate, time, profissional);

                Toast.makeText(MarcacaoActivity.this,
                        "Consulta desmarcada com sucesso.",
                        Toast.LENGTH_SHORT).show();
                updateConsultaStatus("livre");
            } else {
                Toast.makeText(MarcacaoActivity.this,
                        "Por favor, selecione uma data no calendário.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Atualiza o status da consulta na interface
    private void updateConsultaStatus(String status) {
        switch (status) {
            case "marcada":
                statusConsultaTextView.setText("Consulta Marcada");
                break;
            case "indisponível":
                statusConsultaTextView.setText("Data Indisponível");
                break;
            default:
                statusConsultaTextView.setText("Data Livre");
                break;
        }
    }

    // Método auxiliar para desmarcar consulta
    private void desmarcarConsulta(String data, String hora, String profissional) {
        databaseHelper.updateConsulta(data, hora, profissional, "", "livre");
    }

    // Retorna a data formatada com o dia da semana e o mês em português
    private String getFormattedDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.set(year, month, dayOfMonth);

        // Formatar a data
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", locale);
        return dateFormat.format(calendar.getTime());
    }
}
