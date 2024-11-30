package com.example.gym_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TimePicker;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gym_app.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MarcacaoActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner spinnerProfissionais;
    private TextView statusConsultaTextView;
    private DatabaseHelper databaseHelper;
    private String loggedInCliente; // Armazena o cliente logado
    private Locale locale = new Locale("pt", "BR"); // Locale para português do Brasil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcacao);

        // Configuração do Locale para português
        Locale.setDefault(locale);

        // Inicializar componentes do layout
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        spinnerProfissionais = findViewById(R.id.spinner_profissionais);
        statusConsultaTextView = findViewById(R.id.textViewTitle);
        Button marcarConsultaButton = findViewById(R.id.button_marcar_consulta);

        // Inicializar o banco de dados
        databaseHelper = new DatabaseHelper(this);

        // Simulação do cliente logado (substitua com lógica real de login)
        loggedInCliente = "ClienteExemplo";

        // Preencher o Spinner com a lista de profissionais
        List<String> profissionais = databaseHelper.getProfissionais();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profissionais);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfissionais.setAdapter(adapter);

        // Configurar o botão de marcar consulta
        marcarConsultaButton.setOnClickListener(v -> {
            try {
                // Obter a data selecionada no DatePicker
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                String selectedDate = getFormattedDate(year, month, day);

                // Obter o horário selecionado no TimePicker
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();

                // Obter o horário atual
                Calendar now = Calendar.getInstance();
                now.set(Calendar.SECOND, 0); // Ignorar segundos
                now.set(Calendar.MILLISECOND, 0); // Ignorar milissegundos

                // Configurar o horário selecionado
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.set(Calendar.SECOND, 0);
                selectedTime.set(Calendar.MILLISECOND, 0);
                selectedTime.set(year, month, day, hour, minute);

                // Log para verificar a data e horário selecionados
                Log.d("MarcacaoActivity", "Data selecionada: " + selectedDate);
                Log.d("MarcacaoActivity", "Hora selecionada: " + String.format("%02d:%02d", hour, minute));

                // Validar se o horário já passou
                if (selectedTime.before(now)) {
                    Toast.makeText(MarcacaoActivity.this,
                            "O horário selecionado já passou. Escolha outro horário.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar o status da consulta no banco de dados
                Log.d("MarcacaoActivity", "Verificando status da consulta para a data: " + selectedDate);
                String currentStatus = databaseHelper.getConsultaStatus(selectedDate);
                Log.d("MarcacaoActivity", "Status da consulta: " + currentStatus);

                if (!"livre".equals(currentStatus)) {
                    Toast.makeText(MarcacaoActivity.this,
                            "A data selecionada não está disponível para marcação.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Obter o profissional selecionado
                String profissional = (String) spinnerProfissionais.getSelectedItem();
                if (profissional == null || profissional.isEmpty()) {
                    Toast.makeText(MarcacaoActivity.this,
                            "Por favor, selecione um profissional.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Atualizar a consulta no banco de dados
                String time = String.format(locale, "%02d:%02d", hour, minute);
                String consultaInfo = selectedDate + " às " + time + " com " + profissional;

                Log.d("MarcacaoActivity", "Atualizando consulta no banco de dados com informações: " + consultaInfo);

                boolean success = databaseHelper.updateConsulta(selectedDate, time, profissional, loggedInCliente, "marcada");
                if (!success) {
                    Toast.makeText(MarcacaoActivity.this,
                            "Erro ao marcar a consulta. Tente novamente.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Notificar sucesso ao usuário
                Toast.makeText(MarcacaoActivity.this,
                        "Consulta marcada com sucesso para " + consultaInfo,
                        Toast.LENGTH_SHORT).show();

                // Navegar para a tela Home do cliente
                Intent intent = new Intent(MarcacaoActivity.this, HomeClienteActivity.class);
                intent.putExtra("consulta_info", consultaInfo);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("MarcacaoActivity", "Erro ao processar a marcação", e);
                Toast.makeText(MarcacaoActivity.this, "Erro ao processar a marcação. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Retorna a data formatada com o dia da semana e o mês em português
    private String getFormattedDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(locale);
        calendar.set(year, month, dayOfMonth);

        // Formatar a data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        return dateFormat.format(calendar.getTime());
    }
}
