package com.rygelouv.dicoesp.interfaces.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rygelouv.dicoesp.R;
import com.rygelouv.dicoesp.constants.Constants;
import com.rygelouv.dicoesp.model.Word;
import com.rygelouv.dicoesp.service.EspDicoDatabaseService;

import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddWordActivity extends AppCompatActivity
{
    @InjectView(R.id.wolof_word)
    EditText wolofWord;
    @InjectView(R.id.wolof_word_desc)
    EditText wolofWordDef;
    @InjectView(R.id.english_word)
    EditText englishWord;
    @InjectView(R.id.english_word_desc)
    EditText englishWordDef;
    @InjectView(R.id.french_word)
    EditText frenchWord;
    @InjectView(R.id.french_word_desc)
    EditText frenchWordDef;
    @InjectView(R.id.validate_form)
    Button validateForm;

    private Word word;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Ajouter un mot au dictionnaire");
        ButterKnife.inject(this);

        setupOpeningOptions();

    }

    private void setupOpeningOptions()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            if (bundle.getBoolean(Constants.EDIT_INIC_KEY))
            {
                isEdit = true;
                if (bundle.getString(Constants.WORD_ID_KEY) != null)
                {
                    word = EspDicoDatabaseService.getInstance(this)
                            .getWord(bundle.getString(Constants.WORD_ID_KEY));

                    fillForm();
                }
            }
        }
    }

    public void fillForm()
    {
        wolofWord.setText(word.getWolofWord());
        wolofWordDef.setText(word.getWolofDef());
        englishWord.setText(word.getEnglishWord());
        englishWordDef.setText(word.getEnglishDef());
        frenchWord.setText(word.getFrenchWord());
        frenchWordDef.setText(word.getFrenchDef());
    }

    @OnClick(R.id.validate_form)
    public void onValidateFormClicked()
    {
        if (isFormValid())
        {
            Word word = buildWord();
            EspDicoDatabaseService.getInstance(this)
                    .addNewWord(word);

            new MaterialStyledDialog(this)
                    .setTitle("REUSSI !")
                    .setDescription("Le nouveau  mot "+ word.getFrenchWord()+
                            " a bien été ajouté ")
                    .setIcon(getResources().getDrawable(R.drawable.check_circle_outline))
                    .withDialogAnimation(true)
                    .setHeaderColor(android.R.color.holo_green_dark)
                    .setPositive("OK", new MaterialDialog.SingleButtonCallback()
                    {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                        {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .show();
        }
    }

    private Word buildWord()
    {
        Word word = new Word();
        word.setId(UUID.randomUUID().toString());
        word.setEnglishDef(englishWordDef.getText().toString());
        word.setEnglishWord(englishWord.getText().toString());
        word.setFrenchDef(frenchWordDef.getText().toString());
        word.setFrenchWord(frenchWord.getText().toString());
        word.setWolofDef(wolofWordDef.getText().toString());
        word.setWolofWord(wolofWord.getText().toString());

        return word;
    }

    private boolean isFormValid()
    {
        if (wolofWord.getText().toString().isEmpty())
        {
            wolofWord.setError("Vous devez remplire ce champ");
            return false;
        }
        if (wolofWordDef.getText().toString().isEmpty())
        {
            wolofWordDef.setError("Vous devez remplire ce champ");
            return false;
        }
        if (englishWord.getText().toString().isEmpty())
        {
            englishWord.setError("Vous devez remplire ce champ");
            return false;
        }
        if (englishWordDef.getText().toString().isEmpty())
        {
            englishWordDef.setError("Vous devez remplire ce champ");
            return false;
        }
        if (frenchWord.getText().toString().isEmpty())
        {
            frenchWord.setError("Vous devez remplire ce champ");
            return false;
        }
        if (frenchWordDef.getText().toString().isEmpty())
        {
            frenchWordDef.setError("Vous devez remplire ce champ");
            return false;
        }

        return true;
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
