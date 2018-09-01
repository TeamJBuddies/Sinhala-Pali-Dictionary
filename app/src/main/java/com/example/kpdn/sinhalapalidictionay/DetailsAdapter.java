package com.example.kpdn.sinhalapalidictionay;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DetailsAdapter extends BaseAdapter {

    private Context mContext;
    private List<WordDetails> mWordDetailsList;

    public DetailsAdapter(Context mContext, List<WordDetails> mWordDetailsList) {
        this.mContext = mContext;
        this.mWordDetailsList = mWordDetailsList;
    }

    @Override
    public int getCount() {
        return mWordDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWordDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View V=View.inflate(mContext,R.layout.details_layout_item,null);
        TextView meanings=(TextView)V.findViewById(R.id.meaning);
        TextView tenss=(TextView)V.findViewById(R.id.tens);
        TextView englishWords=(TextView)V.findViewById(R.id.englishWord);

        meanings.setText(mWordDetailsList.get(position).getMeaning());
        tenss.setText(mWordDetailsList.get(position).getTens());
        englishWords.setText(mWordDetailsList.get(position).getEnglishWord());

        V.setTag(mWordDetailsList.get(position).getId());
        return V;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }
}
