package com.example.vanessa_pc.pruebaigandroid.util;

import android.content.Context;
import android.widget.Toast;

import com.example.vanessa_pc.pruebaigandroid.entidades.Estado;
import com.example.vanessa_pc.pruebaigandroid.entidades.Prospecto;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validaciones {

    public boolean isCorreoValido(String email){
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]*.com$");

        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    public ArrayList<Estado> getListEstados(){
        ArrayList<Estado> listEstados = new ArrayList<>();

        //Pending
        Estado estado = new Estado(EstadoProspecto.PENDING, "Pendiente");
        listEstados.add(estado);

        //Approved
        estado = new Estado(EstadoProspecto.APPROVED, "Aprobado");
        listEstados.add(estado);

        //Accepted
        estado = new Estado(EstadoProspecto.ACCEPTED, "Aceptado");
        listEstados.add(estado);

        //Rejected
        estado = new Estado(EstadoProspecto.REJECTED, "Rechazado");
        listEstados.add(estado);

        //Disabled
        estado = new Estado(EstadoProspecto.DISABLED, "Deshabilitado");
        listEstados.add(estado);

        return listEstados;
    }

    public String fechaActual(){
        final Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));

        if(month.length() == 1) month = "0" + month;
        if(day.length() == 1) day = "0" + day;

        return year + "-" + month + "-" + day;
    }

    public void saveJsonLog(Context context, Prospecto pAnterior, Prospecto pActual){
        JSONObject jsonObject = new JSONObject();

        try {
            //Creación de JSON

            //Json con valores anteriores
            JSONObject jsonAnterior = new JSONObject();
            jsonAnterior.put("NOMBRE", pAnterior.getNombre());
            jsonAnterior.put("APELLIDO", pAnterior.getApellido());
            jsonAnterior.put("TELEFONO", pAnterior.getTelefono());
            jsonAnterior.put("ESTADO", pAnterior.getEstado());
            jsonObject.put("ANTERIOR", jsonAnterior);

            //Json con valores nuevos
            JSONObject jsonNuevo = new JSONObject();
            jsonNuevo.put("NOMBRE", pActual.getNombre());
            jsonNuevo.put("APELLIDO", pActual.getApellido());
            jsonNuevo.put("TELEFONO", pActual.getTelefono());
            jsonNuevo.put("ESTADO", pActual.getEstado());
            jsonObject.put("NUEVO", jsonNuevo);

            //Documento Prospecto
            jsonObject.put("DOCUMENTO", pActual.getCedula());

            //Correo usuario
            String email = Preferences.getPreferenceValue(context, Preferences.PREF_EMAIL);
            jsonObject.put("USER_MODIFICACION", email);

            //Fecha Actual
            Date currentTime = Calendar.getInstance().getTime();
            String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
            jsonObject.put("FECHA_MODIFICACION", currentDateTime);

            //Guardar JSON en Log
            FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                    .tag("ProspectosTag")
                    .build();

            Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
            Logger.json(jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
