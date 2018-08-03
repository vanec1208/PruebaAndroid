package com.example.vanessa_pc.pruebaigandroid.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.vanessa_pc.pruebaigandroid.model.PruebaDb;

public class Controller {
    protected Context context;
    protected SQLiteDatabase db;

    public Controller(Context context) {
        this.context = context;
        db = PruebaDb.getInstance(context);
    }
}
