package com.example.vanessa_pc.pruebaigandroid.comm;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vanessa_pc.pruebaigandroid.util.Preferences;
import com.example.vanessa_pc.pruebaigandroid.util.Urls;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginClient extends AsyncTask<String, String, String[]> {
    public final static String TAG = "LoginClient";

    private Context context;
    String email;
    String password;

    private OnTaskListener listener;

    public LoginClient(Context context, String email, String password) {
        this.context = context;
        this.email = email;
        this.password = password;
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

        String resURI = Urls.getMainUrl() + Urls.getLoginUrl();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(resURI).newBuilder();
        urlBuilder.addQueryParameter("email", email);
        urlBuilder.addQueryParameter("password", password);

        Request request = new Request.Builder()
                .url(urlBuilder.build().toString())
                .build();

        try {
            Response response = client.newCall(request).execute();
            int code = response.code();
            String message = response.message();

            JSONObject jsonRespuesta = new JSONObject(response.body().string());
            response.body().close();

            if (code == 200) {
                //Guardar Token en Preference
                String token = jsonRespuesta.getString("authToken");
                Preferences.setPreferenceValue(context, Preferences.PREF_TOKEN, token);

                return new String[]{"1", "Datos guardados"};
            } else {
                if(code == 422){
                    String strError = "Email y/o Password no válido";
                    Log.e(TAG, strError);
                    return new String[]{"0", strError};
                }else {
                    String strError = "Error: " + code + " " + message;
                    return new String[]{"0", strError};
                }
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return new String[]{"0", e.toString()};
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.toString() : e.getMessage());
            return new String[]{"0", e.getMessage() == null ? e.toString() : e.getMessage()};
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);

        if (result[0].equals("0")) {
            Log.e(TAG, result[1]);
            listener.showMessage(result[1]);
        } else if (result[0].equals("1")) {
            listener.onLoginFinish(email, password);
        }
    }

    public interface OnTaskListener {
        void showMessage(String message);
        void onLoginFinish(String email, String password);
    }

}
