package com.example.kpdn.sinhalapalidictionay;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class BookmarkFragment extends Fragment {

private DatabaseConnector mdbHelper;
private int mDicType;


private FragmentListener listener;

    public BookmarkFragment() {

    }


    public static BookmarkFragment getnewinstance(DatabaseConnector databaseConnector, int dicType){
        BookmarkFragment book=new BookmarkFragment();
        book.mdbHelper= databaseConnector;
        book.mDicType=dicType;
        return book;

    }
    public void setmDicType(int dicType){

        mDicType=dicType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

          final ListView BookmarkList=(ListView)view.findViewById(R.id.select_dialog_listview);
          final bookmarkAdapter adapter=new bookmarkAdapter(getActivity(),mdbHelper.getAllWordFromBookmark(mDicType+""));
          BookmarkList.setAdapter(adapter);


          adapter.setonItemClick(new ListitemListener() {
              @Override
              public void onItemClick(int position) {

                  if(listener!=null){


                      listener.onItemClick(String.valueOf(adapter.getItem(position)));
                  }
              }
          });

          adapter.setonItemDeletClick(new ListitemListener() {
              TextView value;
              View viewFroEachText;
              @Override
              public void onItemClick(int position) {

                  adapter.removeItem(position);
                  viewFroEachText=BookmarkList.getChildAt(position);
                  adapter.notifyDataSetChanged();
                  value=(TextView)viewFroEachText.findViewById(R.id.search_src_text);

                  mdbHelper.removeBookmark(value.getText().toString(),mDicType+"");

                  Toast.makeText(getContext(),"මකා දැමුවා", Toast.LENGTH_SHORT).show();
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

    public void setOnFragmentListener(FragmentListener listener){

        this.listener=listener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_c,menu);


    }

}
