package com.example.distribuidorank.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.distribuidorank.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "pos.db";
    private static String DATABASE_SCRIPT;
    private SQLiteDatabase DB;

    public DataBaseHelper(Context _context) {
        super(_context, DATABASE_NAME, null, DATABASE_VERSION);
        DATABASE_SCRIPT = readFile(_context);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase database) {
        DB = database;
        StringTokenizer tokens = new StringTokenizer(DATABASE_SCRIPT, ";");

        DB.beginTransaction();
        while(tokens.hasMoreTokens()){
            DB.execSQL(tokens.nextToken());
        }

        DB.setTransactionSuccessful();
        DB.endTransaction();
        //DB.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onCreate(db);
    }
    
    @NonNull
    private static String readFile(@NonNull Context context) {

        StringBuilder fileAsString = null;
        String currentLine;
        InputStream stream = context.getResources().openRawResource(R.raw.db);

            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(stream))) {
                while ((currentLine = buffer.readLine()) != null) {
                    if (fileAsString == null) {
                        fileAsString = new StringBuilder(currentLine);
                    } else {
                        fileAsString.append("\n").append(currentLine);
                    }
                }
                if (fileAsString != null) {
                    return fileAsString.toString();
                } else {
                    Toast.makeText(context, "El script de la base de datos esta en blanco", Toast.LENGTH_LONG).show();
                    return "";
                }
            } catch (IOException ioException) {
                Toast.makeText(context, "Error en el archivo que contiene el script de la base de datos", Toast.LENGTH_LONG).show();
                return "";
            }
    }
    }