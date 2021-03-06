package com.rygelouv.dicoesp.service;

import android.content.Context;
import android.util.Log;
import com.rygelouv.dicoesp.model.Word;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rygelouv on 30/05/16.
 *
 * Cette classe implémente l'interface {@link EspDicoDatabaseService}
 * et redefini les methodes de celle ci pour un acces à la base de données
 */
public class EspDicoDatabaseService implements DatabaseService<Word>
{
    private static final String TAG = "ESP_DIC_DATABASE";
    private static EspDicoDatabaseService instance;
    private Context mContext;

    public synchronized static EspDicoDatabaseService getInstance(Context context)
    {
        if (instance == null)
            instance = new EspDicoDatabaseService(context);

        return instance;
    }

    public EspDicoDatabaseService(Context context)
    {
        this.mContext = context;
    }

    @Override
    public void addNewWord(Word word)
    {
        Log.e(TAG, "Trying to insert word");

        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(word);
        realm.commitTransaction();

        Log.e(TAG, "Everything gone right");
    }

    @Override
    public void editWord(String id)
    {
        /*Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();*/
    }

    @Override
    public Word getWord(String id)
    {
        Realm realm = Realm.getInstance(mContext);
        Word word = realm.where(Word.class)
                .equalTo("id", id)
                .findFirst();

        return word;
    }

    @Override
    public Word getWordFromText(String text)
    {

        Realm realm = Realm.getInstance(mContext);
        Word word = realm.where(Word.class)
                .equalTo("frenchWord", text.trim())
                .findFirst();

        return word;
    }

    @Override
    public void deleteWord(String id)
    {
        Realm realm = Realm.getInstance(mContext);
        Word word = getWord(id);
        if (word != null)
        {
            realm.beginTransaction();
            word.removeFromRealm();
            realm.commitTransaction();
        }
    }

    @Override
    public RealmResults<Word> wordsList()
    {
        Realm realm = Realm.getInstance(mContext);
        RealmResults<Word> query = realm.where(Word.class).findAll();

        return query;
    }
}
