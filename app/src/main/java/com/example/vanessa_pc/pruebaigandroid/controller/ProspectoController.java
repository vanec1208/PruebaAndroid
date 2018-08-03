package com.example.vanessa_pc.pruebaigandroid.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.vanessa_pc.pruebaigandroid.entidades.Prospecto;
import com.example.vanessa_pc.pruebaigandroid.model.PruebaTables;

import java.util.ArrayList;

public class ProspectoController extends Controller {

    public ProspectoController(Context context) {
        super(context);
    }

    public boolean hasProspectoTable(){
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" +
                        PruebaTables.ProspectosTable.TABLE_NAME + "'",null);

        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    public void insertProspecto(String documento, String nombre, String apellido, String telefono, int estado){
        ContentValues values = new ContentValues();
        values.put(PruebaTables.ProspectosTable.COL_DOCUMENTO, documento);
        values.put(PruebaTables.ProspectosTable.COL_NOMBRE, nombre);
        values.put(PruebaTables.ProspectosTable.COL_APELLIDO, apellido);
        values.put(PruebaTables.ProspectosTable.COL_TELEFONO, telefono);
        values.put(PruebaTables.ProspectosTable.COL_ESTADO, estado);

        db.insert(PruebaTables.ProspectosTable.TABLE_NAME, null, values);
    }

    public void updateProspecto(String documento, String nombre, String apellido, String telefono, int estado){
        ContentValues values = new ContentValues();
        values.put(PruebaTables.ProspectosTable.COL_NOMBRE, nombre);
        values.put(PruebaTables.ProspectosTable.COL_APELLIDO, apellido);
        values.put(PruebaTables.ProspectosTable.COL_TELEFONO, telefono);
        values.put(PruebaTables.ProspectosTable.COL_ESTADO, estado);

        db.update(PruebaTables.ProspectosTable.TABLE_NAME, values,
                PruebaTables.ProspectosTable.COL_DOCUMENTO + " = '" + documento + "'", null);
    }

    public void deleteProspectoAll(){
        db.delete(PruebaTables.ProspectosTable.TABLE_NAME, null, null);
    }

    public ArrayList<Prospecto> getListProspectos(){
        String[] col = {
                PruebaTables.ProspectosTable.COL_DOCUMENTO,     // 0
                PruebaTables.ProspectosTable.COL_NOMBRE,        // 1
                PruebaTables.ProspectosTable.COL_APELLIDO,      // 2
                PruebaTables.ProspectosTable.COL_TELEFONO,      // 3
                PruebaTables.ProspectosTable.COL_ESTADO         // 4
        };

        Cursor cursor = db.query(PruebaTables.ProspectosTable.TABLE_NAME, col, null,
                null, null, null, null);

        ArrayList<Prospecto> listProspectos = new ArrayList<>();

        while (cursor.moveToNext()) {
            Prospecto prospecto = new Prospecto();
            prospecto.setCedula(cursor.getString(0));
            prospecto.setNombre(cursor.getString(1));
            prospecto.setApellido(cursor.getString(2));
            prospecto.setTelefono(cursor.getString(3));
            prospecto.setEstado(cursor.getInt(4));

            listProspectos.add(prospecto);
        }

        cursor.close();
        return listProspectos;
    }
}
