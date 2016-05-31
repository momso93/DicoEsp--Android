package com.rygelouv.dicoesp.interfaces.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rygelouv.dicoesp.R;
import com.rygelouv.dicoesp.event.RecyclerClickListener;
import com.rygelouv.dicoesp.model.Word;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

/**
 * Created by rygelouv on 30/05/16.
 */
public class EspDictAdapter extends RecyclerView.Adapter<EspDictAdapter.ViewHolder>
{
    private RecyclerClickListener recyclerClickListener;
    private RealmResults<Word> mDataset; // Represente la liste des mot qui vont etre affiché
    private Context mContext;
    private boolean isManageMode = false;

    public EspDictAdapter(RecyclerClickListener recyclerClickListener,
                          RealmResults<Word> mDataset,
                          Context mContext,
                          boolean isManageMode)
    {
        this.recyclerClickListener = recyclerClickListener;
        this.mDataset = mDataset;
        this.mContext = mContext;
        this.isManageMode = isManageMode;

    }

    @Override
    public EspDictAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.item_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EspDictAdapter.ViewHolder holder, final int position)
    {
        Word word = mDataset.get(position);

        holder.englishWord.setText(word.getEnglishWord());
        holder.frenchWord.setText(word.getFrenchWord());
        holder.wolofWord.setText(word.getWolofWord());
        if (!isManageMode)
        {
            holder.managemode.setVisibility(View.GONE);
        }
        else
            holder.managemode.setVisibility(View.VISIBLE);


        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                recyclerClickListener.onElementClicked(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        // Opérateur ternaire pour renvoyer la taille de liste
        return mDataset == null ? 0 : mDataset.size();
    }

    public void setData(RealmResults<Word> data)
    {
        mDataset = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        @InjectView(R.id.word_name_english)
        TextView englishWord;
        @InjectView(R.id.word_name_french)
        TextView frenchWord;
        @InjectView(R.id.word_name_wolof)
        TextView wolofWord;
        @InjectView(R.id.manage_mode)
        RelativeLayout managemode;
        @InjectView(R.id.edit_btn)
        ImageButton editBtn;
        @InjectView(R.id.delete_btn)
        ImageButton deleteBtn;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
