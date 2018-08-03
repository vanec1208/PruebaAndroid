package com.example.vanessa_pc.pruebaigandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.vanessa_pc.pruebaigandroid.comm.LoginClient;
import com.example.vanessa_pc.pruebaigandroid.gui.LoginActivity;
import com.example.vanessa_pc.pruebaigandroid.gui.ProfileActivity;
import com.example.vanessa_pc.pruebaigandroid.util.Conexion;
import com.example.vanessa_pc.pruebaigandroid.util.Preferences;

public class MainActivity extends AppCompatActivity implements LoginClient.OnTaskListener {
    private Context context;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        //Validar usuario guardado
        final String strEmail = Preferences.getPreferenceValue(context, Preferences.PREF_EMAIL);
        final String strPassword = Preferences.getPreferenceValue(context, Preferences.PREF_PASSWORD);
        String strToken = Preferences.getPreferenceValue(context, Preferences.PREF_TOKEN);

        if(strEmail == null || strEmail.equals("") || strPassword == null || strPassword.equals("") || strToken == null
                || strToken.equals("")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            //Verificar conexión
            boolean hasConnection = Conexion.hasConnection(context);

            //Si tiene internet se validan las credenciales guardadas sino se permite iniciar sesión
            if(hasConnection) {
                pd = ProgressDialog.show(context, "", "Iniciando Sesión..", true);
                pd.setCancelable(false);

                new LoginClient(MainActivity.this, strEmail, strPassword).execute();
            }else{
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.ARG_EMAIL, strEmail);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void showMessage(String message) {
        pd.dismiss();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        finish();
    }

    @Override
    public void onLoginFinish(String email, String password) {
        //Iniciar Profile
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.ARG_EMAIL, email);
        startActivity(intent);

        pd.dismiss();
        finish();
    }
}
