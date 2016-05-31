package com.rygelouv.dicoesp.interfaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rygelouv.dicoesp.R;
import com.rygelouv.dicoesp.constants.Constants;
import com.rygelouv.dicoesp.event.RecyclerClickListener;
import com.rygelouv.dicoesp.interfaces.adapter.EspDictAdapter;
import com.rygelouv.dicoesp.interfaces.fragments.AddWordFragment;
import com.rygelouv.dicoesp.model.Word;
import com.rygelouv.dicoesp.service.EspDicoDatabaseService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class EditDictActivity extends AppCompatActivity
        implements RecyclerClickListener
{

    @InjectView(R.id.edit_dictionnary)
    RecyclerView mRecyclerView;

    private EspDictAdapter mAdapter;
    private RealmResults<Word> mDataset;
    private RealmChangeListener mWordsChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dict);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Gérer le dictionnaire");
        ButterKnife.inject(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(EditDictActivity.this,
                        AddWordActivity.class));
            }
        });

        setupRecyclerView();
        setupReamlListeners();
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

    private void setupRecyclerView()
    {
        mAdapter = new EspDictAdapter(this, mDataset, this, true);
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
    public void onElementClicked(int position)
    {
        Intent intent = new Intent(this, WordDetailActivity.class);
        intent.putExtra(Constants.WORD_ID_KEY, mDataset.get(position).getId());
        intent.putExtra(Constants.WORD_SELECTED_KEY, mDataset.get(position).getFrenchWord()
                +" [Français]");
        startActivity(intent);
    }

    @Override
    public void onOptionsClicked(int position, String action)
    {
        switch (action)
        {
            case Constants.ACTION_DELETE_WORD:
                deleteWord(position);
                break;
            case Constants.ACTION_EDIT_WORD:
                Intent intent = new Intent(EditDictActivity.this, AddWordActivity.class);
                intent.putExtra(Constants.EDIT_INIC_KEY, true);
                intent.putExtra(Constants.WORD_ID_KEY, mDataset.get(position).getId());
                startActivity(intent);
                break;
        }
    }

    public void deleteWord(final int position)
    {
        new MaterialStyledDialog(this)
                .setTitle("SUPPRESSION")
                .setDescription("Voulez vous vraiment supprimer ce mot du dictionnaire ?")
                .setIcon(getResources().getDrawable(R.drawable.check_circle_outline))
                .withDialogAnimation(true)
                .setHeaderColor(android.R.color.holo_red_light)
                .setPositive("OUI", new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        EspDicoDatabaseService.getInstance(EditDictActivity.this)
                                .deleteWord(mDataset.get(position).getId());
                        dialog.dismiss();
                    }
                })
                .setNegative("ANNULER", new MaterialDialog.SingleButtonCallback()
                {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                    {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
