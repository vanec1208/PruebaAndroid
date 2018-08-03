package com.example.vanessa_pc.pruebaigandroid.model;

import android.provider.BaseColumns;

public final class PruebaTables {

    public PruebaTables() {}

    public static abstract class ProspectosTable implements BaseColumns {
        public static final String TABLE_NAME = "PROSPECTOS";
        public static final String COL_DOCUMENTO = "ID_PROSPECTO";
        public static final String COL_NOMBRE = "NOMBRE";
        public static final String COL_APELLIDO = "APELLIDO";
        public static final String COL_TELEFONO = "TELEFONO";
        public static final String COL_ESTADO = "ESTADO";

        static final String SQL_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_DOCUMENTO + " TEXT PRIMARY KEY, " +
                        COL_NOMBRE + " TEXT, " +
                        COL_APELLIDO + " TEXT, " +
                        COL_TELEFONO + " TEXT, " +
                        COL_ESTADO + " INTEGER " +
                        ")";
    }
}
