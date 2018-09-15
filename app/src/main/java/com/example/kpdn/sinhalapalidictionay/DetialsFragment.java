package com.example.kpdn.sinhalapalidictionay;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DetialsFragment extends Fragment {

private String value ="";
private TextView tvWord;
private ImageButton btnBokkmark,btnVolume;


private ListView wordDetails;
private DetailsAdapter detailsAdapter;
private List<WordDetails> mWordDetails;

private DatabaseConnector databaseConnector;
private int mDicType;


    public DetialsFragment() {
        // Required empty public constructor
    }

    public static DetialsFragment  getNewInstance(String value, DatabaseConnector mdbHelper, int dicType, FloatingActionButton fab, android.support.v7.widget.Toolbar toolbar, MenuItem menuSetting){

        DetialsFragment fragment=new DetialsFragment();
        fragment.value=value;
        fragment.databaseConnector =mdbHelper;
        fragment.mDicType=dicType;
        fab.findViewById(R.id.search_voice_btn).setVisibility(View.GONE);
        toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
        toolbar.setTitle("Meaning");
        menuSetting.setVisible(false);

        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detials, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvWord=(TextView)view.findViewById(R.id.text2);
        btnBokkmark=(ImageButton)view.findViewById(R.id.checkbox);
        btnVolume=(ImageButton)view.findViewById(R.id.spacer);


        tvWord.setText(value);

        wordDetails=(ListView)view.findViewById(R.id.details);
        detailsAdapter=new DetailsAdapter(getContext(),databaseConnector.getMeanings(value,mDicType));
        wordDetails.setAdapter(detailsAdapter);

      //  final WordList wordList = databaseConnector.getWordSearchSinhala(value,mDicType);

        WordList bookmarkWordList = databaseConnector.getWordFromBookmark(value);
        int isMark= bookmarkWordList ==null?0:1;

        btnBokkmark.setTag(isMark);

        int icon= bookmarkWordList ==null? R.drawable.ic_bookmark_border:R.drawable.ic_bookmark;
        btnBokkmark.setImageResource(icon);


        btnBokkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int  value=(int)btnBokkmark.getTag();

              if(value==0){

                btnBokkmark.setImageResource(R.drawable.ic_bookmark);
                databaseConnector.addBookmark(tvWord.getText().toString(),mDicType+"");
                  Toast.makeText(getContext(),"පිටු සලකුණක් යෙදුවා", Toast.LENGTH_SHORT).show();
                btnBokkmark.setTag(1);

              }
                else if(value==1){

                  btnBokkmark.setImageResource(R.drawable.ic_bookmark_border);
                  btnBokkmark.setTag(0);
                  databaseConnector.removeBookmark(tvWord.getText().toString(),mDicType+"");
                  Toast.makeText(getContext(),"පිටු සලකුණ ඉවත් කරන ලදි", Toast.LENGTH_SHORT).show();

              }

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
