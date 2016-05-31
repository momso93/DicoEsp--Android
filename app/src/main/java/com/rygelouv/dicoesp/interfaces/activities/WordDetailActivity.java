package com.rygelouv.dicoesp.interfaces.activities;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.rygelouv.dicoesp.R;
import com.rygelouv.dicoesp.constants.Constants;
import com.rygelouv.dicoesp.model.Word;
import com.rygelouv.dicoesp.service.EspDicoDatabaseService;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WordDetailActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener
{
    @InjectView(R.id.word_name_french)
    TextView wordFrench;
    @InjectView(R.id.word_french_def)
    TextView wordFrenchDef;
    @InjectView(R.id.word_name_english)
    TextView wordEnglish;
    @InjectView(R.id.word_english_def)
    TextView wordEnglishDef;
    @InjectView(R.id.word_name_wolof)
    TextView wordWolof;
    @InjectView(R.id.word_wolof_def)
    TextView wordWolofDef;

    private Word word;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        ButterKnife.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupWord();
        tts = new TextToSpeech(this, this);
    }

    public void setupWord()
    {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            if (bundle.getString(Constants.WORD_ID_KEY) != null)
            {
                word = EspDicoDatabaseService.getInstance(this)
                        .getWord(bundle.getString(Constants.WORD_ID_KEY));
                if (word != null)
                {
                    displayText();
                }
            }

            if (bundle.getString(Constants.WORD_SELECTED_KEY) != null)
                setTitle(bundle.getString(Constants.WORD_SELECTED_KEY));
        }
    }

    public void displayText()
    {
        String french = word.getFrenchWord() + " [Français]";
        String english = word.getEnglishWord() + " [Anglais]";
        String wolof = word.getWolofWord() + " [Wolof]";
        wordFrench.setText(french);
        wordFrenchDef.setText(word.getFrenchDef());
        wordEnglish.setText(english);
        wordEnglishDef.setText(word.getEnglishDef());
        wordWolof.setText(wolof);
        wordWolofDef.setText(word.getWolofDef());
    }


    @OnClick(R.id.play_french)
    public void onPlayFrench()
    {
        speakOut(Constants.FRENCH, word.getFrenchWord());
    }

    @OnClick(R.id.play_english)
    public void onPlayEnglish()
    {
        speakOut(Constants.ENGLISH, word.getEnglishWord());
    }

    @OnClick(R.id.play_wolof)
    public void onPlayWolof()
    {
        speakOut(Constants.FRENCH, "Je ne suis pas capable de lire du wolof." +
                " S'il vous plait, veuillez enregistrer vous même le son. Merci !");
    }

    @Override
    public void onDestroy()
    {
        // Don't forget to shutdown tts!
        if (tts != null)
        {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status)
    {
        if (status == TextToSpeech.SUCCESS)
        {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS", "This Language is not supported");
            } else
            {
                //btnSpeak.setEnabled(true);
                //speakOut();
            }

        } else
        {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String language, String textToSpeak)
    {
        switch (language)
        {
            case Constants.ENGLISH:
                tts.setLanguage(Locale.ENGLISH);
                break;
            case Constants.FRENCH:
                tts.setLanguage(Locale.FRENCH);
        }

        tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }
}
