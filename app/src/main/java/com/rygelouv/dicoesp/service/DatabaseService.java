package com.rygelouv.dicoesp.service;

import com.rygelouv.dicoesp.model.Word;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by rygelouv on 30/05/16.
 *
 * Cette interface fourni les methodes d'acces à la base de données
 * Elle se base sur un objet Generique ce qui veut dire qu'on peurra
 * la reutiliser au cas on aurait d'autres entité de base de données
 */
public interface DatabaseService<T extends RealmObject>
{
    void addNewWord(T word);

    void editWord(String id);

    void getWord(String id);

    void deleteWord(String id);

    RealmResults<T> wordsList();
}
