package com.example.vanessa_pc.pruebaigandroid.prospectos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vanessa_pc.pruebaigandroid.R;
import com.example.vanessa_pc.pruebaigandroid.controller.ProspectoController;
import com.example.vanessa_pc.pruebaigandroid.entidades.Prospecto;

import java.util.ArrayList;

public class ProspectosFragment extends Fragment {
    private ListView lvProspectos;
    private ProspectoItemAdapter itemAdapter;

    public ProspectosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_prospectos, container, false);
        lvProspectos = rootView.findViewById(R.id.lvProspectos);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListaProspectos();
    }



    public Prospecto getProspecto(String documento){
        for (Prospecto prospecto : ((ProspectoItemAdapter)lvProspectos.getAdapter()).getListProspectos()){
            if(prospecto.getCedula().equals(documento)) return prospecto;
        }

        return null;
    }

    public void updateListaProspectos(){
        final ArrayList<Prospecto> listProspectos = new ProspectoController(getContext()).getListProspectos();

        itemAdapter = new ProspectoItemAdapter(getActivity(), listProspectos, ProspectosFragment.this);
        lvProspectos.setAdapter(itemAdapter);
    }
}
