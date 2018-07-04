package com.example.kpdn.sinhalapalidictionay;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class bookmarkAdapter extends BaseAdapter{

    private ListitemListener listener;
    private ListitemListener listenerBtnDelet;



    Context mContext;
    ArrayList<String> msource;

    public bookmarkAdapter(Context context ,ArrayList<String> source){

        this.mContext=context;
        this.msource=source;

    }


    @Override
    public int getCount() {
        return msource.size();
    }

    @Override
    public Object getItem(int i) {
        return msource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }



    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        viewHolder viewHolder;
        if(view==null){
            viewHolder=new viewHolder();
            view= LayoutInflater.from(mContext).inflate(R.layout.bookmark_layout_item,viewGroup,false);
            viewHolder.textView=view.findViewById(R.id.search_src_text);
            viewHolder.btnDelet= view.findViewById(R.id.image);


            view.setTag(viewHolder);
        }
        else{

            viewHolder=(viewHolder)view.getTag();

        }
        viewHolder.textView.setText(msource.get(i));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null) {
                    listener.onItemClick(i);
                                    }
            }
        });

    viewHolder.btnDelet.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(listenerBtnDelet!=null){

            listenerBtnDelet.onItemClick(i);


            }
        }
    });

        return view;
    }


    public void removeItem(int position){

        msource.remove(position);


    }



    class viewHolder{

        TextView textView;
        ImageView btnDelet;
    }

    public void setonItemClick(ListitemListener listitemListener){

        this.listener=listitemListener;
    }
    public void setonItemDeletClick(ListitemListener listitemListener){

        this.listenerBtnDelet=listitemListener;
    }

}
