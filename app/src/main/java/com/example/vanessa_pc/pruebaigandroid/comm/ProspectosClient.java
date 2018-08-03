package com.example.vanessa_pc.pruebaigandroid.comm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vanessa_pc.pruebaigandroid.controller.ProspectoController;
import com.example.vanessa_pc.pruebaigandroid.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProspectosClient  extends AsyncTask<String, String, String[]> {
    public final static String TAG = "ProspectosClient";

    private Context context;
    String token;

    private OnTaskListener listener;

    public ProspectosClient(Context context, String token) {
        this.context = context;
        this.token = token;
        this.listener = (OnTaskListener) context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String[] doInBackground(String... strings) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(0, TimeUnit.SECONDS)
                .writeTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS)
                .build();

        String resURI = Urls.getMainUrl() + Urls.getProspectosUrl();

        Request request = new Request.Builder()
                .url(resURI)
                .header("TOKEN", token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            int code = response.code();
            String message = response.message();

            String strResponse = response.body().string();
            response.body().close();

            if (code == 200) {
                Object json = new JSONTokener(strResponse).nextValue();

                if(json instanceof JSONArray){
                    JSONArray jsonArray = new JSONArray(strResponse);
                    guardarProspectos(jsonArray);
                    return new String[]{"1", "Datos guardados"};
                }else {
                    return new String[]{"0", "Token no válido"};
                }
            } else {
                String strError = "Error: " + code + " " + message;
                return new String[]{"0", strError};
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return new String[]{"0", e.toString()};
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.toString() : e.getMessage());
            return new String[]{"0", e.getMessage() == null ? e.toString() : e.getMessage()};
        }
    }

    public void guardarProspectos(JSONArray jsonArray){
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                String strDocumento = object.getString("id");
                String strNombre = object.getString("name");
                String strApellido = object.getString("surname");
                String strTelefono = object.getString("telephone");
                int estado = object.getInt("statusCd");

                new ProspectoController(context).insertProspecto(strDocumento, strNombre, strApellido, strTelefono, estado);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);

        if (result[0].equals("0")) {
            Log.e(TAG, result[1]);
            listener.showMessageP(result[1]);
        } else if (result[0].equals("1")) {
            listener.onLoginFinishP();
        }
    }

    public interface OnTaskListener {
        void showMessageP(String message);
        void onLoginFinishP();
    }
}