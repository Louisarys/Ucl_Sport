package com.sport_ucl;

/* Cette classe contient les methodes d'initialisation et d'acces a la database SQLite
* locale de l'application
*/

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDBName.db";
    public static final int VERSION_NUMBER = 61;
    public static final String RAW_DATABASE_PATH = "R.raw_sport_month.raw_database";
    public static final String RAW_DELETE_PATH = "R.raw_sport_month.raw_drop_table";
    Context context;

    //constructeur de la classe

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME , null, VERSION_NUMBER);
        this.context = context;
    }

    //cette methode est appelee par defaut
    //lors du premier appel de cette classe dans le programme

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] construct = databaseFileReader(R.raw.raw_database).split(";");
        for (String s : construct){
            db.execSQL(s+";");
        }
    }

    //Cette methode est appelee lorsque l'on change le numero de version de
    //la base de donnee

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] remove = databaseFileReader(R.raw.raw_drop_table).split(";");
        for (String s : remove){
            db.execSQL(s);
        }
        onCreate(db);
    }

    /***************   methode d'aide ********************* */

    //permet d'inserer de nouveaux utilisateurs en local

    public void insertUsers (SQLiteDatabase db, int id,String login, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("Login", login);
        contentValues.put("Password", password);
        db.insert("Users", null, contentValues);
    }


    /***************  methodes de lecture externe **********************/

    //cette fonction renvoit sous forme d'un string unique le contenu d'un dossier
    //INPUT : R.raw_sport_month.<ressource>
    //elle permet de lire les deux fichiers text dans raw_sport_month pour construire et effacer
    //la database locale

    public String databaseFileReader(int id){
        StringBuilder stringBuilder = new StringBuilder();
        try{
            InputStream inputStream = context.getResources().openRawResource(id);
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));

            String eachLine = bufferedReader.readLine();
            while (eachLine != null){
                stringBuilder.append(eachLine);
                eachLine = bufferedReader.readLine();
            }
        }
        catch (Exception e){
            Log.e("ReadinError", "BufferedReader have encounter an error");
        }
        return stringBuilder.toString();
    }
    /***************  methodes delete **********************/

    public void deleteFavoris(SQLiteDatabase db, long discardUserId, long discardEventId){
        db.delete("favoris", "id_User = "+ discardUserId+" and id_Event= "+discardEventId, null);
    }

    /******************************** setting des disciplines ******************/

    public void disciplineFill(SQLiteDatabase db){
        String[] construct = databaseFileReader(R.raw.raw_sport_month).split(";");
        for (String s : construct){
            db.execSQL(s+";");
        }
    }
}
