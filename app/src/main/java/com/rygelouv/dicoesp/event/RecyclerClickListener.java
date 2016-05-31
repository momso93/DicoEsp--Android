package com.rygelouv.dicoesp.event;

/**
 * Created by rygelouv on 30/05/16.
 *
 * Cette interface est utilisé pour gerer les clicks
 * sur les elements d'une liste quelconque grace à la methode onElementClicked
 * qui prend en paramettre la position de l'element cliqué
 */
public interface RecyclerClickListener
{
    void onElementClicked(int position);

    void onOptionsClicked(int position, String action);
}
