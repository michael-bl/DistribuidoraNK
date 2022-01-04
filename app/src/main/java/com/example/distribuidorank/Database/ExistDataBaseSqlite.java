package com.example.distribuidorank.Database;

import java.io.File;

public class ExistDataBaseSqlite {
    private final File DB_FILE = new File("/data/data/com.example.distribuidorank/databases/pos.db");
    public ExistDataBaseSqlite(){
    }

    public boolean existeDataBase(){
        return DB_FILE.exists();
    }
}
