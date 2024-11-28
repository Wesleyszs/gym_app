package com.example.gym_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gym_app.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MonitoramentoActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView statusConsultaTextView, consultaInfoTextView;
    private DatabaseHelper databaseHelper;
    private LinearLayout consultaMarcadaLegend;
    private LinearLayout indisponivelLegend;
    private String selectedDate; // Data selecionada no calendário
    private Locale locale = new Locale("pt", "BR"); // Locale para português do Brasil
    private String loggedInProfissional; // Armazena o profissional logado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoramento);

        // Configuração do Locale para português
        Locale.setDefault(locale);

        // Inicializar componentes do layout
        calendarView = findViewById(R.id.calendarView);
        statusConsultaTextView = findViewById(R.id.textViewTitle);
        consultaInfoTextView = findViewById(R.id.textViewConsultaInfo);
        consultaMarcadaLegend = findViewById(R.id.consultaMarcadaLegend);
        indisponivelLegend = findViewById(R.id.indisponivelLegend);

        // Inicializar o banco de dados
        databaseHelper = new DatabaseHelper(this);

        // Simulação do profissional logado (substitua com lógica real de login)
        loggedInProfissional = "ProfissionalExemplo";

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
                Toast.makeText(MonitoramentoActivity.this,
                        selectedDate + " - Status: " + status,
                        Toast.LENGTH_LONG).show();

                // Abrir um diálogo para editar o status
                showEditStatusDialog(selectedDate, status);
            }
        });
    }

    // Atualiza o status da consulta na interface
    private void updateConsultaStatus(String status) {
        switch (status) {
            case "marcada":
                consultaMarcadaLegend.setVisibility(LinearLayout.VISIBLE);
                indisponivelLegend.setVisibility(LinearLayout.GONE);
                statusConsultaTextView.setText("Consulta Marcada");
                loadConsultaDetalhes(selectedDate);
                break;
            case "indisponível":
                consultaMarcadaLegend.setVisibility(LinearLayout.GONE);
                indisponivelLegend.setVisibility(LinearLayout.VISIBLE);
                statusConsultaTextView.setText("Data Indisponível");
                break;
            case "cancelada":
                consultaMarcadaLegend.setVisibility(LinearLayout.GONE);
                indisponivelLegend.setVisibility(LinearLayout.GONE);
                statusConsultaTextView.setText("Consulta Cancelada");
                Cursor cursor = databaseHelper.getConsultaDetalhes(selectedDate);
                if (cursor.moveToFirst()) {
                    int motivoIndex = cursor.getColumnIndex("motivo_cancelamento");
                    if (motivoIndex != -1) {
                        String motivoCancelamento = cursor.getString(motivoIndex);
                        consultaInfoTextView.setText("Motivo do cancelamento: " + motivoCancelamento);
                    }
                }
                break;
            default:
                consultaMarcadaLegend.setVisibility(LinearLayout.GONE);
                indisponivelLegend.setVisibility(LinearLayout.GONE);
                statusConsultaTextView.setText("Data Livre");
                consultaInfoTextView.setText("");
                break;
        }
    }

    // Carregar detalhes da consulta
    private void loadConsultaDetalhes(String date) {
        Cursor cursor = databaseHelper.getConsultaDetalhes(date);
        if (cursor.moveToFirst()) {
            int horaIndex = cursor.getColumnIndex("hora");
            int clienteIndex = cursor.getColumnIndex("cliente");

            if (horaIndex != -1 && clienteIndex != -1) {
                String hora = cursor.getString(horaIndex);
                String cliente = cursor.getString(clienteIndex);
                consultaInfoTextView.setText("Hora: " + hora + "\nCliente: " + cliente);
            } else {
                consultaInfoTextView.setText("Informações da consulta não disponíveis.");
            }
        } else {
            consultaInfoTextView.setText("Nenhuma consulta marcada.");
        }
    }

    // Exibe um diálogo para editar o status da data
    private void showEditStatusDialog(String date, String currentStatus) {
        // Opções de status
        String[] options = {"Disponível", "Indisponível", "Consulta Marcada", "Cancelar Consulta"};

        // Determinar a seleção inicial com base no status atual
        int checkedItem = -1;
        switch (currentStatus) {
            case "livre":
                checkedItem = 0;
                break;
            case "indisponível":
                checkedItem = 1;
                break;
            case "marcada":
                checkedItem = 2;
                break;
            case "cancelada":
                checkedItem = 3;
                break;
        }

        // Criar o diálogo
        new AlertDialog.Builder(this)
                .setTitle("Editar Status da Data: " + date)
                .setSingleChoiceItems(options, checkedItem, null)
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedOption = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        String newStatus;

                        switch (selectedOption) {
                            case 0:
                                newStatus = "livre";
                                break;
                            case 1:
                                newStatus = "indisponível";
                                break;
                            case 2:
                                newStatus = "marcada";
                                break;
                            case 3:
                                showCancelDialog(date);
                                return; // Não prosseguir com as outras ações
                            default:
                                newStatus = "livre";
                                break;
                        }

                        // Atualizar o status no banco de dados
                        databaseHelper.updateConsulta(date, "", loggedInProfissional, "", newStatus);

                        // Atualizar a interface com o novo status
                        updateConsultaStatus(newStatus);

                        // Mostrar uma mensagem ao usuário
                        Toast.makeText(MonitoramentoActivity.this,
                                "Status atualizado para: " + newStatus,
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Exibir diálogo para cancelar consulta com motivo
    private void showCancelDialog(String date) {
        final EditText input = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Consulta")
                .setMessage("Digite o motivo do cancelamento:")
                .setView(input)
                .setPositiveButton("Cancelar Consulta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String motivo = input.getText().toString();
                        if (motivo.isEmpty()) {
                            Toast.makeText(MonitoramentoActivity.this,
                                    "Por favor, insira um motivo.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Hora e profissional são necessários para cancelar a consulta
                            String hora = "10:00"; // Obtém a hora desejada (substitua por lógica real)
                            String profissional = loggedInProfissional;

                            databaseHelper.cancelarConsulta(date, hora, profissional, motivo);
                            updateConsultaStatus("cancelada");
                            Toast.makeText(MonitoramentoActivity.this,
                                    "Consulta cancelada com sucesso.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Voltar", null)
                .show();
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
