package com.rygelouv.dicoesp.interfaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rygelouv.dicoesp.R;
import com.rygelouv.dicoesp.event.RecyclerClickListener;
import com.rygelouv.dicoesp.interfaces.adapter.EspDictAdapter;
import com.rygelouv.dicoesp.model.Word;
import com.rygelouv.dicoesp.service.EspDicoDatabaseService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerClickListener
{

    @InjectView(R.id.dictionnary) // Injection de la vue qui affiche le dictionnaire grace à la librairy ButterKnife
    RecyclerView mRecyclerView;

    private RealmResults<Word> mDataset;
    private RealmChangeListener mWordsChangeListener;
    private EspDictAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.inject(this); // Ici on initialise l'injecteur de vue pour qu'il fasse implicitement  les findViewById

        setupRecycleview();
        setupReamlListeners();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Cette methode va simplement configurer les ecouteur de base de donnée
     * Elle va essentiellement permettre d'ecouter les changement
     * sur la table des mots ({@link Word})
     */
    private void setupReamlListeners()
    {
        mWordsChangeListener = new RealmChangeListener()
        {
            @Override
            public void onChange()
            {
                mAdapter.notifyDataSetChanged();
            }
        };
        mDataset.addChangeListener(mWordsChangeListener);
    }


    /**
     * Cette methode va permettre de configurer le recyclerview
     * Dans cette methode on cree un {@link android.widget.Adapter},
     * un {@link android.support.v7.widget.RecyclerView.LayoutManager}
     * et on les affecte au recyclerview
     */
    private void setupRecycleview()
    {
        mDataset = null;
        mAdapter = new EspDictAdapter(this, mDataset, this, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        loadData();
    }

    /**
     * Cette methode permet de charger la liste des mots depuis la base de données
     */
    private void loadData()
    {
        mDataset = EspDicoDatabaseService.getInstance(this)
                .wordsList();
        mAdapter.setData(mDataset);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_dictionnary)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_manage_dict)
        {
            startActivity(new Intent(this, EditDictActivity.class));
        }
        else if (id == R.id.nav_slideshow)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onElementClicked(int position)
    {

    }
}
