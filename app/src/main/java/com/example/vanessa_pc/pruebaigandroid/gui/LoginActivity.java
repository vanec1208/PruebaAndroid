package com.example.vanessa_pc.pruebaigandroid.gui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vanessa_pc.pruebaigandroid.comm.ProspectosClient;
import com.example.vanessa_pc.pruebaigandroid.util.Preferences;
import com.example.vanessa_pc.pruebaigandroid.R;
import com.example.vanessa_pc.pruebaigandroid.util.Validaciones;
import com.example.vanessa_pc.pruebaigandroid.comm.LoginClient;

public class LoginActivity extends AppCompatActivity implements LoginClient.OnTaskListener, ProspectosClient.OnTaskListener {

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnLogin;

    private Context context;
    ProgressDialog pd;
    Validaciones validaciones = new Validaciones();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        txtEmail.setBackgroundResource(R.drawable.style_edit_text);
        txtPassword.setBackgroundResource(R.drawable.style_edit_text);

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                txtEmail.setTextColor(validaciones.isCorreoValido(editable.toString()) ? Color.BLACK :
                        Color.rgb(223,91,95));
                activarLoginButton();
            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                activarLoginButton();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strEmail = txtEmail.getText().toString();
                final String strPassword = txtPassword.getText().toString();

                pd = ProgressDialog.show(context,"", "Iniciando Sesión..", true);
                pd.setCancelable(false);

                new LoginClient(LoginActivity.this, strEmail, strPassword).execute();
            }
        });

    }

    public void activarLoginButton(){
        String strEmail = txtEmail.getText().toString();
        String strPassword = txtPassword.getText().toString();
        btnLogin.setEnabled(!strEmail.equals("") && validaciones.isCorreoValido(strEmail) && !strPassword.equals(""));
    }


    @Override
    public void showMessage(String message) {
        pd.dismiss();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onLoginFinish(String email, String password) {
        //Guardar Preferences
        Preferences.setPreferenceValue(context, Preferences.PREF_EMAIL, email);
        Preferences.setPreferenceValue(context, Preferences.PREF_PASSWORD, password);

        pd.setMessage("Cargando prospectos...");
        String token = Preferences.getPreferenceValue(context, Preferences.PREF_TOKEN);

        //Descargar Prospectos
        //boolean existeProspectosTable = new ProspectoController(context).hasProspectoTable();
        //Log.d("HOLA", String.valueOf(existeProspectosTable));
        new ProspectosClient(LoginActivity.this, token).execute();
    }

    @Override
    public void showMessageP(String message) {
        pd.dismiss();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onLoginFinishP() {
        //Iniciar Profile
        String email = Preferences.getPreferenceValue(context, Preferences.PREF_EMAIL);

        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.ARG_EMAIL, email);
        startActivity(intent);

        pd.dismiss();
        finish();
    }
}
