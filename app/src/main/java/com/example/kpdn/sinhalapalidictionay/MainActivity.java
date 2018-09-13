package com.example.kpdn.sinhalapalidictionay;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DatabaseConnector databaseConnector;

   private EditText SearchText;
   MenuItem menuSetting,CurrentMenuItemInNavigatinon;
   boolean MenueSelected=false;
   public  int idDicType;
   DictionaryFragment dictionaryFragment;
   BookmarkFragment bookmarkFragment;
   AboutFragment aboutFragment;
   helpFragment helpFragment;

   FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchText=(EditText)findViewById(R.id.edit_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseConnector =new DatabaseConnector(this);


        fab = (FloatingActionButton) findViewById(R.id.search_voice_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "සිංහල භාශාවෙන් කතා කරන්න", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                GoogleSpeech();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dictionaryFragment=new DictionaryFragment();
        helpFragment=new helpFragment();
        aboutFragment = new AboutFragment();


        final String id= SharedPreference.getState(MainActivity.this,"dicType");
        int dictype=id==null?R.id.sinhala_pali:Integer.valueOf(id);
        bookmarkFragment=BookmarkFragment.getnewinstance(databaseConnector,dictype);


        goTpFragment(dictionaryFragment,true);

        dictionaryFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {

                String id= SharedPreference.getState(MainActivity.this,"dicType");
                int dictype=id==null?R.id.sinhala_pali:Integer.valueOf(id);

                goTpFragment(DetialsFragment.getNewInstance(value, databaseConnector,dictype,fab,toolbar,menuSetting),false);

            }
        });

        bookmarkFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {

                String id= SharedPreference.getState(MainActivity.this,"dicType");
                int dictype=id==null?R.id.sinhala_pali:Integer.valueOf(id);

                goTpFragment(DetialsFragment.getNewInstance(value, databaseConnector,dictype,fab,toolbar,menuSetting),false);
            }
        });

        EditText editSearch=(EditText)findViewById(R.id.edit_search);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkLanguage();
                ArrayList<String> sourc= databaseConnector.getWord(charSequence.toString());
                dictionaryFragment.resetDataSource(sourc);
              //  dictionaryFragment.filterValue(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                goTpFragment(dictionaryFragment,false);
            }
        });

    }

    @Override
    public void onBackPressed() {

        menuSetting.setVisible(true);
        fab.findViewById(R.id.search_voice_btn).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.edit_search).setVisibility(View.VISIBLE);
        toolbar.setTitle("");
       if(MenueSelected==true) {
           CurrentMenuItemInNavigatinon.setEnabled(true);//set selected menu enable true
       }
       MenueSelected=false;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        menuSetting=menu.findItem(R.id.action_settings);

      String id= SharedPreference.getState(this,"dicType");

            if(id!=null){

                onOptionsItemSelected(menu.findItem(Integer.valueOf(id)));
                        }else {
                ArrayList<String> sourc= databaseConnector.getWord("");
                databaseConnector.SetDicType(R.id.sinhala_pali);
                dictionaryFragment.resetDataSource(sourc);
            }



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       int id = item.getItemId();

        if(R.id.action_settings==id) return true;

        SharedPreference.saveState(this,"dicType",String.valueOf(id));


        databaseConnector.SetDicType(id);


        if (id == R.id.sinhala_pali) {
            ArrayList<String> source= databaseConnector.getWord(SearchText.getText().toString());
            dictionaryFragment.resetDataSource(source);
            bookmarkFragment.setmDicType(id);

        }else
            if(id == R.id.pali_sinhala)
            { ArrayList<String> source= databaseConnector.getWord(SearchText.getText().toString());
                dictionaryFragment.resetDataSource(source);
                bookmarkFragment.setmDicType(id);

            }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if(MenueSelected==true) {
            CurrentMenuItemInNavigatinon.setEnabled(true);//set selected menu enable true
        }

        int id = item.getItemId();
         item.setEnabled(false);
         MenueSelected=true;
        CurrentMenuItemInNavigatinon=item;//catch current select menu
        if (id == R.id.nav_Bookmark) {

            goTpFragment(bookmarkFragment,false);

        } else if (id == R.id.nav_About) {
            goTpFragment(aboutFragment,false);
        } else if (id == R.id.nav_Help) {
            goTpFragment(helpFragment,false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void GoogleSpeech() {
        /*this function use to touch the voice icon then auto start Google Cloud*/

        SearchText.setFocusable(true);
        SearchText.isActivated();

        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"si-LK");


                if(intent.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(intent, 10);
                }
                else{

                    Toast.makeText(this,"Your Device Don't Support Speech Input",Toast.LENGTH_SHORT).show();

                }



    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case 10:
                if(resultCode==RESULT_OK && data!=null){

                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    SearchText.setText(result.get(0));

                }
                break;

        }
    }

    void goTpFragment(Fragment fragment,boolean isTop){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container,fragment);
        if(!isTop)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        String activeFragment=getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();

        if(activeFragment.equals(BookmarkFragment.class.getSimpleName())){

            menuSetting.setVisible(false);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
            toolbar.setTitle("Bookmark");
            fab.findViewById(R.id.search_voice_btn).setVisibility(View.GONE);

        }
       else if(activeFragment.equals(DetialsFragment.class.getSimpleName())){

            menuSetting.setVisible(false);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
            toolbar.setTitle("Meaning");
            fab.findViewById(R.id.search_voice_btn).setVisibility(View.GONE);

        }

        else {

            menuSetting.setVisible(true);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.VISIBLE);
            toolbar.setTitle("");
            fab.findViewById(R.id.search_voice_btn).setVisibility(View.VISIBLE);

        }
        return true;

    }


    public  void clearBookmark(){

        databaseConnector. CleenBookmark();
    }
    public void checkLanguage() {
        char firsCh = 'A';
        if (!SearchText.getText().toString().matches("")) {
            firsCh = SearchText.getText().toString().charAt(0);
            if (firsCh >= 'a' && firsCh <= 'z' || firsCh >= 'A' && firsCh <= 'Z') {
                databaseConnector.SetLanguageType('E');
            } else {

                databaseConnector.SetLanguageType('S');
            }

        }
    }

}

