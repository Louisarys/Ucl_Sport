package com.sport_ucl.module;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.sport_ucl.DBAdapter;
import com.sport_ucl.dialog.DialogSportFavoris;
import com.sport_ucl.DisciplineToken;
import com.sport_ucl.EvenementListAdapter;
import com.sport_ucl.PlanningSingleton;
import com.sport_ucl.R;

import java.util.ArrayList;

//todo refresh la page apres le delete
//todo delete les donnees en ligne

public class SportFavoris extends AppCompatActivity {

    private static final String TAG = "SportFavoris";
    public static String fsport   = null;
    public static String fsalle   = null;
    public static String fdate    = null;
    public static String fheure   = null;
    public static String fLieu = null;
    public static ListView mListViewFavoris;
    public static ArrayList<DisciplineToken> eventListFavoris;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_favoris);

        context = this;
        mListViewFavoris = (ListView) findViewById(R.id.listViewFavoris);

        eventListFavoris = PlanningSingleton.getINSTANCE().getFavorisList(this);

        final EvenementListAdapter adapterFavoris = new EvenementListAdapter(this,R.layout.adapter_view_layout, eventListFavoris);
        mListViewFavoris.setAdapter(adapterFavoris);


        mListViewFavoris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PlanningSingleton.getINSTANCE().setToken(eventListFavoris.get(position));
                openDialogSportFavoris(eventListFavoris.get(position));
            }
        });


    }

    public void openDialogSportFavoris(DisciplineToken token) {
        DialogSportFavoris dialogSportFavoris = new DialogSportFavoris();
        dialogSportFavoris.setEvenement(token);
        dialogSportFavoris.show(getSupportFragmentManager(), "exemple dialog sport");
    }

    public static void changeListViewAdapter(){

        DBAdapter dbAdapter = new DBAdapter(context);

        eventListFavoris = PlanningSingleton.getINSTANCE().getFavorisList(context);

        final EvenementListAdapter adapterFavoris = new EvenementListAdapter(context,R.layout.adapter_view_layout, eventListFavoris);
        mListViewFavoris.setAdapter(adapterFavoris);
    }

}
