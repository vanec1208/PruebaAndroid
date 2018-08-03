package com.example.vanessa_pc.pruebaigandroid.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PruebaDb {
    private static SQLiteDatabase INSTANCE = null;

    private PruebaDb(){}

    private synchronized static void createInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PruebaHelper(context).getReadableDatabase();
        }
    }

    public static SQLiteDatabase getInstance(Context context) {
        if (INSTANCE == null) createInstance(context);
        return INSTANCE;
    }
}
