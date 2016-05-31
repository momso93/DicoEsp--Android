package com.rygelouv.dicoesp.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rygelouv on 30/05/16.
 *
 * Cette classe represente l'entité mot du ductionnaire
 */
public class Word extends RealmObject
{
    @PrimaryKey
    private String id;
    private String englishWord;
    private String wolofWord;
    private String frenchWord;
    private String englishDef;
    private String frenchDef;
    private String wolofDef;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getEnglishWord()
    {
        return englishWord;
    }

    public void setEnglishWord(String englishWord)
    {
        this.englishWord = englishWord;
    }

    public String getWolofWord()
    {
        return wolofWord;
    }

    public void setWolofWord(String wolofWord)
    {
        this.wolofWord = wolofWord;
    }

    public String getFrenchWord()
    {
        return frenchWord;
    }

    public void setFrenchWord(String frenchWord)
    {
        this.frenchWord = frenchWord;
    }

    public String getEnglishDef()
    {
        return englishDef;
    }

    public void setEnglishDef(String englisDef)
    {
        this.englishDef = englisDef;
    }

    public String getFrenchDef()
    {
        return frenchDef;
    }

    public void setFrenchDef(String frenchDef)
    {
        this.frenchDef = frenchDef;
    }

    public String getWolofDef()
    {
        return wolofDef;
    }

    public void setWolofDef(String wolofDef)
    {
        this.wolofDef = wolofDef;
    }

    @Override
    public String toString()
    {
        // Affichier les 3 significaiton du mot en de besoin de log par exemple
        return englishWord+" - "+frenchWord+" - "+wolofDef;
    }
}
