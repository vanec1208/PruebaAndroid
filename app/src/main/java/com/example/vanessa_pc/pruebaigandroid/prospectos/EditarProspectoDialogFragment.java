package com.example.vanessa_pc.pruebaigandroid.prospectos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.vanessa_pc.pruebaigandroid.R;
import com.example.vanessa_pc.pruebaigandroid.controller.ProspectoController;
import com.example.vanessa_pc.pruebaigandroid.entidades.Estado;
import com.example.vanessa_pc.pruebaigandroid.util.Validaciones;
import com.example.vanessa_pc.pruebaigandroid.entidades.Prospecto;

import java.util.ArrayList;

public class EditarProspectoDialogFragment extends DialogFragment {
    public static final String ARG_DOCUMENTO = "ARG_DOCUMENTO";

    private Context context;

    private EditText txtDocumento;
    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtTelefono;
    private Spinner spnEstado;

    private Button btnGuardar;
    private Button btnCancelar;

    private String documento;
    private Prospecto prospecto;

    OnProspectosListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_editar_prospecto, null);

        Bundle args = getArguments();
        if(args != null){
            documento = args.getString(ARG_DOCUMENTO, "");
            prospecto = listener.getProspecto(documento);
        }

        txtDocumento = view.findViewById(R.id.txtDocumento);
        txtNombre = view.findViewById(R.id.txtNombre);
        txtApellido = view.findViewById(R.id.txtApellido);
        txtTelefono = view.findViewById(R.id.txtTelefono);
        spnEstado = view.findViewById(R.id.spnEstado);
        btnGuardar = view.findViewById(R.id.btnGuardar);
        btnCancelar = view.findViewById(R.id.btnCancelar);

        txtDocumento.setBackgroundResource(R.drawable.style_edit_text);
        txtNombre.setBackgroundResource(R.drawable.style_edit_text);
        txtApellido.setBackgroundResource(R.drawable.style_edit_text);
        txtTelefono.setBackgroundResource(R.drawable.style_edit_text);

        final ArrayList<Estado> lista = new Validaciones().getListEstados();
        lista.add(0, new Estado(-1, "Seleccione"));

        ArrayAdapter<Estado> adapterEstados = new ArrayAdapter<>(context,
                R.layout.support_simple_spinner_dropdown_item, lista);
        spnEstado.setAdapter(adapterEstados);

        txtDocumento.setText(documento);
        txtNombre.setText(prospecto.getNombre());
        txtApellido.setText(prospecto.getApellido());
        txtTelefono.setText(prospecto.getTelefono());

        for (int i = 0; i < spnEstado.getAdapter().getCount(); i++) {
            int idEstado = ((Estado) spnEstado.getAdapter().getItem(i)).getId();
            if (prospecto.getEstado() == idEstado)
                spnEstado.setSelection(i);
        }

        final ProspectoController prospectoController = new ProspectoController(context);

        builder.setView(view);
        final AlertDialog dialog = builder.create();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nombre = txtNombre.getText().toString().trim();
                final String apellido = txtApellido.getText().toString().trim();
                final String telefono = txtTelefono.getText().toString().trim();
                final int estado = ((Estado) spnEstado.getSelectedItem()).getId();

                if(nombre.equals("") || apellido.equals("") || telefono.equals("") || estado == -1){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("TODOS LOS CAMPOS SON OBLIGATORIOS");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return;
                }

                if(!prospecto.getNombre().equals(nombre) || !prospecto.getApellido().equals(apellido) ||
                        !prospecto.getTelefono().equals(telefono) || prospecto.getEstado() != estado){
                    //Guardar cambios en DB
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("¿ESTÁ SEGURO(A) DE GUARDAR LOS CAMBIOS?");
                    alertDialogBuilder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int id) {
                            //Update List Prospecto
                            prospectoController.updateProspecto(txtDocumento.getText().toString(), nombre, apellido,
                                    telefono, estado);
                            listener.updateListaProspectos();

                            //Log para Prospecto
                            Prospecto nProspecto = new Prospecto();
                            nProspecto.setCedula(prospecto.getCedula());
                            nProspecto.setNombre(nombre);
                            nProspecto.setApellido(apellido);
                            nProspecto.setTelefono(telefono);
                            nProspecto.setEstado(estado);
                            new Validaciones().saveJsonLog(context, prospecto, nProspecto);

                            dialog.cancel();
                        }
                    }).setNegativeButton("NO", null);

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("NO SE HAN DETECTADO CAMBIOS");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtNombre.getText().toString().trim();
                String apellido = txtApellido.getText().toString().trim();
                String telefono = txtTelefono.getText().toString().trim();
                int estado = ((Estado) spnEstado.getSelectedItem()).getId();

                //Cambios detectados
                if(!prospecto.getNombre().equals(nombre) || !prospecto.getApellido().equals(apellido) ||
                        !prospecto.getTelefono().equals(telefono) || prospecto.getEstado() != estado){
                    //Descartar cambios
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("¿DESCARTAR CAMBIOS?");
                    alertDialogBuilder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int id) {
                            dialog.cancel();
                        }
                    }).setNegativeButton("NO", null);

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }else{
                    dialog.cancel();
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnProspectosListener) activity;
            context = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnProspectosListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnProspectosListener {
        Prospecto getProspecto(String documento);
        void updateListaProspectos();
    }
}
