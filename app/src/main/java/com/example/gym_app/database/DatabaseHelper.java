package com.example.gym_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gymlab.db";
    private static final int DATABASE_VERSION = 1;

    // Tabelas
    private static final String TABLE_PROFISSIONAIS = "profissionais";
    private static final String TABLE_CLIENTES = "clientes";

    // Colunas comuns
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Criação das tabelas
    private static final String TABLE_CREATE_PROFISSIONAIS =
            "CREATE TABLE " + TABLE_PROFISSIONAIS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT NOT NULL, " +
                    COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

    private static final String TABLE_CREATE_CLIENTES =
            "CREATE TABLE " + TABLE_CLIENTES + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT NOT NULL, " +
                    COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT NOT NULL);";

    // Construtor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_PROFISSIONAIS); // Cria a tabela de profissionais
        db.execSQL(TABLE_CREATE_CLIENTES); // Cria a tabela de clientes
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFISSIONAIS); // Atualiza o esquema do banco de profissionais
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTES); // Atualiza o esquema do banco de clientes
        onCreate(db);
    }

    // Adicionar profissional ao banco
    public boolean addProfissional(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtém o banco para escrita
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        // Insere os dados na tabela de profissionais
        long result = db.insert(TABLE_PROFISSIONAIS, null, values);

        // Verifica se a inserção foi bem-sucedida
        return result != -1;
    }

    // Adicionar cliente ao banco
    public boolean addCliente(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Obtém o banco para escrita
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);

        // Insere os dados na tabela de clientes
        long result = db.insert(TABLE_CLIENTES, null, values);

        // Verifica se a inserção foi bem-sucedida
        return result != -1;
    }

    // Validar login do profissional
    public boolean checkProfissional(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase(); // Obtém o banco para leitura

        // Seleciona o ID do profissional com o email e senha fornecidos
        String[] columns = { COLUMN_USER_ID };
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(
                TABLE_PROFISSIONAIS,    // Nome da tabela
                columns,        // Colunas a selecionar
                selection,      // Condição WHERE
                selectionArgs,  // Argumentos do WHERE
                null,           // Agrupamento
                null,           // Filtro de grupo
                null            // Ordenação
        );

        int count = cursor.getCount(); // Conta os resultados encontrados
        cursor.close(); // Fecha o cursor
        return count > 0; // Retorna true se encontrou pelo menos um usuário
    }

    // Validar login do cliente
    public boolean checkCliente(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase(); // Obtém o banco para leitura

        // Seleciona o ID do cliente com o email e senha fornecidos
        String[] columns = { COLUMN_USER_ID };
        String selection = COLUMN_USER_EMAIL + " = ? AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(
                TABLE_CLIENTES,    // Nome da tabela
                columns,        // Colunas a selecionar
                selection,      // Condição WHERE
                selectionArgs,  // Argumentos do WHERE
                null,           // Agrupamento
                null,           // Filtro de grupo
                null            // Ordenação
        );

        int count = cursor.getCount(); // Conta os resultados encontrados
        cursor.close(); // Fecha o cursor
        return count > 0; // Retorna true se encontrou pelo menos um usuário
    }

    // Checar se o e-mail já existe na tabela de profissionais
    public boolean checkEmailExistsProfissional(String email) {
        SQLiteDatabase db = this.getReadableDatabase(); // Obtém o banco para leitura

        // Seleciona o ID do profissional com o email fornecido
        String[] columns = { COLUMN_USER_ID };
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                TABLE_PROFISSIONAIS,    // Nome da tabela
                columns,        // Colunas a selecionar
                selection,      // Condição WHERE
                selectionArgs,  // Argumentos do WHERE
                null,           // Agrupamento
                null,           // Filtro de grupo
                null            // Ordenação
        );

        int count = cursor.getCount(); // Conta os resultados encontrados
        cursor.close(); // Fecha o cursor
        return count > 0; // Retorna true se encontrou o email
    }

    // Checar se o e-mail já existe na tabela de clientes
    public boolean checkEmailExistsCliente(String email) {
        SQLiteDatabase db = this.getReadableDatabase(); // Obtém o banco para leitura

        // Seleciona o ID do cliente com o email fornecido
        String[] columns = { COLUMN_USER_ID };
        String selection = COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                TABLE_CLIENTES,    // Nome da tabela
                columns,        // Colunas a selecionar
                selection,      // Condição WHERE
                selectionArgs,  // Argumentos do WHERE
                null,           // Agrupamento
                null,           // Filtro de grupo
                null            // Ordenação
        );

        int count = cursor.getCount(); // Conta os resultados encontrados
        cursor.close(); // Fecha o cursor
        return count > 0; // Retorna true se encontrou o email
    }
}
