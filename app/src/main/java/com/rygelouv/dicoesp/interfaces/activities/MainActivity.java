package com.rygelouv.dicoesp.interfaces.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
import com.rygelouv.dicoesp.R;
import com.rygelouv.dicoesp.constants.Constants;
import com.rygelouv.dicoesp.event.RecyclerClickListener;
import com.rygelouv.dicoesp.interfaces.adapter.EspDictAdapter;
import com.rygelouv.dicoesp.model.Word;
import com.rygelouv.dicoesp.service.EspDicoDatabaseService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RecyclerClickListener
{

    private static final String TAG = "MAIN_ACTIVITY";
    @InjectView(R.id.dictionnary) // Injection de la vue qui affiche le dictionnaire grace à la librairy ButterKnife
    RecyclerView mRecyclerView;

    protected SearchView mSearchView = null;
    protected DrawerLayout mDrawerLayout = null;
    protected Toolbar mToolbar = null;

    private SearchHistoryTable mHistoryDatabase;
    private RealmResults<Word> query;


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

        setDrawer();
        setNavigationView();

        setSearchView();
        mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener()
        {
            @Override
            public void onMenuClick()
            {
                mDrawerLayout.openDrawer(GravityCompat.START); // finish();
                perm(Manifest.permission.RECORD_AUDIO, 0);
            }
        });

        customSearchView();
    }

    private void perm(String permission, int permission_request)
    {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
        {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
            {
                ActivityCompat.requestPermissions(this, new String[]{permission}, permission_request);
            }
        }
    }

    private void setDrawer()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener()
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                if (mSearchView != null && mSearchView.isSearchOpen())
                {
                    mSearchView.close(true);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        });
    }

    private void setNavigationView()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
        {
            navigationView.setNavigationItemSelectedListener(this);
            if (getNavItem() > -1)
            {
                navigationView.getMenu().getItem(getNavItem()).setChecked(true);
            }
        }
    }

    protected int getNavItem()
    {
        return Constants.NAV_ITEM_TOOLBAR;
    }

    private void getToolbar()
    {
        if (mToolbar == null)
        {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            if (mToolbar != null)
            {
                mToolbar.setNavigationContentDescription(getResources().getString(R.string.app_name));
                setSupportActionBar(mToolbar);
            }
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_dictionnary)
        {
            // Handle the camera action
        } else if (id == R.id.nav_manage_dict)
        {
            startActivity(new Intent(this, EditDictActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    }

    protected void setSearchView()
    {
        mHistoryDatabase = new SearchHistoryTable(this);

        mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null)
        {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setHint("Recherchez un mot");
            mSearchView.setTextSize(16);
            mSearchView.setDivider(false);
            mSearchView.setVoice(true);
            mSearchView.setVoiceText("Dites le mot que cherchez !");
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
            mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    mSearchView.close(false);
                    getData(query, 0);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText)
                {
                    return false;
                }
            });

            List<SearchItem> suggestionsList;
            suggestionsList = getWords();

            SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
            searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    mSearchView.close(false);
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    String query = textView.getText().toString();
                    getData(query, position);
                }
            });

            mSearchView.setAdapter(searchAdapter);
        }
    }

    private List<SearchItem> getWords()
    {
        query = EspDicoDatabaseService.getInstance(this)
                .wordsList();
        List<SearchItem> suggestionsList = new ArrayList<>();
        for (Word word : query)
        {
            suggestionsList.add(new SearchItem(word.getWolofWord()));
            suggestionsList.add(new SearchItem(word.getEnglishWord()));
            suggestionsList.add(new SearchItem(word.getFrenchWord()));
        }

        return suggestionsList;
    }

    protected void customSearchView()
    {
        final Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            mSearchView.setVersion(extras.getInt("version"));
            mSearchView.setVersionMargins(extras.getInt("version_margins"));
            mSearchView.setTheme(extras.getInt("theme"), true);
            mSearchView.setText(extras.getString("text"));
        }
    }

    private void getData(String text, int position)
    {
        mHistoryDatabase.addItem(new SearchItem(text));

        Word word = null;
        Log.e(TAG, "size : "+query.size());
        for (Word wordItem : query)
        {
            Log.e(TAG, wordItem.toString());

            Log.e(TAG, wordItem.getEnglishWord()+ " "+text);
            Log.e(TAG, wordItem.getWolofWord()+ " "+text);
            Log.e(TAG, wordItem.getFrenchWord()+ " "+text);

            if (wordItem.getEnglishWord().toLowerCase().equals(text))
            {
                Log.e(TAG, "there is matching");
                word = wordItem;
                text = wordItem.getEnglishWord();
                break;
            }
            else if (wordItem.getFrenchWord().toLowerCase().equals(text))
            {
                Log.e(TAG, "there is matching");
                word = wordItem;
                text = wordItem.getFrenchWord();
                break;
            }
            else if (wordItem.getWolofWord().toLowerCase().equals(text))
            {
                Log.e(TAG, "there is matching");
                word = wordItem;
                text = wordItem.getWolofWord();
                break;
            }
        }

        if (word != null)
        {
            Intent intent = new Intent(getApplicationContext(), WordDetailActivity.class);
            intent.putExtra(Constants.WORD_ID_KEY, word.getId());
            intent.putExtra(Constants.WORD_SELECTED_KEY, text);
            startActivity(intent);
        }
        else
            Log.e(TAG, "no word found");

        Toast.makeText(getApplicationContext(), text +
                ", position: " + position, Toast.LENGTH_SHORT).show();
    }
}
