package com.example.vanessa_pc.pruebaigandroid.gui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.vanessa_pc.pruebaigandroid.controller.ProspectoController;
import com.example.vanessa_pc.pruebaigandroid.entidades.Prospecto;
import com.example.vanessa_pc.pruebaigandroid.prospectos.EditarProspectoDialogFragment;
import com.example.vanessa_pc.pruebaigandroid.util.Preferences;
import com.example.vanessa_pc.pruebaigandroid.R;
import com.example.vanessa_pc.pruebaigandroid.prospectos.ProspectosFragment;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        EditarProspectoDialogFragment.OnProspectosListener{

    public static final String ARG_EMAIL = "email";

    private Context context;
    private String email;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        email = getIntent().getStringExtra(ARG_EMAIL);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_profile);
        ((TextView) headerView.findViewById(R.id.txtEmail)).setText(email);

        navigationView.setNavigationItemSelectedListener(this);
        drawer.openDrawer(Gravity.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private MenuItem previousMenuItem;
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setCheckable(true);
        item.setChecked(true);
        if (previousMenuItem != null) {
            previousMenuItem.setChecked(false);
        }

        previousMenuItem = item;
        String title = getString(R.string.app_name);

        int id = item.getItemId();

        if (id == R.id.nav_prospectos) {
            title = getString(R.string.title_prospectos);
            Fragment fragment = new ProspectosFragment();
            getSupportActionBar().setTitle(title);

            if (fragment != null){
                fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment, "prospectosFragment")
                        .commit();
            }
        } else if (id == R.id.nav_logout) {
            final ProgressDialog pd = ProgressDialog.show(context,
                    "", "Cerrando Sesión..", true);

            //Eliminar Preferences
            Preferences.setPreferenceValue(context, Preferences.PREF_EMAIL, "");
            Preferences.setPreferenceValue(context, Preferences.PREF_PASSWORD, "");
            Preferences.setPreferenceValue(context, Preferences.PREF_TOKEN, "");

            //Eliminar Prospectos
            new ProspectoController(context).deleteProspectoAll();

            //Volver al Login
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);

            pd.dismiss();
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Prospecto getProspecto(String documento) {
        ProspectosFragment fragment = (ProspectosFragment)fragmentManager.findFragmentByTag("prospectosFragment");
        return fragment.getProspecto(documento);
    }

    @Override
    public void updateListaProspectos() {
        ProspectosFragment fragment = (ProspectosFragment)fragmentManager.findFragmentByTag("prospectosFragment");
        fragment.updateListaProspectos();
    }
}
