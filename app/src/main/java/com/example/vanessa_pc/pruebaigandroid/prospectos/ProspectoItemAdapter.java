package com.example.vanessa_pc.pruebaigandroid.prospectos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vanessa_pc.pruebaigandroid.util.EstadoProspecto;
import com.example.vanessa_pc.pruebaigandroid.R;
import com.example.vanessa_pc.pruebaigandroid.entidades.Prospecto;

import java.util.ArrayList;

public class ProspectoItemAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<Prospecto> listProspectos;
    private ProspectosFragment prospectosFragment;

    private LayoutInflater inflater;

    public static class ViewHolder{
        ImageView imgProspectoStatus;
        TextView lblNombre;
        TextView lblDocumento;
        TextView lblTelefono;
        ImageButton imgProspectoEdit;
    }

    ProspectoItemAdapter(Context c, ArrayList<Prospecto> listProspectos, ProspectosFragment fragment){
        this.context = c;
        this.listProspectos = listProspectos;
        inflater = LayoutInflater.from(c);
        this.prospectosFragment = fragment;
    }

    @Override
    public View getView(int position, View view, final ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            view = inflater.inflate(R.layout.adapter_item_prospecto, null);

            holder = new ViewHolder();

            holder.imgProspectoStatus = view.findViewById(R.id.imgProspectoStatus);
            holder.lblNombre = view.findViewById(R.id.lblNombre);
            holder.lblDocumento = view.findViewById(R.id.lblDocumento);
            holder.lblTelefono = view.findViewById(R.id.lblTelefono);
            holder.imgProspectoEdit = view.findViewById(R.id.imgProspectoEdit);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        final Prospecto prospecto = (Prospecto) getItem(position);

        int estado = prospecto.getEstado();
        switch(estado) {
            case EstadoProspecto.PENDING:
                holder.imgProspectoStatus.setImageResource(R.drawable.icon_pending);
                break;
            case EstadoProspecto.APPROVED:
                holder.imgProspectoStatus.setImageResource(R.drawable.icon_approved);
                break;
            case EstadoProspecto.ACCEPTED:
                holder.imgProspectoStatus.setImageResource(R.drawable.icon_accepted);
                break;
            case EstadoProspecto.REJECTED:
                holder.imgProspectoStatus.setImageResource(R.drawable.icon_rejected);
                break;
            case EstadoProspecto.DISABLED:
                holder.imgProspectoStatus.setImageResource(R.drawable.icon_disabled);
                break;
        }

        holder.lblNombre.setText(prospecto.getNombre() + " " + prospecto.getApellido());
        holder.lblDocumento.setText("CC. " + prospecto.getCedula());
        holder.lblTelefono.setText("Télefono: " + prospecto.getTelefono());

        holder.imgProspectoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarProspectoDialogFragment editarProspectoFragment = new EditarProspectoDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(EditarProspectoDialogFragment.ARG_DOCUMENTO, prospecto.getCedula());
                editarProspectoFragment.setArguments(bundle);
                editarProspectoFragment.show(prospectosFragment.getFragmentManager(), "editarProspectoDialog");
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return listProspectos.size();
    }

    @Override
    public Object getItem(int i) {
        return listProspectos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public ArrayList<Prospecto> getListProspectos(){
        return listProspectos;
    }
}
