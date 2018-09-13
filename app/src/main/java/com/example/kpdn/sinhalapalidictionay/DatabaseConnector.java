package com.example.kpdn.sinhalapalidictionay;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseConnector extends SQLiteOpenHelper{

    private Context mcontex;

    public static final String DATABASE_NAME="dhanushka.db";
    public static final int DATABASE_VERSION=1;

    private String DATABASE_LOCATION="";
    private String DATABASE_FULL_PATH="";

    private final String TBL_SI_PL="S";
    private final String TBL_PL_SI="P";
    private final String TBL_BOOKMARK="bookmark";

    private final String COL_KEY="key";
    private final String COL_VALUE="value";

    private char SearchLanguage='E';
    private int DICTYPE;

    public SQLiteDatabase mDB;

    public DatabaseConnector(Context context){

        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mcontex=context;

        DATABASE_LOCATION="/data/data/"+mcontex.getPackageName()+"/databases/";
        DATABASE_FULL_PATH=DATABASE_LOCATION+DATABASE_NAME;

        if(!isExistingDB()){

            try {
                File creatFolderLocation=new File(DATABASE_LOCATION);
                creatFolderLocation.mkdirs();
                extractAssetToDatabasesDirectory(DATABASE_NAME);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        mDB=SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH,null);


    }
    boolean isExistingDB(){

        File file=new File(DATABASE_FULL_PATH);

        return file.exists();
    }



    public void extractAssetToDatabasesDirectory(String fileName)throws IOException{


        int length;
        InputStream sourceDatabase=this.mcontex.getAssets().open(fileName);
        File destinationPath=new File(DATABASE_FULL_PATH);
        OutputStream destination=new FileOutputStream(destinationPath);

        byte[] buffer=new byte[4096];
        while ((length=sourceDatabase.read(buffer))>0){

            destination.write(buffer,0,length);

        }
        sourceDatabase.close();
        destination.flush();
        destination.close();


    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void SetLanguageType(char language){

        this.SearchLanguage=language;

    }
    public void SetDicType(int dicType){
        this.DICTYPE=dicType;

    }
    public char getLanguageType(){
        return SearchLanguage;
    }

    public ArrayList<String > getWord(String searchWord){

        ArrayList<String> source = new ArrayList<>();
       if(!searchWord.matches("")) {
           String tableName = getTableName(DICTYPE);
           String q;
           if(SearchLanguage=='E') {
               q = "SELECT * FROM dhanushkatable WHERE `field1` LIKE '" + searchWord + "%' AND `field5`='" + tableName + "' GROUP BY field2";
           }else {
               q = "SELECT * FROM dhanushkatable WHERE `field2` LIKE '" + searchWord + "%' AND `field5`='" + tableName + "' GROUP BY field2";
           }
           Cursor result = mDB.rawQuery(q, null);


           while (result.moveToNext()) {

               source.add(result.getString(result.getColumnIndex("field2")));

           }

       }
        return source;
    }


    public WordList getWordSearchSinhala(String key, int dicType) {

        String tableName = getTableName(dicType);

        String q = "SELECT DISTINCT * FROM dhanushkatable where upper(field2)=upper(?) and field5='" + tableName + "'";

        Cursor result = mDB.rawQuery(q, new String[]{key});


        WordList wordList = new WordList();
        while (result.moveToNext()) {

            wordList.key = result.getString(result.getColumnIndex("field2"));
            wordList.value = result.getString(result.getColumnIndex("field3"));
        }
        return wordList;
    }
    public WordList getWordSearchEnglish(String key, int dicType){

        String tableName=getTableName(dicType);

        String q="SELECT DISTINCT * FROM dhanushkatable where upper(field1)=upper(?) and field5='"+tableName+"'";

        Cursor result=mDB.rawQuery(q,new String[] {key});


        WordList wordList =new WordList();
        while (result.moveToNext()){

            wordList.key=result.getString(result.getColumnIndex("field2"));
            wordList.value=result.getString(result.getColumnIndex(COL_VALUE));
        }
        return wordList;

    }
public ArrayList<WordDetails> getMeanings(String word ,int dicType){

        ArrayList<WordDetails> wordDetails=new ArrayList<>();

    String tableName=getTableName(dicType);
    String q = "SELECT * FROM dhanushkatable where upper(field2)=upper(?) and field5='" + tableName + "'";

    Cursor result = mDB.rawQuery(q, new String[]{word});
    int i=1;
    while (result.moveToNext()){
        wordDetails.add(new WordDetails(result.getString(result.getColumnIndex("field3")),result.getString(result.getColumnIndex("field2")),result.getString(result.getColumnIndex("field4")),i));

        i++;
    }

     return wordDetails;

}
    public void addBookmark(String key,String value){

        try{

            String q="insert INTO bookmark(["+COL_KEY+"],["+COL_VALUE+"]) values (?,?);";
            mDB.execSQL(q,new Object[]{key,value});
        }catch (android.database.SQLException ex)
        {


        }

    }

    public void removeBookmark(String key,String value){

        try{

            String q="delete from bookmark where upper(["+COL_KEY+"])=upper(?) AND ["+COL_VALUE+"]=?;";
            mDB.execSQL(q,new Object[]{key,value});
        }catch (android.database.SQLException ex)
        {


        }

    }


    public ArrayList<String> getAllWordFromBookmark(String value){

        String q="SELECT * from bookmark where value='"+value+"'";

        Cursor result=mDB.rawQuery(q,null);

        ArrayList<String> source=new ArrayList<>();
        while (result.moveToNext()){

            source.add(result.getString(result.getColumnIndex(COL_KEY)));

        }
        return source;

    }


    public  boolean isMark(WordList wordList){

        String q="SELECT * from bookmark where upper([key]) = upper(?) and [value]=?";

        Cursor result=mDB.rawQuery(q,new String[]{wordList.key, wordList.value});

        return result.getCount()>0;
    }

    public WordList getWordFromBookmark(String key){

        String q="SELECT * FROM bookmark WHERE upper([key]) = upper(?)";

        Cursor result=mDB.rawQuery(q,new String[]{key});
        WordList wordList =null;

        while (result.moveToNext()){
            wordList =new WordList();
           wordList.key=result.getString(result.getColumnIndex(COL_KEY));
            wordList.value=result.getString(result.getColumnIndex(COL_VALUE));

        }
        return wordList;

    }



    public String getTableName(int dicType){
        String tableName="";

    if(dicType==R.id.sinhala_pali){

        tableName=TBL_SI_PL;

    }else if(dicType==R.id.pali_sinhala){
        tableName=TBL_PL_SI;
    }

    return tableName;
    }

    public void CleenBookmark(){
        try{

            String q="delete from bookmark;";
            mDB.execSQL(q,null);
        }catch (android.database.SQLException ex)
        {


        }

    }


}
